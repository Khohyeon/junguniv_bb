package com.example.junguniv_bb.domain.authlevel.dto;

import com.example.junguniv_bb.domain.authlevel.model.AuthLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record AuthLevelPageResDTO(List<AuthLevelDTO> authLevelList, PageableDTO pageable) {

    public AuthLevelPageResDTO(Page<AuthLevel> authLevelPage, Map<Long, Long> memberCountMap) {
        this(
            authLevelPage.getContent().stream()
                .map(authLevel -> AuthLevelDTO.from(authLevel, memberCountMap.getOrDefault(authLevel.getAuthLevel(), 0L)))
                .collect(Collectors.toList()),
            new PageableDTO(authLevelPage)
        );
    }

    public static AuthLevelPageResDTO from(Page<AuthLevel> authLevelPage, Map<Long, Long> memberCountMap) {
        return new AuthLevelPageResDTO(authLevelPage, memberCountMap);
    }

    public record AuthLevelDTO(
            Long authLevelIdx,
            String authLevelName,
            Long authLevel,
            String chkViewJumin,
            LocalDateTime createdDate,
            LocalDateTime updatedDate,
            Long linkedMemberCount
    ) {
        public static AuthLevelDTO from(AuthLevel authLevel, Long linkedMemberCount) {
            return new AuthLevelDTO(
                authLevel.getAuthLevelIdx(),
                authLevel.getAuthLevelName(),
                authLevel.getAuthLevel(),
                authLevel.getChkViewJumin(),
                authLevel.getCreatedDate(),
                authLevel.getUpdatedDate(),
                linkedMemberCount
            );
        }
    }

    public record PageableDTO(int pageNumber, int pageSize, int totalPages, long totalElements, boolean isLast,
            int numberOfElements, boolean isEmpty, Sort sort) {
        public PageableDTO(Page<AuthLevel> authLevelPage) {
            this(authLevelPage.getNumber(), authLevelPage.getSize(), authLevelPage.getTotalPages(),
                    authLevelPage.getTotalElements(), authLevelPage.isLast(), authLevelPage.getNumberOfElements(),
                    authLevelPage.isEmpty(), authLevelPage.getSort());
        }
    }
}
