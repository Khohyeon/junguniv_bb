package com.example.junguniv_bb.domain.refundPrice.controller;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.refundPrice.dto.RefundPriceSaveReqDTO;
import com.example.junguniv_bb.domain.refundPrice.dto.RefundPriceSearchReqDTO;
import com.example.junguniv_bb.domain.refundPrice.dto.RefundPriceSearchResDTO;
import com.example.junguniv_bb.domain.refundPrice.dto.RefundPriceUpdateReqDTO;
import com.example.junguniv_bb.domain.refundPrice.service.RefundPriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/masterpage_sys/refund_price/api")
public class RefundPriceRestController {

    private final RefundPriceService refundPriceService;

    @PostMapping("/search")
    public ResponseEntity<?> refundPRiceSearch(
            @RequestBody RefundPriceSearchReqDTO refundPriceSearchReqDTO,
            Pageable pageable
    ) {

        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "refundPriceIdx"));

        Page<RefundPriceSearchResDTO> refundPriceSearchResDTOPage = refundPriceService.refundPriceSearchPage(refundPriceSearchReqDTO, pageable);

        return ResponseEntity.ok(APIUtils.success(refundPriceSearchResDTOPage));
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveRefundPrice(
            @Valid @ModelAttribute RefundPriceSaveReqDTO refundPriceSaveReqDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new Exception400(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        refundPriceService.refundPriceSave(refundPriceSaveReqDTO);
        return ResponseEntity.ok(APIUtils.success("지원금종류 등록이 완료 되었습니다."));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateRefundPrice(
            @Valid @ModelAttribute RefundPriceUpdateReqDTO refundPriceUpdateReqDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new Exception400(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        refundPriceService.refundPriceUpdate(refundPriceUpdateReqDTO);
        return ResponseEntity.ok(APIUtils.success("지원금종류 수정이 완료 되었습니다."));
    }

}
