package com.example.junguniv_bb.domain.board.controller;

import com.example.junguniv_bb._core.aop.CheckAuth;
import com.example.junguniv_bb.domain.board.dto.BbsAuthResDTO;
import com.example.junguniv_bb.domain.board.service.BbsAuthService;
import com.example.junguniv_bb.domain.board.service.BoardService;
import com.example.junguniv_bb.domain.member._enum.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/board/notice")
public class _BoardNoticeController {

    private final BoardService boardService;
    private String bbsId = "NOTICE";


    @GetMapping
    public String noticeListForm(Model model) {
        BbsAuthResDTO permissions = boardService.getPermission(bbsId, UserType.GUEST);
        model.addAttribute("permissions", permissions);

        return "masterpage_sys/board/notice/listForm";
    }

    @GetMapping("/save")
    @CheckAuth("WRITE")
    public String noticeSaveForm(Model model) {
        model.addAttribute("board", boardService.getBoardSave(bbsId));
        return "masterpage_sys/board/notice/saveForm";
    }

    @GetMapping("/{bbsIdx}")
    public String noticeDetailForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardDetail(bbsIdx));
        model.addAttribute("comments", boardService.getCommentDetail(bbsIdx));
        model.addAttribute("permissions", boardService.getPermission(bbsId, UserType.GUEST));
        return "masterpage_sys/board/notice/detailForm";
    }

    @GetMapping("/update/{bbsIdx}")
    public String noticeUpdateForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardUpdate(bbsIdx));
        return "masterpage_sys/board/notice/updateForm";
    }

    @GetMapping("/reply/{bbsIdx}")
    public String noticeReplyForm(@PathVariable Long bbsIdx, Model model) {
        model.addAttribute("board", boardService.getBoardUpdate(bbsIdx));
        return "masterpage_sys/board/notice/replyForm";
    }
}
