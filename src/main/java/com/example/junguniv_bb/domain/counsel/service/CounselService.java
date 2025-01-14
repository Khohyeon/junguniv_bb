package com.example.junguniv_bb.domain.counsel.service;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb.domain.counsel.dto.CounselDetailResDTO;
import com.example.junguniv_bb.domain.counsel.dto.CounselPageResDTO;
import com.example.junguniv_bb.domain.counsel.dto.CounselSaveReqDTO;
import com.example.junguniv_bb.domain.counsel.model.Counsel;
import com.example.junguniv_bb.domain.counsel.model.CounselRepository;
import com.example.junguniv_bb.domain.popup.dto.PopupDetailResDTO;
import com.example.junguniv_bb.domain.popup.dto.PopupSaveReqDTO;
import com.example.junguniv_bb.domain.popup.model.Popup;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CounselService {

    private final CounselRepository counselRepository;


    /**
     * 문의상담 리스트 조회
     * 응답 형태 : Page<CounselPageResDTO>
     */
    public Page<CounselPageResDTO> getCounselPage(Pageable pageable) {

        Page<Counsel> counselPage = counselRepository.findAll(pageable);

        return counselPage.map(counsel ->
                new CounselPageResDTO(
                        counsel.getCounselIdx(),
                        counsel.getMemo(),
                        counsel.getTalkTime(),
                        counsel.getName(),
                        counsel.getCounselState(),
                        counsel.getFormattedUpdatedDate2()
                ));
    }

    /**
     * 문의상담 상세페이지 조회
     * 응답 타입 : PopupDetailResDTO
     */
    public CounselDetailResDTO counselDetail(Long counselIdx) {
        Counsel counsel = counselRepository.findById(counselIdx)
                .orElseThrow(()-> new Exception400("adas"));

        return new CounselDetailResDTO(
                counselIdx,
                counsel.getCounselName(),
                counsel.getName(),
                counsel.getTalkTime(),
                counsel.getDegreeHope(),
                counsel.getAddr1(),
                counsel.getAddr2(),
                counsel.getZipcode(),
                counsel.getTelMobile(),
                counsel.getEmail(),
                counsel.getFinalEducationType(),
                counsel.getPwd(),
                counsel.getMemo(),
                counsel.getLicense(),
                counsel.getFname1(),
                counsel.getFname2(),
                counsel.getFname3(),
                counsel.getFname1Name(),
                counsel.getFname2Name(),
                counsel.getFname3Name(),
                counsel.getAnswerMemo(),
                counsel.getCounselState()
        );
    }


    /**
     * 문의상담 예약
     * 요청 타입 : counselSave
     */
    @Transactional
    public void counselSave(CounselSaveReqDTO counselSaveReqDTO) {
        counselRepository.save(counselSaveReqDTO.saveEntity());
    }

}
