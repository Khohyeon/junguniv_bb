package com.example.junguniv_bb.domain.agreement.service;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import com.example.junguniv_bb.domain.agreement.dto.AgreementDetailResDTO;
import com.example.junguniv_bb.domain.agreement.dto.AgreementPageResDTO;
import com.example.junguniv_bb.domain.agreement.dto.AgreementUpdateReqDTO;
import com.example.junguniv_bb.domain.agreement.model.Agreement;
import com.example.junguniv_bb.domain.agreement.model.AgreementRepository;
import com.example.junguniv_bb.domain.popup.dto.PopupDetailResDTO;
import com.example.junguniv_bb.domain.popup.dto.PopupPageResDTO;
import com.example.junguniv_bb.domain.popup.model.Popup;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgreementService {

    private final AgreementRepository agreementRepository;

    /**
     * 회원가입약관 리스트 조회
     * 응답 형태 : Page<AgreementPageResDTO>
     */
    public Page<AgreementPageResDTO> getAgreementPage(Pageable pageable) {
        Page<Agreement> agreementPage = agreementRepository.findAll(pageable);

        return agreementPage.map(agreement ->
                new AgreementPageResDTO(
                        agreement.getAgreementIdx(),
                        agreement.getAgreementTitle(),
                        agreement.getAgreementContents(),
                        agreement.getOpenYn()
                ));
    }

    /**
     * 회원가입약관 상세페이지 조회
     * 응답 타입 : PopupDetailResDTO
     */
    public AgreementDetailResDTO getAgreementDetail(Long agreementIdx) {

        Agreement agreement = agreementRepository.findById(agreementIdx)
                .orElseThrow(()-> new Exception400(ExceptionMessage.NOT_FOUND_AGREEMENT.getMessage()));

        return new AgreementDetailResDTO(
                agreementIdx,
                agreement.getAgreementTitle(),
                agreement.getAgreementContents()
        );
    }

    /**
     * 회원가입약관 수정
     * 요청 타입 : AgreementUpdateReqDTO
     */
    public void agreementUpdate(AgreementUpdateReqDTO agreementUpdateReqDTO) {
            agreementRepository.save(agreementUpdateReqDTO.updateJoinEntity());
    }
}
