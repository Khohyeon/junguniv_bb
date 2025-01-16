package com.example.junguniv_bb.domain.board._notice.controller;

import com.example.junguniv_bb.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/board/notice")
public class BoardNoticeController {

    private final BoardService boardService;

    @GetMapping("/listForm")
    public String noticeListForm() {
        return "masterpage_sys/board/notice/listForm";
    }

    @GetMapping("/saveForm")
    public String noticeSaveForm() {
        return "masterpage_sys/board/notice/saveForm";
    }

    @GetMapping("/detailForm/{bbsIdx}")
    public String noticeDetailForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardDetail(bbsIdx));
        return "masterpage_sys/board/notice/detailForm";
    }

    @GetMapping("/updateForm/{bbsIdx}")
    public String noticeUpdateForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardUpdate(bbsIdx));
        return "masterpage_sys/board/notice/updateForm";
    }
}
