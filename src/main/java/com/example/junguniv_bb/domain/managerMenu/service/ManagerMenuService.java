package com.example.junguniv_bb.domain.managerMenu.service;


import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import com.example.junguniv_bb._core.permission.ManagerMenuChangeEvent;
import com.example.junguniv_bb.domain.managerAuth.model.ManagerAuthRepository;
import com.example.junguniv_bb.domain.managerMenu.dto.*;
import com.example.junguniv_bb.domain.managerMenu.model.ManagerMenu;
import com.example.junguniv_bb.domain.managerMenu.model.ManagerMenuRepository;
import com.example.junguniv_bb.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ManagerMenuService {

    /* DI */
    private final ManagerMenuRepository managerMenuRepository;
    private final ManagerAuthRepository managerAuthRepository;
    private final ApplicationEventPublisher eventPublisher; // 이벤트 발행을 위한 DI


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
    public ManagerMenuPageResDTO managerMenuPage(Member member, Pageable pageable, String menuName) {
        // TODO 권한 체크

        // 1. DB 조회 (fetch join으로 N+1 문제 방지)
        Page<ManagerMenu> managerMenuPagePS = (menuName != null)
            ? managerMenuRepository.findByMenuNameContainingIgnoreCaseWithParentAndChildren(menuName, pageable)
            : managerMenuRepository.findAllWithParentAndChildren(pageable);

        // 2. DTO 변환 및 반환
        return ManagerMenuPageResDTO.from(managerMenuPagePS);
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
}
