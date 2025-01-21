package com.example.junguniv_bb.domain.managermenu.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerMenuDepth3ListResDTO {
    private String name;
    private String url;
    private String target;
    private boolean active;

    public ManagerMenuDepth3ListResDTO(String name, String url, String target, boolean active) {
        this.name = name;
        this.url = url;
        this.target = target;
        this.active = active;
    }

}