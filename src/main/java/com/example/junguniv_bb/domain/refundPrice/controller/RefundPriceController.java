package com.example.junguniv_bb.domain.refundPrice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/masterpage_sys/refund_price")
@RequiredArgsConstructor
public class RefundPriceController {

    @GetMapping("/refund")
    public String refundPrice() {
        return "/masterpage_sys/refund_price/refund/listForm";
    }
}
