package com.example.junguniv_bb.domain.systemcode.dto;

import com.example.junguniv_bb.domain.systemcode.model.SystemCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public record SystemCodePageResDTO(List<SystemCodeDTO> systemCodeList, PageableDTO pageable) {

    public static SystemCodePageResDTO from(Page<SystemCode> systemCodePage) {
        return new SystemCodePageResDTO(
            systemCodePage.getContent().stream()
                .map(SystemCodeDTO::from)
                .collect(Collectors.toList()),
            new PageableDTO(systemCodePage)
        );
    }

    public record SystemCodeDTO(
            Long systemCodeIdx, // 시스템 코드 IDX
            String systemCodeName, // 시스템 코드 이름
            String systemCodeRule, // 시스템 코드 규칙
            String systemCodeGroup, // 시스템 코드 그룹
            String systemCodeKey, // 시스템 코드 키
            String systemCodeValue // 시스템 코드 값
    ) {
        public static SystemCodeDTO from(SystemCode systemCode) {
            return new SystemCodeDTO(
                systemCode.getSystemCodeIdx(),
                systemCode.getSystemCodeName(),
                systemCode.getSystemCodeRule(),
                systemCode.getSystemCodeGroup(),
                systemCode.getSystemCodeKey(),
                systemCode.getSystemCodeValue()
            );
        }
    }

    public record PageableDTO(int pageNumber, int pageSize, int totalPages, long totalElements, boolean isLast,
            int numberOfElements, boolean isEmpty, Sort sort) {
        public PageableDTO(Page<SystemCode> systemCodePage) {
            this(systemCodePage.getNumber(), systemCodePage.getSize(), systemCodePage.getTotalPages(),
                    systemCodePage.getTotalElements(), systemCodePage.isLast(), systemCodePage.getNumberOfElements(),
                    systemCodePage.isEmpty(), systemCodePage.getSort());
        }
    }
}
