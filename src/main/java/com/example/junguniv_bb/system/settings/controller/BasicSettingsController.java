package com.example.junguniv_bb.system.settings.controller;

import com.example.junguniv_bb.domain.systemcode.service.SystemCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 시스템설정 > 기본설정 메뉴 웹 페이지 컨트롤러
 */
@Controller
@RequestMapping("/masterpage_sys/settings/basic")
@RequiredArgsConstructor
@Slf4j
public class BasicSettingsController {

    /* DI */
    private final SystemCodeService systemCodeService;


    /* 시스템설정 > 기본설정 페이지 */
    @GetMapping
    public String basicSettingsForm(Model model) {

        return "masterpage_sys/system_code/basicSettingsForm";
    }

}
