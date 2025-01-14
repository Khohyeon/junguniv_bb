package com.example.junguniv_bb.domain.popup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("masterpage_sys/popup")
public class PopupController {

    @GetMapping("/listForm")
    public String popupListForm() {
        return "/masterpage_sys/popup/listForm";
    }

    @GetMapping("/registForm")
    public String popupRegistForm() {return "/masterpage_sys/popup/registForm";}

}
