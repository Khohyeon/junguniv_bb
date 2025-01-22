package com.example.junguniv_bb.domain.managermenu.dto;

import com.example.junguniv_bb.domain.managermenu._enum.MenuType;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenu;

public record ManagerMenuSaveReqDTO(
        String menuName, // 메뉴명
        Integer sortno, // 정렬값
        String chkUse, // 사용여부
        String url, // URL
        MenuType menuGroup, // 메뉴그룹
        String menuHelp, // 명칭
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
                .chkPerson(chkPerson)
                .personInfor(personInfor)
                .build();
    }
}
