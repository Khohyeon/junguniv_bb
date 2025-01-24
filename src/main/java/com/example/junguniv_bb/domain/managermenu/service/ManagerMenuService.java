package com.example.junguniv_bb.domain.managermenu.service;


import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import com.example.junguniv_bb._core.permission.ManagerMenuChangeEvent;

import com.example.junguniv_bb._core.security.CustomUserDetails;
import com.example.junguniv_bb.domain.managerauth.model.ManagerAuthRepository;
import com.example.junguniv_bb.domain.managerauth.service.ManagerAuthService;

import com.example.junguniv_bb.domain.counsel.dto.CounselSearchResDTO;
import com.example.junguniv_bb.domain.counsel.model.Counsel;
import com.example.junguniv_bb.domain.managerauth.model.ManagerAuthRepository;
import com.example.junguniv_bb.domain.managermenu._branch.dto.Depth1MenuSaveReqDTO;
import com.example.junguniv_bb.domain.managermenu._branch.dto.Depth2MenuSaveReqDTO;

import com.example.junguniv_bb.domain.managermenu._enum.MenuType;
import com.example.junguniv_bb.domain.member._enum.UserType;
import com.example.junguniv_bb.domain.managermenu.dto.*;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenu;
import com.example.junguniv_bb.domain.managermenu.model.ManagerMenuRepository;
import com.example.junguniv_bb.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ManagerMenuService {

    /* DI */
    private final ManagerMenuRepository managerMenuRepository;
    private final ManagerAuthRepository managerAuthRepository;
    private final ApplicationEventPublisher eventPublisher; // 이벤트 발행을 위한 DI
    private final ManagerAuthService managerAuthService;


    /* ManagerMenu 하위 메뉴 조회 */
    public List<ManagerMenuDepth3ListResDTO> getDepth3Menus(Long parentMenuIdx) {
        List<ManagerMenu> childMenus = managerMenuRepository.findByParent_MenuIdxAndChkUseOrderBySortno(parentMenuIdx, "Y");

        return childMenus.stream()
                .map(menu -> new ManagerMenuDepth3ListResDTO(
                        menu.getMenuName(),
                        menu.getUrl() != null ? menu.getUrl() : "#",
                        "_self",
                        true // 기본적으로 비활성화
                ))
                .toList();
    }

    /* ManagerMenu 하위 메뉴 조회 (권한 체크 포함) */
    public List<ManagerMenuDepth3ListResDTO> getDepth3MenusWithPermission(Long parentMenuIdx, CustomUserDetails userDetails) {
        List<ManagerMenu> childMenus = managerMenuRepository.findByParent_MenuIdxAndChkUseOrderBySortno(parentMenuIdx, "Y");

        return childMenus.stream()
                .filter(menu -> {
                    try {
                        // 현재 인증된 사용자의 권한 정보 가져오기
                        if (userDetails == null) {
                            return false;
                        }

                        // URL이 없는 메뉴는 제외
                        if (menu.getUrl() == null || menu.getUrl().isEmpty() || menu.getUrl().equals("#")) {
                            return false;
                        }

                        // 사용자 유형과 권한 레벨 확인
                        Member member = userDetails.getMember();
                        Long authLevel = userDetails.getAuthLevel();

                        // 관리자의 경우 authLevel로 권한 체크
                        if (member.getUserType() == UserType.ADMIN) {
                            if (authLevel == null) {
                                return false;
                            }
                            // 메뉴에 대한 읽기 권한이 있는지 확인
                            return managerAuthRepository.existsByMenuIdxAndAuthLevel(menu.getMenuIdx(), authLevel) &&
                                   managerAuthService.hasPermission(menu.getMenuIdx(), authLevel, "READ");
                        }
                        
                        // 학생, 강사, 기업 회원의 경우 메뉴의 MenuGroup과 UserType 매칭
                        MenuType menuGroup = menu.getMenuGroup();
                        if (menuGroup == null) {
                            return false;
                        }

                        return switch (member.getUserType()) {
                            case STUDENT -> menuGroup == MenuType.STUDENT;
                            case TEACHER -> menuGroup == MenuType.TEACHER;
                            case COMPANY -> menuGroup == MenuType.COMPANY;
                            default -> false;
                        };
                    } catch (Exception e) {
                        log.error("권한 체크 중 오류 발생: {}", e.getMessage());
                        return false;
                    }
                })
                .map(menu -> new ManagerMenuDepth3ListResDTO(
                        menu.getMenuName(),
                        menu.getUrl(),
                        "_self",
                        true
                ))
                .toList();
    }

    /* ManagerMenu 전체 조회 */
    public List<ManagerMenu> findAll() {
        return managerMenuRepository.findAll();
    }

    /* ManagerMenu 다중 삭제 */
    @Transactional
    public void managerMenuDeleteMultiple(List<Long> ids) {
        // 1. 메뉴 조회 (자식 메뉴 함께 조회)
        List<ManagerMenu> menus = ids.stream()
            .map(id -> managerMenuRepository.findByIdWithParentAndChildren(id)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_MANAGER_MENU.getMessage())))
            .toList();

        // 2. 자식 메뉴 존재 여부 확인
        for (ManagerMenu menu : menus) {
            if (!menu.getChildren().isEmpty()) {
                throw new Exception400(
                    String.format("메뉴 '%s'(ID: %d)에 하위 메뉴가 존재합니다. 하위 메뉴를 먼저 삭제해주세요.",
                        menu.getMenuName(), menu.getMenuIdx())
                );
            }
        }

        // 관련 ManagerAuth 삭제
        for (ManagerMenu menu : menus) {
            managerAuthRepository.deleteAllByMenuIdx(menu.getMenuIdx());
        }

        // 3. 삭제 처리
        managerMenuRepository.deleteAll(menus);

        // 4. 이벤트 발행
        eventPublisher.publishEvent(new ManagerMenuChangeEvent(this));
    }



    /* ManagerMenu 페이지 */
    public ManagerMenuPageResDTO managerMenuPage(Member member, Pageable pageable, String menuName, String menuType) {
        // 1. 기본 메뉴 조회 (fetch join으로 N+1 문제 방지)
        Page<ManagerMenu> managerMenuPage;
        
        if (menuName != null) {
            managerMenuPage = managerMenuRepository.findByMenuNameContainingIgnoreCaseWithParentAndChildren(menuName, pageable);
        } else {
            managerMenuPage = managerMenuRepository.findAllWithParentAndChildren(pageable);
        }

        // menuType이 null이면 모든 메뉴 반환
        if (menuType == null) {
            return ManagerMenuPageResDTO.from(managerMenuPage);
        }

        // MenuType과 MenuLevel에 따른 필터링
        List<ManagerMenu> filteredMenus = managerMenuPage.getContent().stream()
            .filter(menu -> {
                // 1차, 2차 메뉴는 항상 제외
                if (menu.getMenuLevel() < 3) {
                    return false;
                }

                // 3차 메뉴는 MenuType에 따라 필터링
                try {
                    MenuType selectedType = MenuType.valueOf(menuType);
                    MenuType menuGroup = menu.getMenuGroup();

                    if (menuGroup == null) {
                        return false;
                    }

                    return switch (selectedType) {
                        case ADMIN_REFUND -> menuGroup == MenuType.SYSTEM || menuGroup == MenuType.ADMIN_REFUND;
                        case ADMIN_NORMAL -> menuGroup == MenuType.SYSTEM || menuGroup == MenuType.ADMIN_NORMAL;
                        case TEACHER -> menuGroup == MenuType.TEACHER;
                        case COMPANY -> menuGroup == MenuType.COMPANY;
                        case SYSTEM -> menuGroup == MenuType.SYSTEM;
                        case STUDENT -> menuGroup == MenuType.STUDENT;
                    };
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid menuType: {}", menuType);
                    return false;
                }
            })
            .toList();

        return ManagerMenuPageResDTO.from(new PageImpl<>(
            filteredMenus,
            pageable,
            filteredMenus.size()
        ));
    }


    /* ManagerMenu 전체 조회 (메뉴 타입별) */
    public List<ManagerMenu> findAllByMenuType(String menuType) {
        List<ManagerMenu> allMenus = managerMenuRepository.findAll();
        
        // menuType이 null이거나 'undefined'인 경우 3차 메뉴만 반환
        if (menuType == null || menuType.equals("undefined")) {
            return allMenus.stream()
                .filter(menu -> menu.getMenuLevel() == 3)
                .toList();
        }

        return allMenus.stream()
            .filter(menu -> {
                // 3차 메뉴가 아닌 경우 제외
                if (menu.getMenuLevel() != 3) {
                    return false;
                }

                try {
                    // MenuType에 따른 필터링
                    MenuType selectedType = MenuType.valueOf(menuType);
                    MenuType menuGroup = menu.getMenuGroup();

                    if (menuGroup == null) {
                        return false;
                    }

                    return switch (selectedType) {
                        case ADMIN_REFUND -> menuGroup == MenuType.SYSTEM || menuGroup == MenuType.ADMIN_REFUND;
                        case ADMIN_NORMAL -> menuGroup == MenuType.SYSTEM || menuGroup == MenuType.ADMIN_NORMAL;
                        case TEACHER -> menuGroup == MenuType.TEACHER;
                        case COMPANY -> menuGroup == MenuType.COMPANY;
                        case SYSTEM -> menuGroup == MenuType.SYSTEM;
                        case STUDENT -> menuGroup == MenuType.STUDENT;
                    };
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid menuType: {}", menuType);
                    return false;
                }
            })
            .toList();
    }

    /* ManagerMenu 삭제 */
    @Transactional
    public void managerMenuDelete(Long id) {
        // 1. 메뉴 조회 (자식 메뉴 함께 조회)
        ManagerMenu managerMenuPS = managerMenuRepository.findByIdWithParentAndChildren(id)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_MANAGER_MENU.getMessage()));

        Long menuIdx = managerMenuPS.getMenuIdx();
        
        // 2. 자식 메뉴 존재 여부 확인
        if (!managerMenuPS.getChildren().isEmpty()) {
            throw new Exception400("하위 메뉴가 존재하는 메뉴는 삭제할 수 없습니다. 하위 메뉴를 먼저 삭제해주세요.");
        }

        // 관련 ManagerAuth 삭제
        managerAuthRepository.deleteAllByMenuIdx(menuIdx);

        // 3. 삭제 처리
        managerMenuRepository.delete(managerMenuPS);

        // 4. 이벤트 발행
        eventPublisher.publishEvent(new ManagerMenuChangeEvent(this));
    }

    /* ManagerMenu 수정 */
    @Transactional
    public void managerMenuUpdate(Long id, ManagerMenuUpdateReqDTO reqDTO) {
        // 1. 메뉴 조회
        ManagerMenu managerMenuPS = managerMenuRepository.findById(id)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_MANAGER_MENU.getMessage()));

        // 2. 부모 메뉴가 있는 경우
        if (reqDTO.parentIdx() != null) {
            // 2-1. 부모 메뉴 조회
            ManagerMenu parentMenu = managerMenuRepository.findById(reqDTO.parentIdx())
                    .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_PARENT_MANAGER_MENU.getMessage()));
            
            // 2-2. 순환 참조 검사
            validateCircularReference(managerMenuPS, parentMenu);
            
            // 2-3. 엔티티 업데이트
            reqDTO.updateEntity(managerMenuPS, parentMenu);
        } else {
            // 3. 부모 메뉴가 없는 경우
            reqDTO.updateEntity(managerMenuPS, null);
        }

        eventPublisher.publishEvent(new ManagerMenuChangeEvent(this));
    }


    /* ManagerMenu 조회 */
    public ManagerMenuDetailResDTO managerMenuDetail(Long id) {
        // 1. 메뉴 조회 (fetch join으로 N+1 문제 방지)
        ManagerMenu managerMenuPS = managerMenuRepository.findByIdWithParentAndChildren(id)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_MANAGER_MENU.getMessage()));

        // 2. DTO 변환 및 반환
        return ManagerMenuDetailResDTO.from(managerMenuPS);
    }

    /* ManagerMenu 등록 */
    @Transactional
    public void managerMenuSave(ManagerMenuSaveReqDTO reqDTO) {
        // 1. 엔티티 생성
        ManagerMenu managerMenuPS = reqDTO.toEntity();

        // 2. 부모 메뉴가 있는 경우
        if (reqDTO.parentIdx() != null) {
            // 2-1. 부모 메뉴 조회
            ManagerMenu parentMenu = managerMenuRepository.findById(reqDTO.parentIdx())
                    .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_PARENT_MANAGER_MENU.getMessage()));

            // 2-2. 순환 참조 검사 (신규 생성이므로 자기 자신 체크만 필요)
            if (parentMenu.getMenuIdx().equals(managerMenuPS.getMenuIdx())) {
                throw new Exception400("자기 자신을 부모로 설정할 수 없습니다.");
            }

            // 2-3. 부모 메뉴 설정
            managerMenuPS.setParent(parentMenu);
        }

        // 3. 저장
        managerMenuRepository.save(managerMenuPS);

        eventPublisher.publishEvent(new ManagerMenuChangeEvent(this)); // 이벤트 발행
    }


    // 순환 참조 검사 메서드
    private void validateCircularReference(ManagerMenu menu, ManagerMenu newParent) {
        // 1. 자기 자신을 부모로 설정하는 경우
        if (newParent.getMenuIdx().equals(menu.getMenuIdx())) {
            throw new Exception400("자기 자신을 부모로 설정할 수 없습니다.");
        }

        // 2. 상위로 올라가면서 순환 참조 검사
        ManagerMenu currentParent = newParent;
        while (currentParent != null) {
            if (currentParent.getMenuIdx().equals(menu.getMenuIdx())) {
                throw new Exception400("상위 메뉴 중 자기 자신이 있어 순환 참조가 발생합니다.");
            }
            currentParent = currentParent.getParent();
        }

        // 3. 하위로 내려가면서 순환 참조 검사
        if (hasCircularReference(menu, newParent)) {
            throw new Exception400("하위 메뉴를 상위 메뉴로 설정할 수 없습니다.");
        }
    }

    private boolean hasCircularReference(ManagerMenu menu, ManagerMenu targetParent) {
        if (menu.getChildren() == null || menu.getChildren().isEmpty()) {
            return false;
        }

        for (ManagerMenu child : menu.getChildren()) {
            if (child.getMenuIdx().equals(targetParent.getMenuIdx())) {
                return true;
            }
            if (hasCircularReference(child, targetParent)) {
                return true;
            }
        }
        return false;
    }




    /**
     * 1차메뉴 추가
     */
    @Transactional
    public void saveDepth1Menu(Depth1MenuSaveReqDTO depth1MenuSaveReqDTO) {
        Long maxSortNoByMenuLevel = managerMenuRepository.findMaxSortNoByMenuLevel(1L);
        managerMenuRepository.save(depth1MenuSaveReqDTO.saveEntity(maxSortNoByMenuLevel + 1L));
    }

    /**
     * 2차메뉴 추가
     */
    @Transactional
    public void saveDepth2Menu(Depth2MenuSaveReqDTO depth2MenuSaveReqDTO) {
        ManagerMenu managerMenu = managerMenuRepository.findById(depth2MenuSaveReqDTO.parentIdx())
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_MANAGER_MENU.getMessage()));
        Long maxSortNoByMenuLevel = managerMenuRepository.findMaxSortNoByMenuLevel(2L);
        managerMenuRepository.save(depth2MenuSaveReqDTO.saveEntity(maxSortNoByMenuLevel + 1L, managerMenu));
    }

    /**
     * 3차메뉴 추가
     */
    @Transactional
    public void saveDepth3Menu(Depth3MenuSaveReqDTO depth3MenuSaveReqDTO) {
        ManagerMenu managerMenu = managerMenuRepository.findById(depth3MenuSaveReqDTO.parentIdx())
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_MANAGER_MENU.getMessage()));
        Long maxSortNoByMenuLevel = managerMenuRepository.findMaxSortNoByMenuLevel(3L);
        managerMenuRepository.save(depth3MenuSaveReqDTO.saveEntity(maxSortNoByMenuLevel + 1L, managerMenu));
    }

    /**
     * 메뉴 삭제
     */
    @Transactional
    public void deleteMenuByIdx(Long menuIdx) {
        ManagerMenu managerMenu = managerMenuRepository.findById(menuIdx)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_MANAGER_MENU.getMessage()));

        List<ManagerMenu> managerMenuList = managerMenuRepository.findByParent(managerMenu);
        if (!managerMenuList.isEmpty()) {
            throw new Exception400("하위 메뉴가 존재하여 삭제할 수 없습니다.");
        }

        // 삭제
        managerMenuRepository.delete(managerMenu);

    }

    public List<ManagerMenu> getDepth3MenusByParentIdx(Long parentIdx) {
        ManagerMenu managerMenu = managerMenuRepository.findById(parentIdx)
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_MANAGER_MENU.getMessage()));
        return managerMenuRepository.findByParentAndMenuLevel(managerMenu, 3L);
    }


    public List<ManagerMenu> findMenusByLevelAndType(long level, String menuGroup) {
        return managerMenuRepository.findByMenuLevelAndMenuGroup(level, MenuType.valueOf(menuGroup));
    }

    public Page<ManagerMenuSearchResDTO> getManagerMenuPage(String menuName, String chkUse, Pageable pageable) {

        Page<ManagerMenu> menuPage = null;

        if (Objects.equals(chkUse, "ALL")) {
            // 전체 조회
            menuPage = managerMenuRepository.findByMenuNameContainingIgnoreCaseAndMenuLevel(menuName, 3L, pageable);
        } else {
            menuPage = managerMenuRepository.findByMenuNameContainingIgnoreCaseAndChkUseAndMenuLevel(menuName, chkUse, 3L, pageable);
        }


        return menuPage.map(menu ->
                new ManagerMenuSearchResDTO(
                        menu.getMenuIdx(),
                        menu.getMenuGroup().getText(),
                        menu.getMenuName(),
                        menu.getUrl(),
                        menu.getChkUse()
                ));
    }
}
