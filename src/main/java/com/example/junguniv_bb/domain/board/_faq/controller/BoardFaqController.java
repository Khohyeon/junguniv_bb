package com.example.junguniv_bb.domain.board._faq.controller;

import com.example.junguniv_bb.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/detailForm/{bbsIdx}")
    public String faqDetailForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardDetail(bbsIdx));
        return "masterpage_sys/board/faq/detailForm";
    }

    @GetMapping("/updateForm/{bbsIdx}")
    public String faqUpdateForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardUpdate(bbsIdx));
        return "masterpage_sys/board/faq/updateForm";
    }
}
