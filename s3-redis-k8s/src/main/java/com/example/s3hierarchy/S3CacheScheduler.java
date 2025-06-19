package com.example.s3hierarchy.scheduler;

import com.example.s3hierarchy.service.S3HierarchyService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3CacheScheduler {

    private final S3HierarchyService s3HierarchyService;

    @Scheduled(cron = "0 0 3 * * ?", zone = "Asia/Seoul")
    public void clearCache() {
        System.out.println("Clearing S3 folder cache at 3AM KST...");
        s3HierarchyService.clearAllCache();
    }

    @Scheduled(cron = "0 0 5 * * ?", zone = "Asia/Seoul")
    public void warmUpCache() {
        System.out.println("Warming up S3 folder cache at 5AM KST...");
        s3HierarchyService.preloadRootFolders();
    }
}
