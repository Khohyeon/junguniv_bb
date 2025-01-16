package com.example.junguniv_bb.domain.board.controller;

import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.board.dto.BoardSaveReqDTO;
import com.example.junguniv_bb.domain.board.dto.BoardSearchResDTO;
import com.example.junguniv_bb.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/save")
    public ResponseEntity<?> saveBoard(@ModelAttribute BoardSaveReqDTO requestDTO) {
        boardService.saveBoard(requestDTO); // 서비스 호출
        return ResponseEntity.ok(APIUtils.success("게시판등록이 성공적으로 완료되었습니다."));
    }
}
