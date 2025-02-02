package com.example.junguniv_bb.system.settings.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.junguniv_bb.domain.systemcode.service.SystemCodeService;

@Controller
@RequestMapping("/masterpage_sys/mark/refund")
@RequiredArgsConstructor
@Slf4j
public class MarkRefundController {

    /* DI */
    private final SystemCodeService systemCodeService;

    /* 시스템설정 > 기본표시정보 > 환급교육 페이지 */
    @GetMapping
    public String markedRefundForm() {

        return "masterpage_sys/system_code/markRefundForm";
    }
}
