package com.example.junguniv_bb.domain.authLevel.dto;

import com.example.junguniv_bb.domain.authLevel.model.AuthLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record AuthLevelPageResDTO(List<AuthLevelDTO> authLevelList, PageableDTO pageable) {

    public AuthLevelPageResDTO(Page<AuthLevel> authLevelPage) {
        this(
            authLevelPage.getContent().stream()
                .map(AuthLevelDTO::from)
                .collect(Collectors.toList()),
            new PageableDTO(authLevelPage)
        );
    }

    public static AuthLevelPageResDTO from(Page<AuthLevel> authLevelPage) {
        return new AuthLevelPageResDTO(
            authLevelPage.getContent().stream()
                .map(AuthLevelDTO::from)
                .collect(Collectors.toList()),
            new PageableDTO(authLevelPage)
        );
    }

    public record AuthLevelDTO(
            Long authLevelIdx,
            String authLevelName,
            Long authLevel,
            String chkViewJumin,
            LocalDateTime createdDate,
            LocalDateTime updatedDate
    ) {
        public static AuthLevelDTO from(AuthLevel authLevel) {
            return new AuthLevelDTO(
                authLevel.getAuthLevelIdx(),
                authLevel.getAuthLevelName(),
                authLevel.getAuthLevel(),
                authLevel.getChkViewJumin(),
                authLevel.getCreatedDate(),
                authLevel.getUpdatedDate()
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
