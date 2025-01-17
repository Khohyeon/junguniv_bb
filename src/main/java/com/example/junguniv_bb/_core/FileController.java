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

    @Value("${file.upload.base-dir}")
    private String baseDir; // upload

    @Value("${file.upload.directories.member}")
    private String memberDir; // upload/member

    @Value("${file.upload.directories.member-temp}")
    private String memberTempDir; // upload/member/temp

    @Value("${file.upload.directories.board}")
    private String boardDir; // upload/board

    @Value("${file.upload.directories.board-temp}")
    private String boardTempDir; // upload/board/temp


    /**
     * 임시 디렉토리에 파일을 업로드합니다.
     * 
     * @param type 파일 업로드 타입 (member 또는 board)
     * @param file 업로드할 파일 (MultipartFile)
     * @return 업로드된 파일 정보 (fileName, originalFileName)를 포함한 ResponseEntity
     * @throws IllegalArgumentException 잘못된 업로드 타입이 전달된 경우
     * @throws Exception 파일 업로드 중 발생하는 모든 예외
     */
    @PostMapping("/upload/temp/{type}")
    public ResponseEntity<?> uploadTempFile(
            @PathVariable String type,
            @RequestParam("file") MultipartFile file) {
        
        try {
            // 파일 유효성 검사
            if (!FileUtils.isImageFile(file)) {
                return ResponseEntity.badRequest().body("이미지 파일만 업로드 가능합니다.");
            }

            if (!FileUtils.validateFileSize(file, 10 * 1024 * 1024)) { // 10MB
                return ResponseEntity.badRequest().body("파일 크기는 10MB를 초과할 수 없습니다.");
            }

            // type에 따른 임시 디렉토리 선택
            String tempDir = switch (type) {
                case "member" -> memberTempDir;
                case "board" -> boardTempDir;
                default -> throw new IllegalArgumentException("잘못된 업로드 타입입니다: " + type);
            };

            // 디렉토리 생성 및 파일 업로드
            FileUtils.createDirectory(tempDir);
            String savedFileName = FileUtils.uploadFile(file, tempDir);

            return ResponseEntity.ok().body(Map.of(
                "fileName", savedFileName,
                "originalFileName", file.getOriginalFilename()
            ));

        } catch (Exception e) {
            log.error("파일 업로드 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("파일 업로드에 실패했습니다.");
        }
    }

    /**
     * 임시 디렉토리에 있는 파일을 실제 디렉토리로 이동시킵니다.
     * 
     * @param type 파일 타입 (member 또는 board)
     * @param fileName 이동할 파일명
     * @return 이동 결과를 포함한 ResponseEntity
     *         - 성공 시: 200 OK와 성공 메시지
     *         - 실패 시: 400 Bad Request 또는 500 Internal Server Error와 실패 메시지
     * @throws IllegalArgumentException 잘못된 파일 타입이 전달된 경우
     */
    @PostMapping("/move/{type}/{fileName}")
    public ResponseEntity<?> moveFile(
            @PathVariable String type,
            @PathVariable String fileName) {
        
        try {
            // type에 따른 소스/타겟 디렉토리 선택
            String sourceDir = switch (type) {
                case "member" -> memberTempDir;
                case "board" -> boardTempDir;
                default -> throw new IllegalArgumentException("잘못된 파일 타입입니다: " + type);
            };

            String targetDir = switch (type) {
                case "member" -> memberDir;
                case "board" -> boardDir;
                default -> throw new IllegalArgumentException("잘못된 파일 타입입니다: " + type);
            };

            boolean moved = FileUtils.moveFile(fileName, sourceDir, targetDir);

            if (moved) {
                return ResponseEntity.ok().body(Map.of(
                    "success", true,
                    "message", "파일이 성공적으로 이동되었습니다."
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "파일 이동에 실패했습니다."
                ));
            }

        } catch (Exception e) {
            log.error("파일 이동 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "파일 이동 중 오류가 발생했습니다."
            ));
        }
    }

    /**
     * 파일 삭제 API
     * 지정된 타입의 디렉토리에서 파일을 삭제합니다.
     * 
     * @param type 파일 타입 (member, board, member-temp, board-temp)
     * @param fileName 삭제할 파일명
     * @return 삭제 결과를 포함한 ResponseEntity
     *         - 성공 시: 200 OK
     *         - 실패 시: 400 Bad Request 또는 500 Internal Server Error와 실패 메시지
     * @throws IllegalArgumentException 잘못된 파일 타입이 전달된 경우
     */
    @DeleteMapping("/{type}/{fileName}")
    public ResponseEntity<?> deleteFile(
            @PathVariable String type,
            @PathVariable String fileName) {
        try {
            String targetDir = switch (type) {
                case "member" -> memberDir;
                case "board" -> boardDir;
                case "member-temp" -> memberTempDir;
                case "board-temp" -> boardTempDir;
                default -> throw new IllegalArgumentException("잘못된 파일 타입입니다: " + type);
            };

            boolean deleted = FileUtils.deleteFile(fileName, targetDir);
            
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body("파일 삭제에 실패했습니다.");
            }
        } catch (Exception e) {
            log.error("파일 삭제 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("파일 삭제 중 오류가 발생했습니다.");
        }
    }
} 