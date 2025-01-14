package com.example.junguniv_bb._core.util;


import com.example.junguniv_bb._core.exception.Exception403;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import com.example.junguniv_bb._core.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class AuthorityCheckUtils {

    public static boolean authorityCheck(CustomUserDetails customUserDetails, String userRole) {
        // 권한 체크
        if (!customUserDetails.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
                        .equals(userRole))) {

            log.warn("인가되지 않은 요청입니다.");
            throw new Exception403(ExceptionMessage.COMMON_FORBIDDEN.getMessage());
        }

        return true;
    }

    public static boolean authorityCheck(CustomUserDetails customUserDetails, List<String> userRole) {

        var authorities = customUserDetails.getAuthorities()
                .stream()
                .filter(grantedAuthority -> userRole.contains(grantedAuthority.getAuthority()))
                .toList();

        if (authorities.size() >= 1) {
            return true;
        } else {
            log.warn("인가되지 않은 요청입니다.");
            throw new Exception403(ExceptionMessage.COMMON_FORBIDDEN.getMessage());
        }
    }
}
