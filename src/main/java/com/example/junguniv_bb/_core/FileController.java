package com.example.junguniv_bb._core;

import com.example.junguniv_bb._core.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    @Value("${file.upload.directories.member}")
    private String uploadDirPath;

    @Value("${file.upload.directories.member-temp}")
    private String tempDirPath;

    /**
     * 임시 폴더에 파일 업로드
     */
    @PostMapping("/upload/temp")
    public ResponseEntity<?> uploadTempFile(@RequestParam("file") MultipartFile file) {
        try {
            // 이미지 파일 검증
            if (!FileUtils.isImageFile(file)) {
                return ResponseEntity.badRequest().body("이미지 파일만 업로드 가능합니다.");
            }

            // 파일 크기 검증 (10MB)
            if (!FileUtils.validateFileSize(file, 10 * 1024 * 1024)) {
                return ResponseEntity.badRequest().body("파일 크기는 10MB를 초과할 수 없습니다.");
            }

            // temp 폴더에 파일 저장
            String fileName = FileUtils.uploadFile(file, tempDirPath);
            
            Map<String, String> response = new HashMap<>();
            response.put("fileName", fileName);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("파일 업로드 실패: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 임시 폴더에서 실제 폴더로 파일 이동
     */
    @PostMapping("/move/{fileName}")
    public ResponseEntity<?> moveFile(@PathVariable String fileName) {
        try {
            boolean moved = FileUtils.moveFile(fileName, tempDirPath, uploadDirPath);
            if (moved) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body("파일 이동 실패");
            }
        } catch (Exception e) {
            log.error("파일 이동 실패: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 파일 삭제
     */
    @DeleteMapping("/{directory}/{fileName}")
    public ResponseEntity<?> deleteFile(
            @PathVariable String directory,
            @PathVariable String fileName) {
        try {
            String targetDir = switch (directory) {
                case "member" -> uploadDirPath;
                case "temp" -> tempDirPath;
                default -> throw new IllegalArgumentException("잘못된 디렉토리입니다.");
            };

            boolean deleted = FileUtils.deleteFile(fileName, targetDir);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body("파일 삭제 실패");
            }
        } catch (Exception e) {
            log.error("파일 삭제 실패: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 