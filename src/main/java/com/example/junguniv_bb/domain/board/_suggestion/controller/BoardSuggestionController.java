package com.example.junguniv_bb.domain.board._suggestion.controller;

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
public class BoardSuggestionController {

    private final BoardService boardService;


    @GetMapping("/listForm")
    public String suggestionListForm() {
        return "masterpage_sys/board/suggestion/listForm";
    }

    @GetMapping("/saveForm")
    public String suggestionSaveForm() {
        return "masterpage_sys/board/suggestion/saveForm";
    }

    @GetMapping("/detailForm/{bbsIdx}")
    public String suggestionDetailForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardDetail(bbsIdx));
        return "masterpage_sys/board/suggestion/detailForm";
    }

    @GetMapping("/updateForm/{bbsIdx}")
    public String suggestionUpdateForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardUpdate(bbsIdx));
        return "masterpage_sys/board/suggestion/updateForm";
    }
}
