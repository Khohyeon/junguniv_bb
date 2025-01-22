package com.example.junguniv_bb.domain.board.controller;

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
public class _BoardNoticeController {

    private final BoardService boardService;

    @GetMapping
    public String noticeListForm() {
        return "masterpage_sys/board/notice/listForm";
    }

    @GetMapping("/save")
    public String noticeSaveForm(Model model) {
        String bbsId = "NOTICE";
        model.addAttribute("board", boardService.getBoardSave(bbsId));
        return "masterpage_sys/board/notice/saveForm";
    }

    @GetMapping("/{bbsIdx}")
    public String noticeDetailForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardDetail(bbsIdx));
        return "masterpage_sys/board/notice/detailForm";
    }

    @GetMapping("/update/{bbsIdx}")
    public String noticeUpdateForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardUpdate(bbsIdx));
        return "masterpage_sys/board/notice/updateForm";
    }
}
