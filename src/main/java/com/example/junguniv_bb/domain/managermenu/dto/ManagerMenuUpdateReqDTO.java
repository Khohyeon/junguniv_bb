package com.example.junguniv_bb.domain.managermenu.dto;

import com.example.junguniv_bb.domain.managermenu.model.ManagerMenu;

public record ManagerMenuUpdateReqDTO(
    String menuName, // 메뉴명
    Integer sortno, // 정렬값
    String chkUse, // 사용여부
    String url, // URL
    String menuGroup, // 메뉴그룹
    String menuHelp, // 명칭
    String chkPerson, // 개인정보유무
    String personInfor, // 개인정보항목
    Long parentIdx // 부모메뉴 IDX
) {
    public void updateEntity(ManagerMenu managerMenuPS, ManagerMenu parentMenu) {
        if (menuName != null)
            managerMenuPS.setMenuName(menuName);
        if (sortno != null)
            managerMenuPS.setSortno(sortno);
        if (chkUse != null)
            managerMenuPS.setChkUse(chkUse);
        if (url != null)
            managerMenuPS.setUrl(url);
        if (menuGroup != null)
            managerMenuPS.setMenuGroup(menuGroup);
        if (menuHelp != null)
            managerMenuPS.setMenuHelp(menuHelp);
        if (chkPerson != null)
            managerMenuPS.setChkPerson(chkPerson);
        if (personInfor != null)
            managerMenuPS.setPersonInfor(personInfor);
        if (parentIdx != null) {
            managerMenuPS.setParent(parentMenu);
        }
    }
}
