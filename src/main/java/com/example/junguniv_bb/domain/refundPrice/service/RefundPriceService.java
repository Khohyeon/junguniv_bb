package com.example.junguniv_bb.domain.refundPrice.service;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import com.example.junguniv_bb.domain.refundPrice.dto.*;
import com.example.junguniv_bb.domain.refundPrice.model.RefundPrice;
import com.example.junguniv_bb.domain.refundPrice.model.RefundPriceRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                        refundPrice.getChkUse(),
                        refundPrice.getDiscountType(),
                        refundPrice.getSortno()
                ));
    }

    @Transactional
    public void refundPriceSave(RefundPriceSaveReqDTO refundPriceSaveReqDTO) {
        refundPriceRepository.save(refundPriceSaveReqDTO.saveEntity());
    }

    public RefundPriceDetailResDTO refundPriceDetail(Long refundPriceIdx) {
        RefundPrice refundPrice = refundPriceRepository.findById(refundPriceIdx)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_REFUND_PRICE.getMessage()));
        return new RefundPriceDetailResDTO(
                refundPrice.getRefundPriceIdx(),
                refundPrice.getRefundPriceType(),
                refundPrice.getRefundPriceName(),
                refundPrice.getDiscountType(),
                refundPrice.getRefundRate(),
                refundPrice.getChkUse(),
                refundPrice.getSortno()
        );
    }

    @Transactional
    public void refundPriceUpdate(RefundPriceUpdateReqDTO refundPriceUpdateReqDTO) {
        refundPriceRepository.save(refundPriceUpdateReqDTO.updateEntity());
    }
}
