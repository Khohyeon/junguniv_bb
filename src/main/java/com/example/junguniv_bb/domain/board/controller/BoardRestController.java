package com.example.junguniv_bb.domain.board.controller;

import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.board.dto.BoardSaveReqDTO;
import com.example.junguniv_bb.domain.board.dto.BoardSearchResDTO;
import com.example.junguniv_bb.domain.board.dto.BoardUpdateReqDTO;
import com.example.junguniv_bb.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/board/api")
public class BoardRestController {

    private final BoardService boardService;

    /**
     *  [관리자모드] 홈페이지관리 - 게시판관리 - 게시판 조회기능 한번에 작업
     *  Model 응답 Page<BoardSearchResDTO>
     */
    @GetMapping("/search")
    public ResponseEntity<APIUtils.APIResult<Page<BoardSearchResDTO>>> searchBoards(
            @RequestParam(required = false) String title,        // 제목 또는 내용 검색 키워드
            @RequestParam String boardType,                     // 게시판 타입
            @RequestParam(required = false) String searchType,  // 검색 타입 (제목, 제목+내용, 내용)
            @RequestParam(required = false) String startDate,   // 작성일 시작
            @RequestParam(required = false) String endDate,     // 작성일 종료
            @RequestParam(required = false) String category,    // 카테고리
            Pageable pageable) {

        // 페이징 기본 설정: bbsIdx 기준 내림차순
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "bbsIdx"));

        // 서비스 호출하여 검색 수행
        Page<BoardSearchResDTO> searchResults = boardService.searchBoards(
                title,
                boardType,
                searchType,
                startDate,
                endDate,
                category,
                pageable
        );

        return ResponseEntity.ok(APIUtils.success(searchResults));
    }

    /**
     *  [관리자모드] 홈페이지관리 - 게시판관리 - 게시판목록 - 글쓰기
     *  각각 게시판 등록을 하나의 매핑으로 처리 (BoardSaveReqDTO.boardType 으로 구분)
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveBoard(@ModelAttribute BoardSaveReqDTO boardSaveReqDTO) {
        boardService.saveBoard(boardSaveReqDTO); // 서비스 호출
        return ResponseEntity.ok(APIUtils.success("게시판등록이 성공적으로 완료되었습니다."));
    }

    /**
     *  [관리자모드] 홈페이지관리 - 게시판관리 - 게시판목록 - 삭제하기
     *  체크박스에 체크된 팝업 일괄 삭제
     */
    @DeleteMapping("/deletes")
    public ResponseEntity<APIUtils.APIResult<String>> popupDeletes(@RequestBody List<Long> boardIds) {
        boardService.boardListDelete(boardIds);
        return ResponseEntity.ok(APIUtils.success("게시판삭제가 성공적으로 완료되었습니다."));
    }

    /**
     *  [관리자모드] 홈페이지관리 - 게시판관리 - 게시판상세 - 삭제하기
     *  detailForm 페이지에서 삭제하기
     */
    @DeleteMapping("/delete")
    public ResponseEntity<APIUtils.APIResult<String>> popupDelete(@RequestBody Long boardId) {
        boardService.boardDelete(boardId);
        return ResponseEntity.ok(APIUtils.success("게시판삭제가 성공적으로 완료되었습니다."));
    }

    /**
     *  [관리자모드] 홈페이지관리 - 게시판관리 - 게시판목록 - 상세보기 - 수정하기
     *  각각 게시판 수정을 하나의 매핑으로 처리 (BoardUpdateReqDTO.boardType 으로 구분)
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateBoard(@ModelAttribute BoardUpdateReqDTO boardUpdateReqDTO) {
        boardService.updateBoard(boardUpdateReqDTO); // 서비스 호출
        return ResponseEntity.ok(APIUtils.success("게시판수정이 성공적으로 완료되었습니다."));
    }

    @GetMapping("/categories")
    @ResponseBody
    public ResponseEntity<?> getCategories(@RequestParam String boardType) {
        String boardCategory = boardService.getBoardCategory(boardType);
        return ResponseEntity.ok(APIUtils.success(boardCategory));
    }
}
