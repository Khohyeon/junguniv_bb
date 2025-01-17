package com.example.junguniv_bb.domain.board.service;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import com.example.junguniv_bb._core.util.FileUtils;
import com.example.junguniv_bb.domain.board.dto.*;
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
import java.util.Arrays;

import java.util.List;

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

    /* 프로젝트 실행시 yml 경로에 해당하는 파일 업로드 디렉토리 생성 */
    @PostConstruct
    public void init() {
        FileUtils.createDirectory(uploadDirPath);
        FileUtils.createDirectory(tempDir);
        cleanupTempDirectory(); // 임시 디렉토리 초기화
    }

    // 임시 폴더 청소 (24시간 이상 된 파일 삭제)
    @Scheduled(cron = "0 0 * * * *") // 매시간 실행
    public void cleanupTempDirectory() {
        FileUtils.cleanupDirectory(tempDir, 24); // 24시간 이상 된 파일 삭제
    }

    /**
     * 첨부파일 List 를 저장하는 메서드
     */
    private void saveFiles(Bbs bbs, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return;
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    // 파일 업로드 및 이동
                    String savedFileName = FileUtils.uploadFile(file, tempDir);
                    if (savedFileName == null) {
                        continue;
                    }

                    // 엔티티 저장 로직
                    BbsFile bbsFile = new BbsFile(
                            null,
                            "BOARD",
                            bbs,
                            savedFileName,
                            file.getOriginalFilename(),
                            "createUser",
                            "updateUser"
                    );
                    bbsFileRepository.save(bbsFile);
                    log.info("파일 저장 성공: {}", savedFileName);

                } catch (RuntimeException e) {
                    log.error("파일 저장 중 오류 발생: {}", e.getMessage(), e);
                    throw new RuntimeException("파일 저장에 실패했습니다.", e);
                }
            }
        }
    }

    /**
     * 게시판 검색 조회 BoardType 을 통해 모두 하나의 매핑으로 작업
     * 응답 형태 : Page<BoardSearchReqDTO>
     */
    public Page<BoardSearchResDTO> searchByName(String title, String boardType, Pageable pageable) {
        BbsGroup bbsGroup = bbsGroupRepository.findByBbsId(boardType);

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
     * 게시판 상세페이지 BoardType 을 통해 모두 하나의 매핑으로 작업
     * 응답 형태 : BoardDetailResDTO
     */
    public BoardDetailResDTO getBoardDetail(Long bbsIdx) {
        Bbs bbs = bbsRepository.findById(bbsIdx)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_BBS.getMessage()));

        return new BoardDetailResDTO(
                bbs.getBbsIdx(),
                bbs.getTitle(),
                bbs.getWriter(),
                bbs.getFormattedCreatedDate2(),
                bbs.getReadNum(),
                bbs.getContents()
        );
    }

    /**
     * 홈페이지게시판 리스트 조회
     * 응답 형태 : Page<BbsGroupPageResDTO>
     */
    public Page<BbsGroupPageResDTO> getBbsGroupPage(Pageable pageable) {
        Page<BbsGroup> bbsGroupPage = bbsGroupRepository.findAll(pageable);
        return bbsGroupPage.map(bbsGroup -> {

            // DTO에 전달 (그대로 전달하거나 가공해서 사용 가능)
            return new BbsGroupPageResDTO(
                    bbsGroup.getBbsGroupIdx(),
                    bbsGroup.getBbsId(),
                    bbsGroup.getBbsGroupName(),
                    bbsGroup.getCategory(),
                    bbsGroup.getFileNum(),
                    bbsGroup.getSkin(),
                    bbsGroup.getOptionSecretAuth(),
                    bbsGroup.getOptionReplyAuth(),
                    bbsGroup.getOptionCommentAuth(),
                    bbsGroup.getReadAuth(),
                    bbsGroup.getWriteAuth(),
                    bbsGroup.getCommentAuth(),
                    bbsGroup.getReplyAuth()
            );
        });
    }

    /**
     * 홈페이지게시판 등록 BoardType 을 통해 모두 하나의 매핑으로 작업
     * 요청 형태 : BoardSaveReqDTO
     */
    @Transactional
    public void saveBoard(BoardSaveReqDTO boardSaveReqDTO) {
        try {
            String boardType = boardSaveReqDTO.boardType();
            BbsGroup bbsGroup = bbsGroupRepository.findByBbsId(boardType);

            // BBS 엔터티 저장
            Bbs bbs = bbsRepository.save(boardSaveReqDTO.saveEntity(bbsGroup));

            // BBS File 엔티티 저장
            saveFiles(bbs, boardSaveReqDTO.attachments());

            // 강제 플러시
            entityManager.flush();
        } catch (Exception e) {
            // 업로드된 파일이 있다면 삭제 시도
            if (boardSaveReqDTO.attachments() != null) {
                for (MultipartFile file : boardSaveReqDTO.attachments()) {
                    if (!file.isEmpty()) {
                        String fileName = FileUtils.generateUniqueFileName(file.getOriginalFilename());
                        FileUtils.deleteFile(fileName, uploadDirPath);
                    }
                }
            }
            throw e;
        }
    }

    /**
     * 홈페이지게시판 수정 BoardType 을 통해 모두 하나의 매핑으로 작업
     * 요청 형태 : BoardUpdateReqDTO
     */
    @Transactional
    public void updateBoard(BoardUpdateReqDTO boardUpdateReqDTO) {
        try {
            String boardType = boardUpdateReqDTO.boardType();
            BbsGroup bbsGroup = bbsGroupRepository.findByBbsId(boardType);

            // BBS 엔터티 저장
            Bbs bbs = bbsRepository.save(boardUpdateReqDTO.updateEntity(bbsGroup));

            // BBS File 엔티티 저장
            saveFiles(bbs, boardUpdateReqDTO.attachments());

            // 강제 플러시
            entityManager.flush();
        } catch (Exception e) {
            // 업로드된 파일이 있다면 삭제 시도
            if (boardUpdateReqDTO.attachments() != null) {
                for (MultipartFile file : boardUpdateReqDTO.attachments()) {
                    if (!file.isEmpty()) {
                        String fileName = FileUtils.generateUniqueFileName(file.getOriginalFilename());
                        FileUtils.deleteFile(fileName, uploadDirPath);
                    }
                }
            }
            throw e;
        }
    }

    /**
     * 게시판 다중 삭제
     */
    @Transactional
    public void boardListDelete(List<Long> boardIds) {
        List<Bbs> boardToDelete = bbsRepository.findAllById(boardIds);
        if (boardToDelete.isEmpty()) {
            throw new Exception400("삭제할 게시판이 없습니다.");
        }

        // 첨부 파일 삭제
        for (Bbs bbs : boardToDelete) {
            List<BbsFile> files = bbsFileRepository.findAllByBbs(bbs);
            for (BbsFile file : files) {
                FileUtils.deleteFile(file.getFName1(), uploadDirPath);
            }
        }

        bbsRepository.deleteAll(boardToDelete);
    }

    /**
     * 게시판 삭제
     */
    @Transactional
    public void boardDelete(Long boardId) {
        Bbs boardToDelete = bbsRepository.findById(boardId)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_BBS.getMessage()));

        // 첨부 파일 삭제
        List<BbsFile> files = bbsFileRepository.findAllByBbs(boardToDelete);
        for (BbsFile file : files) {
            FileUtils.deleteFile(file.getFName1(), uploadDirPath);
        }

        bbsRepository.delete(boardToDelete);
    }

    public BoardUpdateResDTO getBoardUpdate(Long bbsIdx) {
        Bbs bbs = bbsRepository.findById(bbsIdx)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_BBS.getMessage()));

        List<String> attachments = bbsFileRepository.findAllByBbs(bbs)
                .stream()
                .map(BbsFile::getFName1)
                .toList();

        return new BoardUpdateResDTO(
                bbsIdx,
                bbs.getTitle(),
                bbs.getWriter(),
                bbs.getCategory(),
                bbs.getFormattedCreatedDate2(),
                bbs.getChkTopFix(),
                bbs.getFixStartDate(),
                bbs.getFixEndDate(),
                bbs.getChkMain(),
                bbs.getStartDate(),
                bbs.getEndDate(),
                bbs.getContents(),
                attachments
        );
    }

    public String getBoardCategory(String boardType) {
        BbsGroup bbsGroup = bbsGroupRepository.findByBbsId(boardType);
        return bbsGroup.getCategory();
    }
}
