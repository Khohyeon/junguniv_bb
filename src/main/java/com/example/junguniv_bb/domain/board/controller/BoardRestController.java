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
            @RequestParam String title,
            @RequestParam String boardType,
            Pageable pageable) {

        // 팝업의 페이징 형태를 boardIdx 기준으로 DESC 내림차순으로 설정
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "bbsIdx"));

        Page<BoardSearchResDTO> searchResults = boardService.searchByName(title, boardType, pageable);
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
}
