package com.example.junguniv_bb._core.util;

import org.springframework.stereotype.Component;

@Component
public class PrivacyMaskingUtil {
    
    // 이름 마스킹
    public String maskName(String name) {
        if (name == null || name.trim().isEmpty()) return name;
        if (name.length() < 2) return "*";
        return name.substring(0, 1) + "*".repeat(name.length() - 1);
    }
    
    // 주민번호 마스킹 (뒤 6자리)
    public String maskResidentNumber(String residentNumber) {
        if (residentNumber == null || residentNumber.trim().isEmpty()) return residentNumber;
        if (!residentNumber.contains("-") || residentNumber.length() < 8) return residentNumber;
        try {
            String[] parts = residentNumber.split("-");
            return parts[0] + "-" + parts[1].charAt(0) + "*".repeat(6);
        } catch (Exception e) {
            return residentNumber; // 형식이 맞지 않으면 원본 반환
        }
    }
    
    // 이메일 마스킹
    public String maskEmail(String email) {
        if (email == null || email.trim().isEmpty() || !email.contains("@")) return email;
        try {
            String[] parts = email.split("@");
            if (parts.length != 2) return email;
            String localPart = parts[0];
            String maskedLocal = localPart.substring(0, Math.min(3, localPart.length())) 
                + "*".repeat(Math.max(localPart.length() - 3, 0));
            return maskedLocal + "@" + parts[1];
        } catch (Exception e) {
            return email; // 형식이 맞지 않으면 원본 반환
        }
    }
    
    // 집전화번호 마스킹 (지역번호 제외)
    public String maskTelHome(String phone) {
        if (phone == null || phone.trim().isEmpty()) return phone;
        try {
            return phone.replaceAll("(\\d{2,3})-(\\d{3,4})-(\\d{4})", "$1-****-****");
        } catch (Exception e) {
            return phone; // 형식이 맞지 않으면 원본 반환
        }
    }
    
    // 휴대전화 마스킹
    public String maskTelMobile(String phone) {
        if (phone == null || phone.trim().isEmpty()) return phone;
        try {
            return phone.replaceAll("(\\d{3})-(\\d{3,4})-(\\d{4})", "$1-****-****");
        } catch (Exception e) {
            return phone; // 형식이 맞지 않으면 원본 반환
        }
    }
    
    // 우편번호 마스킹
    public String maskZipcode(String zipCode) {
        if (zipCode == null || zipCode.trim().isEmpty()) return zipCode;
        if (zipCode.length() < 5) return zipCode;
        return "*****";
    }
    
    // 주소1 마스킹 (시/군/구까지만 표시)
    public String maskAddr1(String addr) {
        if (addr == null || addr.trim().isEmpty()) return addr;
        try {
            String[] parts = addr.split(" ");
            if (parts.length <= 2) return addr;
            
            StringBuilder masked = new StringBuilder();
            for (int i = 0; i < parts.length; i++) {
                if (i < 2) {
                    masked.append(parts[i]).append(" ");
                } else {
                    masked.append("**** ");
                }
            }
            return masked.toString().trim();
        } catch (Exception e) {
            return addr; // 형식이 맞지 않으면 원본 반환
        }
    }
    
    // 주소2 마스킹 (전체 마스킹)
    public String maskAddr2(String addr) {
        if (addr == null || addr.trim().isEmpty()) return addr;
        return "*".repeat(addr.length());
    }
    
    // 계좌번호 마스킹 (뒷 4자리 제외)
    public String maskBankNumBer(String account) {
        if (account == null || account.trim().isEmpty()) return account;
        if (account.length() < 4) return account;
        return "*".repeat(account.length() - 4) + account.substring(account.length() - 4);
    }
} 