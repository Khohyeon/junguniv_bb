package com.example.junguniv_bb.domain.board.controller;

import com.example.junguniv_bb._core.exception.Exception400;
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
@RequestMapping("/masterpage_sys/board/suggestion")
public class _BoardSuggestionController {

    private final BoardService boardService;
    private String bbsId = "SUGGESTION";


    @GetMapping
    public String suggestionListForm( Model model
//            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        BbsAuthResDTO permissions = boardService.getPermission(bbsId, UserType.GUEST);
        model.addAttribute("permissions", permissions);
        return "masterpage_sys/board/suggestion/listForm";
    }

    @GetMapping("/save")
    public String suggestionSaveForm(Model model) {
        model.addAttribute("board", boardService.getBoardSave(bbsId));

        return "masterpage_sys/board/suggestion/saveForm";
    }

    @GetMapping("/{bbsIdx}")
    public String suggestionDetailForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardDetail(bbsIdx));
        model.addAttribute("comments", boardService.getCommentDetail(bbsIdx));
        model.addAttribute("permissions", boardService.getPermission(bbsId, UserType.GUEST));
        return "masterpage_sys/board/suggestion/detailForm";
    }

    @GetMapping("/update/{bbsIdx}")
    public String suggestionUpdateForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardUpdate(bbsIdx));
        return "masterpage_sys/board/suggestion/updateForm";
    }

    @GetMapping("/reply/{bbsIdx}")
    public String suggestionReplyForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardUpdate(bbsIdx));
        return "masterpage_sys/board/suggestion/replyForm";
    }
}
