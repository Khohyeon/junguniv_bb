package com.example.junguniv_bb.system.settings.controller;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.systemcode.controller.SystemCodeRestController;
import com.example.junguniv_bb.domain.systemcode.dto.SystemCodeSaveReqDTO;
import com.example.junguniv_bb.domain.systemcode.service.SystemCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 시스템설정 > 기본설정 메뉴의 REST API 컨트롤러
 */
@RestController
@RequestMapping("/masterpage_sys/settings/basic/api")
@RequiredArgsConstructor
@Slf4j
public class BasicSettingsRestController {

    private final SystemCodeRestController systemCodeRestController;
    private final SystemCodeService systemCodeService;

    /**
     * 관리자 로고 이미지 업로드
     */
    @PostMapping("/logo")
    public ResponseEntity<?> uploadAdminLogo(@RequestParam("file") MultipartFile file) {
        return systemCodeRestController.uploadAdminLogo(file);
    }

    /**
     * 기본 설정값 조회
     */
    @GetMapping("/settings")
    public ResponseEntity<?> getBasicSettings() {
        try {
            // 기본 설정 관련 시스템 코드들을 조회
            Map<String, String> settings = new HashMap<>();
            
            // 로그인 세션 관리 설정 추가
            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("LOGIN_SESSION", "기본설정")
                .ifPresent(code -> settings.put("로그인 세션관리", code.getSystemCodeValue()));
            
            // 각 설정값 조회 - 시스템 코드 그룹과 키로 조회
            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("COURSE_DEADLINE", "기본설정")
                .ifPresent(code -> settings.put("수강신청 마감시간", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("ADMIN_TITLE", "기본설정")
                .ifPresent(code -> settings.put("관리자모드 제목", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("CERTIFICATE_FOOTER", "기본설정")
                .ifPresent(code -> settings.put("증명서별 하단 기관명", code.getSystemCodeValue()));

            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("EVAL_NOTIFICATION", "기본설정")
                .ifPresent(code -> settings.put("평가알림기능", code.getSystemCodeValue()));

            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("AUTO_MESSAGE", "기본설정")
                .ifPresent(code -> settings.put("회원가입 안내 자동문자/메일 사용여부", code.getSystemCodeValue()));

            // 관리자 로고 이미지 조회
            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("ADMIN_LOGO", "기본설정")
                .ifPresent(code -> settings.put("관리자페이지 로고 이미지", code.getSystemCodeValue()));

            // 관리자 로고 이미지 원본 파일명 조회
            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("ADMIN_LOGO_ORIGINAL", "기본설정")
                .ifPresent(code -> settings.put("관리자페이지 로고 이미지_original", code.getSystemCodeValue()));

            return ResponseEntity.ok(APIUtils.success(settings));
        } catch (Exception e) {
            log.error("기본 설정값 조회 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIUtils.error(ExceptionMessage.NOT_FOUND_SYSTEM_CODE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    /**
     * 기본 설정값 저장
     */
    @PostMapping("/settings")
    public ResponseEntity<?> saveBasicSetting(@RequestBody Map<String, String> request) {
        try {
            String type = request.get("type");
            String value = request.get("value");
            String originalFileName = request.get("originalFileName");

            SystemCodeSaveReqDTO reqDTO = switch (type) {
                case "로그인 세션관리" -> {
                    // 값이 0~120 범위인지 검증
                    int sessionTime;
                    try {
                        sessionTime = Integer.parseInt(value);
                        if (sessionTime < 0 || sessionTime > 120) {
                            throw new Exception400("로그인 세션 시간은 0분에서 120분 사이여야 합니다.");
                        }
                    } catch (NumberFormatException e) {
                        throw new Exception400("올바른 숫자 형식이 아닙니다.");
                    }
                    yield new SystemCodeSaveReqDTO(
                        "로그인 세션관리",
                        "세션 시간 (분)",
                        "기본설정",
                        "LOGIN_SESSION",
                        value
                    );
                }
                case "수강신청 마감시간" -> new SystemCodeSaveReqDTO(
                    "수강신청 마감시간",
                    "시간 형식 (HH:mm)",
                    "기본설정",
                    "COURSE_DEADLINE",
                    value
                );
                case "관리자모드 제목" -> new SystemCodeSaveReqDTO(
                    "관리자모드 제목",
                    null,
                    "기본설정",
                    "ADMIN_TITLE",
                    value
                );
                case "증명서별 하단 기관명" -> new SystemCodeSaveReqDTO(
                    "증명서별 하단 기관명",
                    null,
                    "기본설정",
                    "CERTIFICATE_FOOTER",
                    value
                );
                case "평가알림기능" -> new SystemCodeSaveReqDTO(
                    "평가알림기능",
                    "Y/N",
                    "기본설정",
                    "EVAL_NOTIFICATION",
                    value
                );
                case "회원가입 안내 자동문자/메일 사용여부" -> new SystemCodeSaveReqDTO(
                    "회원가입 안내 자동문자/메일 사용여부",
                    "Y/N",
                    "기본설정",
                    "AUTO_MESSAGE",
                    value
                );
                case "관리자페이지 로고 이미지" -> new SystemCodeSaveReqDTO(
                    "관리자페이지 로고 이미지",
                    "이미지 파일명",
                    "기본설정",
                    "ADMIN_LOGO",
                    value
                );
                case "관리자페이지 로고 이미지 원본명" -> new SystemCodeSaveReqDTO(
                    "관리자페이지 로고 이미지 원본명",
                    "원본 파일명",
                    "기본설정",
                    "ADMIN_LOGO_ORIGINAL",
                    value
                );
                default -> throw new Exception400("지원하지 않는 설정 타입입니다: " + type);
            };

            return systemCodeRestController.systemCodeSave(reqDTO);
        } catch (Exception e) {
            log.error("기본 설정값 저장 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIUtils.error("설정 저장 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    /**
     * 계정 목록 조회 (모니터링 중지/가입불가/심사계정)
     */
    @GetMapping("/accounts/{type}")
    public ResponseEntity<?> getAccountList(@PathVariable String type) {
        try {
            String systemCodeKey = switch (type) {
                case "monitoring" -> "MONITORING_STOP_ACCOUNTS";
                case "forbidden" -> "FORBIDDEN_ACCOUNTS";
                case "review" -> "REVIEW_ACCOUNTS";
                default -> throw new Exception400("지원하지 않는 계정 타입입니다: " + type);
            };

            // 시스템 코드에서 계정 목록 조회
            return systemCodeService.findBySystemCodeKeyAndSystemCodeGroup(systemCodeKey, "계정관리")
                .map(code -> {
                    // 콤마로 구분된 계정 목록을 배열로 변환
                    List<String> accounts = code.getSystemCodeValue().isEmpty() 
                        ? new ArrayList<>() 
                        : Arrays.asList(code.getSystemCodeValue().split(","));
                    return ResponseEntity.ok(APIUtils.success(accounts));
                })
                .orElse(ResponseEntity.ok(APIUtils.success(new ArrayList<>())));
        } catch (Exception e) {
            log.error("계정 목록 조회 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIUtils.error("계정 목록 조회 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    /**
     * 계정 추가
     */
    @PostMapping("/accounts/{type}")
    public ResponseEntity<?> addAccount(@PathVariable String type, @RequestBody Map<String, String> request) {
        try {
            String account = request.get("account");
            if (account == null || account.trim().isEmpty()) {
                throw new Exception400("계정 ID를 입력해주세요.");
            }

            String systemCodeKey = switch (type) {
                case "monitoring" -> "MONITORING_STOP_ACCOUNTS";
                case "forbidden" -> "FORBIDDEN_ACCOUNTS";
                case "review" -> "REVIEW_ACCOUNTS";
                default -> throw new Exception400("지원하지 않는 계정 타입입니다: " + type);
            };

            // 기존 계정 목록 조회
            List<String> accounts = systemCodeService.findBySystemCodeKeyAndSystemCodeGroup(systemCodeKey, "계정관리")
                .map(code -> new ArrayList<>(Arrays.asList(code.getSystemCodeValue().split(","))))
                .orElse(new ArrayList<>());

            // 이미 존재하는 계정인지 확인
            if (accounts.contains(account)) {
                throw new Exception400("이미 등록된 계정입니다.");
            }

            // 새 계정 추가
            accounts.add(account);

            // 시스템 코드 저장
            SystemCodeSaveReqDTO reqDTO = switch (type) {
                case "monitoring" -> new SystemCodeSaveReqDTO(
                    "모니터링 전송 중지 계정",
                    "계정 목록 (콤마로 구분)",
                    "계정관리",
                    "MONITORING_STOP_ACCOUNTS",
                    String.join(",", accounts)
                );
                case "forbidden" -> new SystemCodeSaveReqDTO(
                    "가입불가 계정",
                    "계정 목록 (콤마로 구분)",
                    "계정관리",
                    "FORBIDDEN_ACCOUNTS",
                    String.join(",", accounts)
                );
                case "review" -> new SystemCodeSaveReqDTO(
                    "심사 계정",
                    "계정 목록 (콤마로 구분)",
                    "계정관리",
                    "REVIEW_ACCOUNTS",
                    String.join(",", accounts)
                );
                default -> throw new Exception400("지원하지 않는 계정 타입입니다: " + type);
            };

            return systemCodeRestController.systemCodeSave(reqDTO);
        } catch (Exception e) {
            log.error("계정 추가 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIUtils.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    /**
     * 계정 삭제
     */
    @DeleteMapping("/accounts/{type}/{account}")
    public ResponseEntity<?> deleteAccount(@PathVariable String type, @PathVariable String account) {
        try {
            String systemCodeKey = switch (type) {
                case "monitoring" -> "MONITORING_STOP_ACCOUNTS";
                case "forbidden" -> "FORBIDDEN_ACCOUNTS";
                case "review" -> "REVIEW_ACCOUNTS";
                default -> throw new Exception400("지원하지 않는 계정 타입입니다: " + type);
            };

            // 기존 계정 목록 조회
            List<String> accounts = systemCodeService.findBySystemCodeKeyAndSystemCodeGroup(systemCodeKey, "계정관리")
                .map(code -> new ArrayList<>(Arrays.asList(code.getSystemCodeValue().split(","))))
                .orElse(new ArrayList<>());

            // 계정 삭제
            if (!accounts.remove(account)) {
                throw new Exception400("존재하지 않는 계정입니다.");
            }

            // 시스템 코드 저장
            SystemCodeSaveReqDTO reqDTO = switch (type) {
                case "monitoring" -> new SystemCodeSaveReqDTO(
                    "모니터링 전송 중지 계정",
                    "계정 목록 (콤마로 구분)",
                    "계정관리",
                    "MONITORING_STOP_ACCOUNTS",
                    String.join(",", accounts)
                );
                case "forbidden" -> new SystemCodeSaveReqDTO(
                    "가입불가 계정",
                    "계정 목록 (콤마로 구분)",
                    "계정관리",
                    "FORBIDDEN_ACCOUNTS",
                    String.join(",", accounts)
                );
                case "review" -> new SystemCodeSaveReqDTO(
                    "심사 계정",
                    "계정 목록 (콤마로 구분)",
                    "계정관리",
                    "REVIEW_ACCOUNTS",
                    String.join(",", accounts)
                );
                default -> throw new Exception400("지원하지 않는 계정 타입입니다: " + type);
            };

            return systemCodeRestController.systemCodeSave(reqDTO);
        } catch (Exception e) {
            log.error("계정 삭제 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIUtils.error("계정 삭제 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
} 