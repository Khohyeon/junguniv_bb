package com.example.junguniv_bb.domain.agreement.service;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import com.example.junguniv_bb.domain.agreement.dto.*;
import com.example.junguniv_bb.domain.agreement.model.Agreement;
import com.example.junguniv_bb.domain.agreement.model.AgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgreementService {

    private final AgreementRepository agreementRepository;

    /**
     * 회원가입약관 리스트 조회
     * 응답 형태 : List<AgreementJoinListResDTO>
     */
    public List<AgreementJoinListResDTO> getAgreementJoinList() {
        List<Agreement> agreementList = agreementRepository.findAllByAgreementType("JOIN");

        return agreementList.stream()
                .map(agreement -> new AgreementJoinListResDTO(
                        agreement.getAgreementIdx(),
                        agreement.getTrainingCenterName(),
                        agreement.getTrainingCenterUrl(),
                        agreement.getAgreementTitle(),
                        agreement.getAgreementContents(),
                        agreement.getOpenYn()
                ))
                .toList();
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
    @Transactional
    public void agreementUpdate(AgreementJoinUpdateReqDTO agreementJoinUpdateReqDTO) {
            agreementRepository.save(agreementJoinUpdateReqDTO.updateJoinEntity());
    }

    public List<AgreementListResDTO> getAgreementCourseList() {
        List<Agreement> agreementList = agreementRepository.findAllByAgreementType("COURSE");
        if (agreementList.isEmpty()) {
            return new ArrayList<>();
        }
        return agreementList.stream()
                .map(agreement -> new AgreementListResDTO(
                        agreement.getAgreementIdx(),
                        agreement.getAgreementContents(),
                        agreement.getOpenYn()
                ))
                .toList();
    }

    public List<AgreementListResDTO> getAgreementRefundList() {
        List<Agreement> agreementList = agreementRepository.findAllByAgreementType("REFUND");
        if (agreementList.isEmpty()) {
            return new ArrayList<>();
        }
        return agreementList.stream()
                .map(agreement -> new AgreementListResDTO(
                        agreement.getAgreementIdx(),
                        agreement.getAgreementContents(),
                        agreement.getOpenYn()
                ))
                .toList();
    }

    @Transactional
    public void agreementUpdate2(List<AgreementUpdateReqDTO> agreementUpdateReqDTOList) {
        for (AgreementUpdateReqDTO agreementUpdateReqDTO : agreementUpdateReqDTOList) {
            if (agreementUpdateReqDTO.agreementIdx() != null) {
                agreementRepository.save(agreementUpdateReqDTO.updateEntity());
            } else {
                agreementRepository.save(agreementUpdateReqDTO.saveEntity());
            }
        }
    }

    @Transactional
    public void deleteAgreement(Long agreementIdx) {
        agreementRepository.deleteById(agreementIdx);
    }
}
