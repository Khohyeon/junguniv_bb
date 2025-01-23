package com.example.junguniv_bb.domain.board.controller;

import com.example.junguniv_bb._core.aop.CheckAuth;
import com.example.junguniv_bb.domain.board.dto.BbsAuthResDTO;
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
@RequestMapping("/masterpage_sys/board/faq")
public class _BoardFaqController {

    private final BoardService boardService;
    private String bbsId = "FAQ";

    @GetMapping
    public String faqListForm(
            Model model) {
        model.addAttribute("permissions", boardService.getPermission(bbsId, UserType.GUEST));
        return "masterpage_sys/board/faq/listForm";
    }

    @GetMapping("/save")
    public String faqSaveForm(Model model) {
        model.addAttribute("board", boardService.getBoardSave(bbsId));
        return "masterpage_sys/board/faq/saveForm";
    }

    @GetMapping("/{bbsIdx}")
    public String faqDetailForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardDetail(bbsIdx));
        model.addAttribute("comments", boardService.getCommentDetail(bbsIdx));
        model.addAttribute("permissions", boardService.getPermission(bbsId, UserType.GUEST));
        return "masterpage_sys/board/faq/detailForm";
    }

    @GetMapping("/update/{bbsIdx}")
    public String faqUpdateForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardUpdate(bbsIdx));
        return "masterpage_sys/board/faq/updateForm";
    }

    @GetMapping("/reply/{bbsIdx}")
    public String faqReplyForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardUpdate(bbsIdx));
        return "masterpage_sys/board/faq/replyForm";
    }
}
