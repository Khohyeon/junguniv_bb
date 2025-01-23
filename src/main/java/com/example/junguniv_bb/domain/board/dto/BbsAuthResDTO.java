package com.example.junguniv_bb.domain.board.dto;

public record BbsAuthResDTO(
        boolean read,
        boolean write,
        boolean comment,
        boolean reply
) {
}
