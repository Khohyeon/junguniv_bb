package com.example.junguniv_bb.domain.board._qna.controller;

import com.example.junguniv_bb.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/masterpage_sys/board/qna")
public class BoardQnaController {

    private final BoardService boardService;


    @GetMapping("/listForm")
    public String qnaListForm() {
        return "masterpage_sys/board/qna/listForm";
    }

    @GetMapping("/saveForm")
    public String qnaSaveForm() {
        return "masterpage_sys/board/qna/saveForm";
    }
}
