package com.example.junguniv_bb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UznDepth3MenuListResDTO {
    private String name;
    private String url;
    private String target;
    private boolean active;

    public UznDepth3MenuListResDTO(String name, String url, String target, boolean active) {
        this.name = name;
        this.url = url;
        this.target = target;
        this.active = active;
    }

}