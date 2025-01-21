package com.example.junguniv_bb.domain.managerMenu.dto;

import java.util.List;

import com.example.junguniv_bb.domain.managerMenu.model.ManagerMenu;

public record ManagerMenuSaveReqDTO(
    String menuName, // 메뉴명
    Integer sortno, // 정렬값
    String chkUse, // 사용여부
    String url, // URL
    String menuGroup, // 메뉴그룹
    String menuHelp, // 명칭
    String managerGroup, // 관리자구분
    String chkPerson, // 개인정보유무
    String personInfor, // 개인정보항목
    Long parentIdx // 부모메뉴 IDX
) {
    public ManagerMenu toEntity() {
        return ManagerMenu.builder()
            .menuName(menuName)
            .sortno(sortno)
            .chkUse(chkUse)
            .url(url)
            .menuGroup(menuGroup)
            .menuHelp(menuHelp)
            .managerGroup(managerGroup)
            .chkPerson(chkPerson)
            .personInfor(personInfor)
            .build();
    }
}
