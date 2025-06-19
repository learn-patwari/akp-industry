package com.example.s3hierarchy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FolderDTO {
    private String folderName;
    private String folderPath;
    private int subDirectoryCount;
    private String lastModifiedDate;
    private int fileCount;
    private List<FolderDTO> subdirectories;
}
