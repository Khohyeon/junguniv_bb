package com.example.junguniv_bb.masterpage.sys;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SysController {

    @GetMapping
    public String index(Model model) {
        model.addAttribute("title", "Home Page");
        model.addAttribute("name", "World");
        return "index";
    }

}
