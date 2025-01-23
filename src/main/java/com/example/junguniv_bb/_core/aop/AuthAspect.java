package com.example.junguniv_bb._core.aop;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.Exception403;
import com.example.junguniv_bb._core.security.CustomUserDetails;
import com.example.junguniv_bb._core.util.AuthUtil;
import com.example.junguniv_bb.domain.board.model.BbsGroup;
import com.example.junguniv_bb.domain.board.model.BbsGroupRepository;
import com.example.junguniv_bb.domain.member._enum.UserType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthAspect {

    private final BbsGroupRepository bbsGroupRepository;

    public AuthAspect(BbsGroupRepository bbsGroupRepository) {
        this.bbsGroupRepository = bbsGroupRepository;
    }

    @Around("@annotation(checkAuth)")
    public Object checkAuth(ProceedingJoinPoint joinPoint, CheckAuth checkAuth) throws Throwable {
        // 1. 현재 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserType currentUserType;

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            currentUserType = UserType.GUEST; // 인증되지 않은 사용자는 GUEST
        } else {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            currentUserType = customUserDetails.getMember().getUserType();
        }

        // 2. 필요한 권한 확인
        String requiredAuth = checkAuth.value(); // 어노테이션의 value 값 (READ, WRITE, UPDATE, DELETE)

        // 3. 게시판 정보 가져오기 (joinPoint를 사용해 메서드 매개변수에서 추출)
        BbsGroup bbsGroup = getBbsGroupFromRequest(joinPoint);

        // 4. 권한 검증
        String authField; // 읽기, 쓰기, 수정, 삭제 필드 선택
        switch (requiredAuth) {
            case "WRITE":
                authField = bbsGroup.getWriteAuth();
                break;
            case "UPDATE":
                authField = bbsGroup.getReplyAuth();
                break;
            case "DELETE":
                authField = bbsGroup.getCommentAuth();
                break;
            default: // READ
                authField = bbsGroup.getReadAuth();
                break;
        }

        if (!AuthUtil.hasAccess(authField, currentUserType)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        // 5. 권한 검증 성공 시 메서드 실행
        return joinPoint.proceed();
    }


    private BbsGroup getBbsGroupFromRequest(ProceedingJoinPoint joinPoint) {
        // 메서드의 모든 매개변수를 가져옵니다.
        Object[] args = joinPoint.getArgs();

        Long bbsIdx = null;

        // 매개변수에서 Long 타입의 게시판 ID를 찾습니다.
        for (Object arg : args) {
            if (arg instanceof Long) {
                bbsIdx = (Long) arg;
                break;
            }
        }

        if (bbsIdx == null) {
            throw new IllegalArgumentException("게시판 ID(bbsId)를 찾을 수 없습니다.");
        }

        // BbsGroup을 데이터베이스에서 조회합니다.
        return bbsGroupRepository.findById(bbsIdx)
                .orElseThrow(() -> new Exception400("게시판 정보를 찾을 수 없습니다."));
    }

}
