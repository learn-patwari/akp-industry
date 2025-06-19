package com.example.s3hierarchy.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.example.s3hierarchy.dto.FolderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3HierarchyService {

    private final AmazonS3 amazonS3;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    private static final String CACHE_PREFIX = "s3:folders:";

    public List<FolderDTO> getFoldersUnderPrefix(String prefix) {
        if (!prefix.endsWith("/")) {
            prefix += "/";
        }
        String cacheKey = CACHE_PREFIX + prefix;

        List<FolderDTO> cached = (List<FolderDTO>) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return cached;
        }

        List<FolderDTO> folders = fetchFoldersFromS3(prefix);
        redisTemplate.opsForValue().set(cacheKey, folders);

        return folders;
    }

    private List<FolderDTO> fetchFoldersFromS3(String prefix) {
        ListObjectsV2Request req = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(prefix)
                .withDelimiter("/");

        ListObjectsV2Result result = amazonS3.listObjectsV2(req);

        List<FolderDTO> folderList = new ArrayList<>();

        for (String commonPrefix : result.getCommonPrefixes()) {
            String name = getLastPart(commonPrefix, prefix);
            FolderDTO dto = new FolderDTO();
            dto.setFolderName(name);
            dto.setFolderPath(commonPrefix);

            boolean hasSubfolder = hasSubfolder(commonPrefix);
            dto.setSubDirectoryCount(hasSubfolder ? 1 : 0);

            if (!hasSubfolder) {
                int fileCount = countFiles(commonPrefix);
                dto.setFileCount(fileCount);
                dto.setLastModifiedDate(getLastModified(commonPrefix));
            }

            folderList.add(dto);
        }

        return folderList;
    }

    private boolean hasSubfolder(String prefix) {
        ListObjectsV2Request req = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(prefix)
                .withDelimiter("/");

        return !amazonS3.listObjectsV2(req).getCommonPrefixes().isEmpty();
    }

    private int countFiles(String prefix) {
        ListObjectsV2Request req = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(prefix)
                .withDelimiter("/");

        return amazonS3.listObjectsV2(req).getObjectSummaries().size();
    }

    private String getLastModified(String prefix) {
        ListObjectsV2Request req = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(prefix);

        ListObjectsV2Result result = amazonS3.listObjectsV2(req);
        if (!result.getObjectSummaries().isEmpty()) {
            return result.getObjectSummaries().get(0).getLastModified().toString();
        }
        return null;
    }

    private String getLastPart(String fullPath, String parentPrefix) {
        String trimmed = fullPath.substring(parentPrefix.length());
        return trimmed.replace("/", "");
    }

    public void clearAllCache() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    public void preloadRootFolders() {
        getFoldersUnderPrefix("");
    }
}
