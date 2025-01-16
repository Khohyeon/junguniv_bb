package com.example.junguniv_bb.domain.board._faq.controller;

import com.example.junguniv_bb.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/masterpage_sys/board/faq")
public class BoardFaqController {

    private final BoardService boardService;

    @GetMapping("/listForm")
    public String faqListForm() {
        return "masterpage_sys/board/faq/listForm";
    }

    @GetMapping("/saveForm")
    public String faqSaveForm() {
        return "masterpage_sys/board/faq/saveForm";
    }

}
