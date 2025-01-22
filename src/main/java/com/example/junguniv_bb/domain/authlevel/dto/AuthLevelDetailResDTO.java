package com.example.junguniv_bb.domain.authlevel.dto;


import java.time.LocalDateTime;

import com.example.junguniv_bb.domain.authlevel.model.AuthLevel;

public record AuthLevelDetailResDTO(
    Long authLevelIdx,
    String authLevelName,
    Long authLevel,
    String chkViewJumin,
    LocalDateTime createdDate,
    LocalDateTime updatedDate
) {
    public static AuthLevelDetailResDTO from(AuthLevel authLevel) {
        return new AuthLevelDetailResDTO(
            authLevel.getAuthLevelIdx(),
            authLevel.getAuthLevelName(),
            authLevel.getAuthLevel(),
            authLevel.getChkViewJumin(),
            authLevel.getCreatedDate(),
            authLevel.getUpdatedDate()
        );
    }
}
