package com.example.junguniv_bb.domain.board._notice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/board/notice")
public class BoardNoticeController {

    @GetMapping("/listForm")
    public String noticeListForm() {
        return "masterpage_sys/board/notice/listForm";
    }

    @GetMapping("/saveForm")
    public String noticeSaveForm() {
        return "masterpage_sys/board/notice/saveForm";
    }
}
