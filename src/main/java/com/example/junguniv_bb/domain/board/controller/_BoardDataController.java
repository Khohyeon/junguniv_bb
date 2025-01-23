package com.example.junguniv_bb.domain.board.controller;

import com.example.junguniv_bb.domain.board.service.BoardService;
import com.example.junguniv_bb.domain.member._enum.UserType;
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
    private String bbsId = "MATERIAL";

    @GetMapping
    public String dataListForm(
            Model model) {
        model.addAttribute("permissions", boardService.getPermission(bbsId, UserType.GUEST));

        return "masterpage_sys/board/data/listForm";
    }

    @GetMapping("/save")
    public String dataSaveForm(Model model) {
        model.addAttribute("board", boardService.getBoardSave(bbsId));
        return "masterpage_sys/board/data/saveForm";
    }

    @GetMapping("/{bbsIdx}")
    public String dataDetailForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardDetail(bbsIdx));
        model.addAttribute("comments", boardService.getCommentDetail(bbsIdx));
        model.addAttribute("permissions", boardService.getPermission(bbsId, UserType.GUEST));
        return "masterpage_sys/board/data/detailForm";
    }

    @GetMapping("/update/{bbsIdx}")
    public String dataUpdateForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardUpdate(bbsIdx));
        return "masterpage_sys/board/data/updateForm";
    }

    @GetMapping("/reply/{bbsIdx}")
    public String dataReplyForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardUpdate(bbsIdx));
        return "masterpage_sys/board/data/replyForm";
    }
}
