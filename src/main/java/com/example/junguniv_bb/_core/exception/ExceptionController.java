package com.example.junguniv_bb._core.exception;

import com.example.junguniv_bb._core.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExceptionController {

    @GetMapping("/access-denied")
    public String accessDenied(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails != null) {
            model.addAttribute("errorMessage", "접근이 거부되었습니다.");
            model.addAttribute("userType", customUserDetails.getMember()
                    .getUserType());
        } else {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
        }
        return "common/_error/error403";
    }

    @GetMapping("/id-stop")
    public String idStop(Model model) {
        model.addAttribute("errorMessage", "계정이 정지되었습니다.\n관리자에게 문의하세요.");
        return "common/_error/error401";
    }
}
