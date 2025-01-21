package com.example.junguniv_bb.domain.board.controller;

import com.example.junguniv_bb.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/masterpage_sys/board/consulting")
public class _BoardConsultingController {

    private final BoardService boardService;

    @GetMapping
    public String consultingListForm() {
        return "masterpage_sys/board/consulting/listForm";
    }

    @GetMapping("/save")
    public String consultingSaveForm() {
        return "masterpage_sys/board/consulting/saveForm";
    }

    @GetMapping("/{bbsIdx}")
    public String consultingDetailForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardDetail(bbsIdx));
        return "masterpage_sys/board/consulting/detailForm";
    }

    @GetMapping("/update/{bbsIdx}")
    public String consultingUpdateForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardUpdate(bbsIdx));
        return "masterpage_sys/board/consulting/updateForm";
    }
}
