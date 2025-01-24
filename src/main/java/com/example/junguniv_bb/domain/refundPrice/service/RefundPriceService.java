package com.example.junguniv_bb.domain.refundPrice.service;

import com.example.junguniv_bb.domain.managermenu.dto.ManagerMenuSearchResDTO;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenu;
import com.example.junguniv_bb.domain.refundPrice.dto.RefundPriceSearchReqDTO;
import com.example.junguniv_bb.domain.refundPrice.dto.RefundPriceSearchResDTO;
import com.example.junguniv_bb.domain.refundPrice.model.RefundPrice;
import com.example.junguniv_bb.domain.refundPrice.model.RefundPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RefundPriceService {
    private final RefundPriceRepository refundPriceRepository;

    public Page<RefundPriceSearchResDTO> refundPriceSearchPage(RefundPriceSearchReqDTO refundPriceSearchReqDTO, Pageable pageable) {

        Page<RefundPrice> refundPricePage = null;

        if (Objects.equals(refundPriceSearchReqDTO.refundPriceType(), "ALL")) {
            // 전체 조회
            refundPricePage = refundPriceRepository.findByRefundPriceNameContainingIgnoreCase(refundPriceSearchReqDTO.refundPriceName(), pageable);
        } else {
            refundPricePage = refundPriceRepository.findByRefundPriceNameContainingIgnoreCaseAndRefundPriceType(
                    refundPriceSearchReqDTO.refundPriceName(), refundPriceSearchReqDTO.refundPriceType(), pageable);
        }

        return refundPricePage.map(refundPrice ->
                new RefundPriceSearchResDTO(
                        refundPrice.getRefundPriceIdx(),
                        refundPrice.getRefundPriceType(),
                        refundPrice.getRefundPriceName(),
                        refundPrice.getRefundRate(),
                        refundPrice.getChkUse()
                ));
    }
}
