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
@RequestMapping("/masterpage_sys/board/data")
public class _BoardDataController {

    private final BoardService boardService;

    @GetMapping("/listForm")
    public String dataListForm() {
        return "masterpage_sys/board/data/listForm";
    }

    @GetMapping("/saveForm")
    public String dataSaveForm() {
        return "masterpage_sys/board/data/saveForm";
    }

    @GetMapping("/detailForm/{bbsIdx}")
    public String dataDetailForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardDetail(bbsIdx));
        return "masterpage_sys/board/data/detailForm";
    }

    @GetMapping("/updateForm/{bbsIdx}")
    public String dataUpdateForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardUpdate(bbsIdx));
        return "masterpage_sys/board/data/updateForm";
    }
}
