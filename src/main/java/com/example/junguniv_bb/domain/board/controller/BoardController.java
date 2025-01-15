package com.example.junguniv_bb.domain.board.controller;

import com.example.junguniv_bb.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/managerForm")
    public String managerForm(Pageable pageable, Model model) {

        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, ("bbsGroupIdx"));

        model.addAttribute("bbsGroupPage", boardService.getBbsGroupPage(pageable));

        return "masterpage_sys/board/managerForm";
    }

    @GetMapping("/notice/listForm")
    public String noticeListForm() {
        return "masterpage_sys/board/notice/listForm";
    }

    @GetMapping("/notice/saveForm")
    public String noticeSaveForm() {
        return "masterpage_sys/board/notice/saveForm";
    }

    @GetMapping("/suggestion/listForm")
    public String suggestionForm() {
        return "masterpage_sys/board/suggestion/listForm";
    }

    @GetMapping("/faq/listForm")
    public String faqForm() {
        return "masterpage_sys/board/faq/listForm";
    }
    @GetMapping("/qna/listForm")
    public String qnaForm() {
        return "masterpage_sys/board/qna/listForm";
    }

    @GetMapping("/data/listForm")
    public String dataForm() {
        return "masterpage_sys/board/dataForm";
    }

    @GetMapping("/consulting/listForm")
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

    @GetMapping("/head/listForm")
    public String courseForm() {
        return "masterpage_sys/board/headForm";
    }
}
