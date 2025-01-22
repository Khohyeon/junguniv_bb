package com.example.junguniv_bb.domain._auth.dto;

import com.example.junguniv_bb._core.exception.ValidExceptionMessage;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ReqLoginDTO(
        @NotEmpty(message = ValidExceptionMessage.Message.INVALID_USERID)
        @Size(min = 4, max = 15, message = ValidExceptionMessage.Message.INVALID_USERID)
        String userId,

        @NotEmpty(message = ValidExceptionMessage.Message.INVALID_PASSWORD)
        @Size(min = 4, max = 20, message = ValidExceptionMessage.Message.INVALID_PASSWORD)
        String pwd
) {
}
