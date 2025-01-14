package com.example.junguniv_bb.masterpage.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/masterpage/board")
public class BoardController {

    @GetMapping("/listForm")
    public String listForm() {
        return "masterpage/board/listForm";
    }

    @GetMapping("/saveForm")
    public String saveForm() {
        return "masterpage/board/saveForm";
    }

    @GetMapping("/detailForm")
    public String detailForm() {
        return "masterpage/board/detailForm";
    }

    @GetMapping("/headForm")
    public String courseForm() {
        return "masterpage/board/headForm";
    }
}
