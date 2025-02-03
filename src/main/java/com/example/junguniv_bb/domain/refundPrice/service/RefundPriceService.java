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

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RefundPriceService {
    private final RefundPriceRepository refundPriceRepository;

    public Page<RefundPriceSearchResDTO> refundPriceSearchPage(RefundPriceSearchReqDTO refundPriceSearchReqDTO, Pageable pageable) {
        Page<RefundPrice> refundPricePage;

        // studyType을 기준으로 분기
        String studyType = refundPriceSearchReqDTO.studyType();
        String refundPriceName = refundPriceSearchReqDTO.refundPriceName();
        String refundPriceType = refundPriceSearchReqDTO.refundPriceType();

        if (studyType == null) {
            studyType = "default";  // 기본값 설정, 필요시 수정
        }

        // 기본 쿼리 조건 생성
        if ("ALL".equals(refundPriceType)) {
            refundPricePage = refundPriceRepository.findByStudyTypeAndRefundPriceNameContainingIgnoreCase(studyType, refundPriceName, pageable);
        } else {
            refundPricePage = refundPriceRepository.findByStudyTypeAndRefundPriceNameContainingIgnoreCaseAndRefundPriceType(
                    studyType, refundPriceName, refundPriceType, pageable);
        }

        // 결과 매핑
        return refundPricePage.map(refundPrice ->
                new RefundPriceSearchResDTO(
                        refundPrice.getRefundPriceIdx(),
                        refundPrice.getRefundPriceType(),
                        refundPrice.getRefundPriceName(),
                        refundPrice.getRefundRate(),
                        refundPrice.getChkUse(),
                        refundPrice.getDiscountType(),
                        refundPrice.getSortno(),
                        refundPrice.getStudyType()
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

    public List<RefundPrice> refundPriceList() {
        return refundPriceRepository.findAll();
    }
}
