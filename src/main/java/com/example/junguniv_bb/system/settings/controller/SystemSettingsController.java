package com.example.junguniv_bb.system.settings.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/masterpage_sys/settings")
@RequiredArgsConstructor
@Slf4j
public class SystemSettingsController {

    @GetMapping("/basic")
    public String settingsController() {
        return "masterpage_sys/settings/basic";
    }
}
