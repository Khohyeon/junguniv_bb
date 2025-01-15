package com.example.junguniv_bb.domain.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/board")
public class BoardController {

    @GetMapping("/noticeForm")
    public String noticeForm() {
        return "masterpage_sys/board/noticeForm";
    }

    @GetMapping("/suggestionForm")
    public String suggestionForm() {
        return "masterpage_sys/board/suggestionForm";
    }

    @GetMapping("/faqForm")
    public String faqForm() {
        return "masterpage_sys/board/faqForm";
    }
    @GetMapping("/qnaForm")
    public String qnaForm() {
        return "masterpage_sys/board/qnaForm";
    }

    @GetMapping("/dataForm")
    public String dataForm() {
        return "masterpage_sys/board/dataForm";
    }

    @GetMapping("/consultingForm")
    public String consultingForm() {
        return "masterpage_sys/board/consultingForm";
    }

    @GetMapping("/saveForm")
    public String saveForm() {
        return "masterpage_sys/board/saveForm";
    }

    @GetMapping("/detailForm")
    public String detailForm() {
        return "masterpage_sys/board/detailForm";
    }

    @GetMapping("/headForm")
    public String courseForm() {
        return "masterpage_sys/board/headForm";
    }
}
