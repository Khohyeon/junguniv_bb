package com.example.junguniv_bb.domain.popup.service;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb.domain.popup.dto.*;
import com.example.junguniv_bb.domain.popup.model.Popup;
import com.example.junguniv_bb.domain.popup.model.PopupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class PopupService {

    private final PopupRepository popupRepository;
    private static final String URL_PATTERN = "^(https?://)?([\\w\\-]+\\.)+[\\w\\-]+(/[\\w\\-._~:/?#]@!$&'()*+,;=*)?$";

    /**
     * 메인팝업 리스트 조회
     * 응답 형태 : Page<PopupPageResDTO>
     */
    public Page<PopupPageResDTO> getPopupPage(Pageable pageable) {
        Page<Popup> popupPage = popupRepository.findAll(pageable);

        return popupPage.map(popup ->
                new PopupPageResDTO(
                        popup.getPopupIdx(),
                        popup.getPopupName(),
                        popup.getFormattedUpdatedDate2(),
                        popup.getStartDate(),
                        popup.getEndDate(),
                        popup.getPopupType(),
                        popup.getChkOpen()
                ));
    }

    /**
     * 메인팝업 검색 조회
     * 응답 형태 : Page<PopupSearchReqDTO>
     */
    public Page<PopupSearchResDTO> searchPopupsByName(String popupName, Pageable pageable) {
        Page<Popup> popupPage = popupRepository.findByPopupNameContainingIgnoreCase(popupName, pageable);
        return popupPage.map(popup ->
                new PopupSearchResDTO(
                        popup.getPopupIdx(),
                        popup.getPopupName(),
                        popup.getFormattedUpdatedDate2(),
                        popup.getStartDate(),
                        popup.getEndDate(),
                        popup.getPopupType(),
                        popup.getChkOpen()
                ));
    }


    /**
     * 팝업 상세페이지 조회
     * 응답 타입 : PopupDetailResDTO
     */
    public PopupDetailResDTO getPopupDetail(Long popupIdx) {

        Popup popup = popupRepository.findById(popupIdx)
                .orElseThrow(()-> new Exception400("adas"));

        return new PopupDetailResDTO(
                popupIdx,
                popup.getPopupName(),
                popup.getStartDate(),
                popup.getEndDate(),
                popup.getChkOpen(),
                popup.getChkToday(),
                popup.getChkScrollbar(),
                popup.getWidthSize(),
                popup.getHeightSize(),
                popup.getLeftSize(),
                popup.getTopSize(),
                popup.getContents(),
                popup.getPopupUrl(),
                popup.getBgcolor()
        );
    }

    /**
     * 팝업 등록
     * 요청 타입 : PopupSaveReqDTO
     */
    @Transactional
    public void popupSave(PopupSaveReqDTO popupSaveReqDTO) {

        String popupUrl = popupSaveReqDTO.popupUrl();

        // 프로토콜 추가
        if (popupUrl != null && !popupUrl.startsWith("http://") && !popupUrl.startsWith("https://")) {
            popupUrl = "https://" + popupUrl;

            // URL 유효성 검증
            if (!Pattern.matches(URL_PATTERN, popupUrl)) {
                throw new Exception400("팝업 URL 형식이 올바르지 않습니다.");
            }
        }

        popupRepository.save(popupSaveReqDTO.saveEntity());
    }

    /**
     * 팝업 수정
     * 요청 타입 : PopupUpdateReqDTO
     */
    @Transactional
    public void popupUpdate(PopupUpdateReqDTO popupUpdateReqDTO) {

        String popupUrl = popupUpdateReqDTO.popupUrl();

        // 프로토콜 추가
        if (popupUrl != null && !popupUrl.startsWith("http://") && !popupUrl.startsWith("https://")) {
            popupUrl = "https://" + popupUrl;

            // URL 유효성 검증
            if (!Pattern.matches(URL_PATTERN, popupUrl)) {
                throw new Exception400("팝업 URL 형식이 올바르지 않습니다.");
            }
        }

        popupRepository.save(popupUpdateReqDTO.updateEntity());
    }


    /**
     * 팝업 삭제
     */
    @Transactional
    public void popupDelete(Long popupIdx) {
        popupRepository.deleteById(popupIdx);
    }

    /**
     * 팝업 다중 삭제
     */
    @Transactional
    public void popupListDelete(List<Long> popupIds) {
        List<Popup> popupsToDelete = popupRepository.findAllById(popupIds);
        if (popupsToDelete.isEmpty()) {
            throw new Exception400("삭제할 팝업이 없습니다.");
        }
        popupRepository.deleteAll(popupsToDelete);
    }

}
