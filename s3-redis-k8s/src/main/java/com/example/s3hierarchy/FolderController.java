package com.example.s3hierarchy.controller;

import com.example.s3hierarchy.dto.FolderDTO;
import com.example.s3hierarchy.service.S3HierarchyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/folders")
@RequiredArgsConstructor
public class FolderController {

    private final S3HierarchyService s3HierarchyService;

    @GetMapping("/{prefix}")
    public List<FolderDTO> getFolders(@PathVariable String prefix) {
        return s3HierarchyService.getFoldersUnderPrefix(prefix);
    }
}
