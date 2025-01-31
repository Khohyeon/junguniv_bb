package com.example.junguniv_bb.domain.refundPrice.controller;

import com.example.junguniv_bb.domain.refundPrice.service.RefundPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/masterpage_sys/refund_price")
@RequiredArgsConstructor
public class RefundPriceController {

    private final RefundPriceService refundPriceService;

    @GetMapping("/refund")
    public String refundPrice() {
        return "/masterpage_sys/refund_price/refund/listForm";
    }

    @GetMapping("/refund/save")
    public String saveRefundPrice() {
        return "/masterpage_sys/refund_price/refund/saveForm";
    }

    @GetMapping("/refund/{refundPriceIdx}")
    public String saveRefundPrice(@PathVariable Long refundPriceIdx, Model model) {
        model.addAttribute("refundDetail", refundPriceService.refundPriceDetail(refundPriceIdx));
        return "/masterpage_sys/refund_price/refund/detailForm";
    }
}
