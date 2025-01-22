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
@RequestMapping("/masterpage_sys/board/suggestion")
public class _BoardSuggestionController {

    private final BoardService boardService;


    @GetMapping
    public String suggestionListForm() {
        return "masterpage_sys/board/suggestion/listForm";
    }

    @GetMapping("/save")
    public String suggestionSaveForm(Model model) {
        String bbsId = "SUGGESTION";
        model.addAttribute("fileCount", boardService.getFileCount(bbsId));
        return "masterpage_sys/board/suggestion/saveForm";
    }

    @GetMapping("/{bbsIdx}")
    public String suggestionDetailForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardDetail(bbsIdx));
        return "masterpage_sys/board/suggestion/detailForm";
    }

    @GetMapping("/update/{bbsIdx}")
    public String suggestionUpdateForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardUpdate(bbsIdx));
        return "masterpage_sys/board/suggestion/updateForm";
    }
}
