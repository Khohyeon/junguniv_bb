package com.example.junguniv_bb._core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.io.File;

@Slf4j
public class FileUtils {

    /**
     * UUID를 사용하여 고유한 파일명 생성
     * @param originalFileName 원본 파일명
     * @return UUID + 확장자
     */
    public static String generateUniqueFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        return UUID.randomUUID().toString() + "." + extension;
    }

    /**
     * 파일 확장자 추출
     * @param fileName 파일명
     * @return 확장자
     */
    private static String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 파일을 디렉토리에 업로드
     * @param file 업로드할 파일
     * @param directory 저장할 디렉토리 경로
     * @return 저장된 파일명
     */
    public static String uploadFile(MultipartFile file, String directory) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            // 디렉토리 생성
            Path dirPath = Paths.get(directory);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // 고유한 파일명 생성
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            Path filePath = dirPath.resolve(fileName);

            // 파일 저장
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            log.info("파일 저장 완료: {}", filePath);

            return fileName;
        } catch (IOException e) {
            log.error("파일 저장 실패: {}", e.getMessage(), e);
            throw new RuntimeException("파일 저장에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 파일 삭제
     * @param fileName 삭제할 파일명
     * @param directory 파일이 저장된 디렉토리 경로
     * @return 삭제 성공 여부
     */
    public static boolean deleteFile(String fileName, String directory) {
        if (fileName == null || directory == null) {
            return false;
        }

        try {
            Path filePath = Paths.get(directory, fileName);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("파일 삭제 성공: {}", filePath);
                return true;
            }
            return false;
        } catch (IOException e) {
            log.error("파일 삭제 실패: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 파일이 이미지인지 확인
     * @param file 확인할 파일
     * @return 이미지 여부
     */
    public static boolean isImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    /**
     * 파일 크기 검증
     * @param file 검증할 파일
     * @param maxSize 최대 허용 크기 (bytes)
     * @return 허용 여부
     */
    public static boolean validateFileSize(MultipartFile file, long maxSize) {
        return file != null && file.getSize() <= maxSize;
    }

    /**
     * 디렉토리 생성
     * @param directory 생성할 디렉토리 경로
     */
    public static void createDirectory(String directory) {
        try {
            Path path = Paths.get(directory);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                log.info("디렉토리 생성: {}", path);
            }
        } catch (IOException e) {
            log.error("디렉토리 생성 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("디렉토리 생성 실패", e);
        }
    }

    /**
     * 임시 파일 정리 (특정 시간 이상 된 파일 삭제)
     * @param directory 정리할 디렉토리 경로
     * @param hours 경과 시간 (시간 단위)
     */
    public static void cleanupDirectory(String directory, long hours) {
        try {
            Files.walk(Paths.get(directory))
                    .filter(Files::isRegularFile)
                    .filter(path -> {
                        try {
                            FileTime creationTime = Files.getLastModifiedTime(path);
                            return Duration.between(creationTime.toInstant(),
                                    Instant.now()).toHours() >= hours;
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                            log.info("파일 삭제됨: {}", path);
                        } catch (IOException e) {
                            log.error("파일 삭제 실패: {}", path, e);
                        }
                    });
        } catch (IOException e) {
            log.error("디렉토리 정리 중 오류 발생: {}", e.getMessage());
        }

    }

    /**
     * 파일을 소스 디렉토리에서 대상 디렉토리로 이동
     * @param fileName 이동할 파일명
     * @param sourceDir 소스 디렉토리 경로
     * @param targetDir 대상 디렉토리 경로
     * @return 이동 성공 여부
     */
    public static boolean moveFile(String fileName, String sourceDir, String targetDir) {
        if (fileName == null || sourceDir == null || targetDir == null) {
            return false;
        }

        try {
            // 대상 디렉토리가 없으면 생성
            createDirectory(targetDir);

            Path sourcePath = Paths.get(sourceDir, fileName);
            Path targetPath = Paths.get(targetDir, fileName);

            if (Files.exists(sourcePath)) {
                Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                log.info("파일 이동 완료: {} -> {}", sourcePath, targetPath);
                return true;
            } else {
                log.warn("소스 파일이 존재하지 않음: {}", sourcePath);
                return false;
            }
        } catch (IOException e) {
            log.error("파일 이동 실패: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 파일 다운로드를 위한 Resource 생성
     * @param fileName 파일명
     * @param directory 디렉토리 경로
     * @return 파일 Path
     * @throws IOException 파일을 찾을 수 없거나 접근할 수 없는 경우
     */
    public static Path getFileAsResource(String fileName, String directory) throws IOException {
        Path filePath = Paths.get(directory).resolve(fileName).normalize();
        
        if (!Files.exists(filePath)) {
            throw new IOException("파일을 찾을 수 없습니다: " + fileName);
        }

        // 디렉토리 탐색 방지
        if (!filePath.toFile().getCanonicalPath().startsWith(new File(directory).getCanonicalPath())) {
            throw new IOException("잘못된 파일 경로입니다.");
        }

        return filePath;
    }

    /**
     * Content-Type 결정
     * @param filePath 파일 경로
     * @return Content-Type 문자열
     */
    public static String determineContentType(Path filePath) {
        String contentType;
        try {
            contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                // 확장자로 Content-Type 추측
                String fileName = filePath.getFileName().toString().toLowerCase();
                if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                    contentType = "image/jpeg";
                } else if (fileName.endsWith(".png")) {
                    contentType = "image/png";
                } else if (fileName.endsWith(".gif")) {
                    contentType = "image/gif";
                } else if (fileName.endsWith(".pdf")) {
                    contentType = "application/pdf";
                } else {
                    contentType = "application/octet-stream";
                }
            }
        } catch (IOException e) {
            contentType = "application/octet-stream";
        }
        return contentType;
    }
} 