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
@RequestMapping("/masterpage_sys/mark/normal/api")
@RequiredArgsConstructor
@Slf4j
public class MarkNormalRestController {
    
    private final SystemCodeRestController systemCodeRestController;
    private final SystemCodeService systemCodeService;

    /**
     * 일반교육 기본 설정값 조회
     */
    @GetMapping("/settings")
    public ResponseEntity<?> getNormalSettings() {
        try {
            Map<String, String> settings = new HashMap<>();
            
            // 기본 사용값 설정
            systemCodeService.findBySystemCodeKeyForEducationType("SEQUENTIAL_LEARNING", "일반교육")
                .ifPresent(code -> settings.put("sequentialLearning", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyForEducationType("COURSE_TAKE_AUTO", "일반교육")
                .ifPresent(code -> settings.put("courseTakeAuto", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyForEducationType("IDENTITY_VERIFICATION", "일반교육")
                .ifPresent(code -> settings.put("identityVerification", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyForEducationType("LCMS_LEARNING_RESTRICT", "일반교육")
                .ifPresent(code -> settings.put("lcmsLearningRestrict", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyForEducationType("LEARNING_50_PERCENT", "일반교육")
                .ifPresent(code -> settings.put("per", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyForEducationType("RETAKE_AUTO", "일반교육")
                .ifPresent(code -> settings.put("retakeAuto", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyForEducationType("COURSE_BOARD_SHARE", "일반교육")
                .ifPresent(code -> settings.put("courseBoardShare", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyForEducationType("AUTO_SMS_MAIL", "일반교육")
                .ifPresent(code -> settings.put("autoSmsMail", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyForEducationType("SURYO_AUTO", "일반교육")
                .ifPresent(code -> settings.put("suryoAuto", code.getSystemCodeValue()));

            // 복습 기능 기본 설정
            systemCodeService.findBySystemCodeKeyForEducationType("REVIEW_PERIOD_TYPE", "일반교육")
                .ifPresent(code -> settings.put("reviewGiganChktype", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyForEducationType("REVIEW_PERIOD_VALUE", "일반교육")
                .ifPresent(code -> settings.put("reviewGiganChktypeValue", code.getSystemCodeValue()));

            systemCodeService.findBySystemCodeKeyForEducationType("REVIEW_TARGET", "일반교육")
                .ifPresent(code -> settings.put("reviewTarget", code.getSystemCodeValue()));

            // 수료 관련 기본 설정
            systemCodeService.findBySystemCodeKeyForEducationType("LCMS_PROGRESS_FINISH_PERCENT", "일반교육")
                .ifPresent(code -> settings.put("lcmsProgressFinishPercent", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyForEducationType("LCMS_PROGRESS_1DAY_PERCENT", "일반교육")
                .ifPresent(code -> settings.put("lcmsProgress1dayPercent", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyForEducationType("LCMS_LIMIT_UNIT_PERCENT", "일반교육")
                .ifPresent(code -> settings.put("lcmsLimitUnitPercent", code.getSystemCodeValue()));

            systemCodeService.findBySystemCodeKeyForEducationType("LCMS_RESULT_FINISH_TYPE", "일반교육")
                .ifPresent(code -> settings.put("lcmsResultFinishChktype", code.getSystemCodeValue()));
            
            systemCodeService.findBySystemCodeKeyForEducationType("LCMS_RESULT_FINISH_VALUE", "일반교육")
                .ifPresent(code -> settings.put("lcmsResultFinishChktypeValue", code.getSystemCodeValue()));

            return ResponseEntity.ok(APIUtils.success(settings));
        } catch (Exception e) {
            log.error("일반교육 설정값 조회 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIUtils.error(ExceptionMessage.NOT_FOUND_SYSTEM_CODE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    /**
     * 일반교육 설정값 저장
     */
    @PostMapping("/settings")
    public ResponseEntity<?> saveNormalSetting(@RequestBody Map<String, String> request) {
        try {
            String type = request.get("type");
            String value = request.get("value");

            SystemCodeSaveReqDTO reqDTO = switch (type) {
                case "sequentialLearning" -> new SystemCodeSaveReqDTO(
                    "순차학습 여부",
                    "Y/N",
                    "일반교육",
                    "SEQUENTIAL_LEARNING",
                    value
                );
                case "courseTakeAuto" -> new SystemCodeSaveReqDTO(
                    "수강자동승인",
                    "Y/N",
                    "일반교육",
                    "COURSE_TAKE_AUTO",
                    value
                );
                case "identityVerification" -> new SystemCodeSaveReqDTO(
                    "본인인증 사용 여부",
                    "Y/N",
                    "일반교육",
                    "IDENTITY_VERIFICATION",
                    value
                );
                case "lcmsLearningRestrict" -> new SystemCodeSaveReqDTO(
                    "차시 학습 제한 여부",
                    "Y/N",
                    "일반교육",
                    "LCMS_LEARNING_RESTRICT",
                    value
                );
                case "per" -> new SystemCodeSaveReqDTO(
                    "50% 학습 설정",
                    "Y/N",
                    "일반교육",
                    "LEARNING_50_PERCENT",
                    value
                );
                case "retakeAuto" -> new SystemCodeSaveReqDTO(
                    "자동 재응시",
                    "Y/N",
                    "일반교육",
                    "RETAKE_AUTO",
                    value
                );
                case "courseBoardShare" -> new SystemCodeSaveReqDTO(
                    "개설과정 게시판 공유 여부",
                    "Y/N",
                    "일반교육",
                    "COURSE_BOARD_SHARE",
                    value
                );
                case "autoSmsMail" -> new SystemCodeSaveReqDTO(
                    "자동문자/메일 사용 여부",
                    "Y/N",
                    "일반교육",
                    "AUTO_SMS_MAIL",
                    value
                );
                case "suryoAuto" -> new SystemCodeSaveReqDTO(
                    "자동 수료",
                    "Y/N",
                    "일반교육",
                    "SURYO_AUTO",
                    value
                );
                case "reviewGiganChktype" -> new SystemCodeSaveReqDTO(
                    "복습 기간 유형",
                    "end/suryo",
                    "일반교육",
                    "REVIEW_PERIOD_TYPE",
                    value
                );
                case "reviewGiganChktypeValue" -> new SystemCodeSaveReqDTO(
                    "복습 기간 값",
                    "일수",
                    "일반교육",
                    "REVIEW_PERIOD_VALUE",
                    value
                );
                case "reviewTarget" -> new SystemCodeSaveReqDTO(
                    "복습 대상",
                    "target_suryo/target_all",
                    "일반교육",
                    "REVIEW_TARGET",
                    value
                );
                case "lcmsProgressFinishPercent" -> new SystemCodeSaveReqDTO(
                    "단원수업 최소 학습율",
                    "퍼센트",
                    "일반교육",
                    "LCMS_PROGRESS_FINISH_PERCENT",
                    value
                );
                case "lcmsProgress1dayPercent" -> new SystemCodeSaveReqDTO(
                    "1일 최대 진도율",
                    "퍼센트",
                    "일반교육",
                    "LCMS_PROGRESS_1DAY_PERCENT",
                    value
                );
                case "lcmsLimitUnitPercent" -> new SystemCodeSaveReqDTO(
                    "수료기준 진도율",
                    "퍼센트",
                    "일반교육",
                    "LCMS_LIMIT_UNIT_PERCENT",
                    value
                );
                case "lcmsResultFinishChktype" -> new SystemCodeSaveReqDTO(
                    "수료기준 종합점수 유형",
                    "OutOf100/each",
                    "일반교육",
                    "LCMS_RESULT_FINISH_TYPE",
                    value
                );
                case "lcmsResultFinishChktypeValue" -> new SystemCodeSaveReqDTO(
                    "수료기준 종합점수 값",
                    "점수",
                    "일반교육",
                    "LCMS_RESULT_FINISH_VALUE",
                    value
                );
                default -> throw new Exception400("지원하지 않는 설정 타입입니다: " + type);
            };

            return systemCodeRestController.systemCodeSave(reqDTO);
        } catch (Exception e) {
            log.error("일반교육 설정값 저장 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIUtils.error("설정 저장 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
