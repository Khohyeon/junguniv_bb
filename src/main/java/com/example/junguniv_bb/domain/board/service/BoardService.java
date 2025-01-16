package com.example.junguniv_bb.domain.board.service;

import com.example.junguniv_bb.domain.board.dto.BbsGroupPageResDTO;
import com.example.junguniv_bb.domain.board.dto.BoardSaveReqDTO;
import com.example.junguniv_bb.domain.board.dto.BoardSearchResDTO;
import com.example.junguniv_bb.domain.board.model.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.h2.store.fs.FileUtils.createDirectory;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BbsRepository bbsRepository;
    private final BbsGroupRepository bbsGroupRepository;
    private final BbsFileRepository bbsFileRepository;
    private final EntityManager entityManager;

    /* 파일 업로드 디렉토리 경로 설정 */
    @Value("${file.upload.directories.board}")
    private String uploadDirPath;

    @Value("${file.upload.directories.board-temp}")
    private String tempDir;


    /**
     * 게시판 검색 조회
     * 응답 형태 : Page<BoardSearchReqDTO>
     */
    public Page<BoardSearchResDTO> searchByName(String title, String boardType, Pageable pageable) {
        BbsGroup bbsGroup = bbsGroupRepository.findByBbsGroupName(boardType);

        Page<Bbs> bbsPage = bbsRepository.findByTitleContainingIgnoreCaseAndBbsGroup(title, bbsGroup,pageable);
        return bbsPage.map(bbs ->
                new BoardSearchResDTO(
                        bbs.getBbsIdx(),
                        bbs.getBbsGroup(),
                        bbs.getTitle(),
                        bbs.getFormattedCreatedDate2(),
                        bbs.getReadNum()
                ));
    }

    /**
     * 홈페이지게시판 리스트 조회
     * 응답 형태 : Page<BbsGroupPageResDTO>
     */
    public Page<BbsGroupPageResDTO> getBbsGroupPage(Pageable pageable) {
        Page<BbsGroup> bbsGroupPage = bbsGroupRepository.findAll(pageable);

        return bbsGroupPage.map(bbsGroup ->
                new BbsGroupPageResDTO(
                        bbsGroup.getBbsGroupIdx(),
                        bbsGroup.getBbsId(),
                        bbsGroup.getBbsGroupName(),
                        bbsGroup.getFileNum()
                ));
    }

    @Transactional
    public void saveBoard(BoardSaveReqDTO boardSaveReqDTO) {

        String boardType = boardSaveReqDTO.boardType();
        BbsGroup bbsGroup = bbsGroupRepository.findByBbsGroupName(boardType);

        // BBS 엔터티 저장
        Bbs bbs = bbsRepository.save(boardSaveReqDTO.saveEntity(bbsGroup));

        // BBS File 엔티티 저장
        saveFiles(bbs, boardSaveReqDTO.attachments());
        // 강제 플러시
        entityManager.flush();

    }

    private void saveFiles(Bbs bbs, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return;
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    String originalFileName = file.getOriginalFilename();
                    String savedFileName = System.currentTimeMillis() + "_" + originalFileName;

                    // 디렉토리 경로 확인 및 생성
                    File uploadDir = new File(uploadDirPath);
                    if (!uploadDir.exists()) {
                        boolean dirCreated = uploadDir.mkdirs();
                        if (dirCreated) {
                            System.out.println("디렉토리가 생성되었습니다: " + uploadDirPath);
                        } else {
                            throw new RuntimeException("디렉토리 생성에 실패했습니다: " + uploadDirPath);
                        }
                    }

                    // 파일 저장 경로
                    String filePath = uploadDirPath + File.separator + savedFileName;

                    // 파일 저장
                    file.transferTo(new File(filePath));
                    System.out.println("파일 저장 성공: " + filePath);

                    // 엔티티 저장 로직
                    BbsFile bbsFile = new BbsFile(
                            null,
                            "BOARD",
                            bbs,
                            savedFileName,
                            originalFileName,
                            "createUser",
                            "updateUser"
                    );
                    bbsFileRepository.save(bbsFile);

                } catch (IOException e) {
                    throw new RuntimeException("파일 저장 중 오류 발생: " + e.getMessage(), e);
                }
            }
        }
    }


    /* 프로젝트 실행시 yml 경로에 해당하는 파일 업로드 디렉토리 생성 */
    @PostConstruct
    public void init() {
        try {
            createDirectory(uploadDirPath); // 업로드 디렉토리 생성
            createDirectory(tempDir); // 임시 디렉토리 생성
            cleanupTempDirectory(); // 임시 디렉토리 초기화
        } catch (IOException e) {
            // 로그 추가
            System.err.println("파일 업로드 디렉토리 초기화 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("Could not create directories!", e);
        }
    }

    private void createDirectory(String directory) throws IOException {
        Path path = Paths.get(directory);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            System.out.println("디렉토리 생성: " + path);
        }
    }

    // 임시 폴더 청소 (24시간 이상 된 파일 삭제)
    @Scheduled(cron = "0 0 * * * *") // 매시간 실행
    public void cleanupTempDirectory() {
        try {
            Files.walk(Paths.get(tempDir))
                    .filter(Files::isRegularFile)
                    .filter(path -> {
                        try {
                            FileTime creationTime = Files.getLastModifiedTime(path);
                            return Duration.between(creationTime.toInstant(),
                                    Instant.now()).toHours() >= 24;
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                            log.info("임시 파일 삭제됨: {}", path);
                        } catch (IOException e) {
                            log.error("임시 파일 삭제 실패: {}", path, e);
                        }
                    });
        } catch (IOException e) {
            log.error("임시 폴더 청소 중 오류 발생", e);
        }
    }


}
