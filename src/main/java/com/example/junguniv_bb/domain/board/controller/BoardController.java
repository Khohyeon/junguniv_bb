package com.example.junguniv_bb.domain.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/board")
public class BoardController {

    @GetMapping("/listForm")
    public String listForm() {
        return "masterpage_sys/board/listForm";
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
