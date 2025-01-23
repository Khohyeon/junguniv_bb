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
@RequestMapping("/masterpage_sys/board/qna")
public class _BoardQnaController {

    private final BoardService boardService;


    @GetMapping
    public String qnaListForm() {
        return "masterpage_sys/board/qna/listForm";
    }

    @GetMapping("/save")
    public String qnaSaveForm(Model model) {
        String bbsId = "Q&A";
        model.addAttribute("board", boardService.getBoardSave(bbsId));
        return "masterpage_sys/board/qna/saveForm";
    }

    @GetMapping("/{bbsIdx}")
    public String qnaDetailForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardDetail(bbsIdx));
        model.addAttribute("comments", boardService.getCommentDetail(bbsIdx));
        return "masterpage_sys/board/qna/detailForm";
    }

    @GetMapping("/update/{bbsIdx}")
    public String qnaUpdateForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("comments", boardService.getCommentDetail(bbsIdx));
        model.addAttribute("board", boardService.getBoardUpdate(bbsIdx));
        return "masterpage_sys/board/qna/updateForm";
    }

    @GetMapping("/reply/{bbsIdx}")
    public String qnaReplyForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardUpdate(bbsIdx));
        return "masterpage_sys/board/qna/replyForm";
    }
}
