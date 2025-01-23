package com.example.junguniv_bb._core.util;

import com.example.junguniv_bb.domain.member._enum.UserType;

import java.util.Arrays;

public class AuthUtil {

    public static boolean hasAccess(String authString, UserType userType) {
        if (authString == null || authString.isEmpty()) {
            return false; // 권한이 없는 경우
        }
        String[] allowedTypes = authString.split("@");
        return Arrays.stream(allowedTypes).anyMatch(type -> type.equalsIgnoreCase(userType.name()));
    }
}
