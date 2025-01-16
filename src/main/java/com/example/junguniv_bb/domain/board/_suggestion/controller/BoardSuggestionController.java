package com.example.junguniv_bb.domain.board._suggestion.controller;

import com.example.junguniv_bb.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
}
