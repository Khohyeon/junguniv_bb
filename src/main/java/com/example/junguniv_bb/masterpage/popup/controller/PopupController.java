package com.example.junguniv_bb.masterpage.popup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("masterpage/popup")
public class PopupController {

    @GetMapping("/listForm")
    public String popupListForm() {
        return "/masterpage/popup/listForm";
    }

    @GetMapping("/registForm")
    public String popupRegistForm() {return "/masterpage/popup/registForm";}

}
