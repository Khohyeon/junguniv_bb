package com.example.junguniv_bb.masterpage.popup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/masterpage")
public class PopupController {



    @GetMapping("/mainpopup")
    public String mainpopup() {
        return "masterpage/popup/mainpopup";
    }


}
