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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/masterpage_sys/mark/refund/api")
@RequiredArgsConstructor
@Slf4j
public class MarkRefundRestController {
    
    private final SystemCodeRestController systemCodeRestController;
    private final SystemCodeService systemCodeService;

    /**
     * 환급교육 기본 설정값 조회
     */
    @GetMapping("/settings")
    public ResponseEntity<?> getRefundSettings() {
        try {
            Map<String, String> settings = new HashMap<>();
            
            // 기본 사용값 설정
            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("SEQUENTIAL_LEARNING", "환급교육")
                .ifPresent(code -> settings.put("sequentialLearning", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("MOTP", "환급교육")
                .ifPresent(code -> settings.put("motp", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("IDENTITY_VERIFICATION", "환급교육")
                .ifPresent(code -> settings.put("identityVerification", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("LEARNING_50_PERCENT", "환급교육")
                .ifPresent(code -> settings.put("per", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("LCMS_LEARNING_RESTRICT", "환급교육")
                .ifPresent(code -> settings.put("lcmsLearningRestrict", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("MONITORING_OTP_AUTH", "환급교육")
                .ifPresent(code -> settings.put("monitoringOtpAuth", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("COURSE_BOARD_SHARE", "환급교육")
                .ifPresent(code -> settings.put("courseBoardShare", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("AUTO_SMS_MAIL", "환급교육")
                .ifPresent(code -> settings.put("autoSmsMail", code.getSystemCodeValue()));

            // 복습 기능 기본 설정
            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("REVIEW_PERIOD_TYPE", "환급교육")
                .ifPresent(code -> settings.put("reviewGiganChktype", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("REVIEW_PERIOD_VALUE", "환급교육")
                .ifPresent(code -> settings.put("reviewGiganChktypeValue", code.getSystemCodeValue()));

            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("REVIEW_TARGET", "환급교육")
                .ifPresent(code -> settings.put("reviewTarget", code.getSystemCodeValue()));

            // 수료 관련 기본 설정
            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("LCMS_PROGRESS_FINISH_PERCENT", "환급교육")
                .ifPresent(code -> settings.put("lcmsProgressFinishPercent", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("LCMS_PROGRESS_1DAY_PERCENT", "환급교육")
                .ifPresent(code -> settings.put("lcmsProgress1dayPercent", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("LCMS_LIMIT_UNIT_PERCENT", "환급교육")
                .ifPresent(code -> settings.put("lcmsLimitUnitPercent", code.getSystemCodeValue()));

            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("LCMS_RESULT_FINISH_TYPE", "환급교육")
                .ifPresent(code -> settings.put("lcmsResultFinishChktype", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyAndSystemCodeGroup("LCMS_RESULT_FINISH_VALUE", "환급교육")
                .ifPresent(code -> settings.put("lcmsResultFinishChktypeValue", code.getSystemCodeValue()));

            return ResponseEntity.ok(APIUtils.success(settings));
        } catch (Exception e) {
            log.error("환급교육 설정값 조회 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIUtils.error(ExceptionMessage.NOT_FOUND_SYSTEM_CODE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    /**
     * 환급교육 설정값 저장
     */
    @PostMapping("/settings")
    public ResponseEntity<?> saveRefundSetting(@RequestBody Map<String, String> request) {
        try {
            String type = request.get("type");
            String value = request.get("value");

            SystemCodeSaveReqDTO reqDTO = switch (type) {
                case "sequentialLearning" -> new SystemCodeSaveReqDTO(
                    "순차학습 여부",
                    "Y/N",
                    "환급교육",
                    "SEQUENTIAL_LEARNING",
                    value
                );
                case "motp" -> new SystemCodeSaveReqDTO(
                    "MOTP 사용 여부",
                    "Y/N",
                    "환급교육",
                    "MOTP",
                    value
                );
                case "identityVerification" -> new SystemCodeSaveReqDTO(
                    "본인인증 사용 여부",
                    "Y/N",
                    "환급교육",
                    "IDENTITY_VERIFICATION",
                    value
                );
                case "per" -> new SystemCodeSaveReqDTO(
                    "50% 학습 설정",
                    "Y/N",
                    "환급교육",
                    "LEARNING_50_PERCENT",
                    value
                );
                case "lcmsLearningRestrict" -> new SystemCodeSaveReqDTO(
                    "차시 학습 제한 여부",
                    "Y/N",
                    "환급교육",
                    "LCMS_LEARNING_RESTRICT",
                    value
                );
                case "monitoringOtpAuth" -> new SystemCodeSaveReqDTO(
                    "모니터링 전송 여부",
                    "Y/N",
                    "환급교육",
                    "MONITORING_OTP_AUTH",
                    value
                );
                case "courseBoardShare" -> new SystemCodeSaveReqDTO(
                    "개설과정 게시판 공유 여부",
                    "Y/N",
                    "환급교육",
                    "COURSE_BOARD_SHARE",
                    value
                );
                case "autoSmsMail" -> new SystemCodeSaveReqDTO(
                    "자동문자/메일 사용 여부",
                    "Y/N",
                    "환급교육",
                    "AUTO_SMS_MAIL",
                    value
                );
                case "reviewGiganChktype" -> new SystemCodeSaveReqDTO(
                    "복습 기간 유형",
                    "end/suryo",
                    "환급교육",
                    "REVIEW_PERIOD_TYPE",
                    value
                );
                case "reviewGiganChktypeValue" -> new SystemCodeSaveReqDTO(
                    "복습 기간 값",
                    "일수",
                    "환급교육",
                    "REVIEW_PERIOD_VALUE",
                    value
                );
                case "reviewTarget" -> new SystemCodeSaveReqDTO(
                    "복습 대상",
                    "target_suryo/target_all",
                    "환급교육",
                    "REVIEW_TARGET",
                    value
                );
                case "lcmsProgressFinishPercent" -> new SystemCodeSaveReqDTO(
                    "단원수업 최소 학습율",
                    "퍼센트",
                    "환급교육",
                    "LCMS_PROGRESS_FINISH_PERCENT",
                    value
                );
                case "lcmsProgress1dayPercent" -> new SystemCodeSaveReqDTO(
                    "1일 최대 진도율",
                    "퍼센트",
                    "환급교육",
                    "LCMS_PROGRESS_1DAY_PERCENT",
                    value
                );
                case "lcmsLimitUnitPercent" -> new SystemCodeSaveReqDTO(
                    "수료기준 진도율",
                    "퍼센트",
                    "환급교육",
                    "LCMS_LIMIT_UNIT_PERCENT",
                    value
                );
                case "lcmsResultFinishChktype" -> new SystemCodeSaveReqDTO(
                    "수료기준 종합점수 유형",
                    "OutOf100/each",
                    "환급교육",
                    "LCMS_RESULT_FINISH_TYPE",
                    value
                );
                case "lcmsResultFinishChktypeValue" -> new SystemCodeSaveReqDTO(
                    "수료기준 종합점수 값",
                    "점수",
                    "환급교육",
                    "LCMS_RESULT_FINISH_VALUE",
                    value
                );
                default -> throw new Exception400("지원하지 않는 설정 타입입니다: " + type);
            };

            return systemCodeRestController.systemCodeSave(reqDTO);
        } catch (Exception e) {
            log.error("환급교육 설정값 저장 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIUtils.error("설정 저장 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
