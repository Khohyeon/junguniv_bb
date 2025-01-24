package com.example.junguniv_bb.domain.managermenu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/masterpage_sys/manager_menu")
@RequiredArgsConstructor
public class ManagerMenuController {

    @GetMapping
    public String managerMenu() {
        return "masterpage_sys/manager_menu/listForm";
    }
}
