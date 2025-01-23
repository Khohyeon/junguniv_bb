package com.example.junguniv_bb.domain.managermenu.dto;

public record ManagerMenuDTO(
    Long menuIdx,
    String menuName,
    String menuGroup,
    Long parentMenuIdx
) {} 