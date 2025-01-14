package com.example.junguniv_bb.masterpage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Depth3Menu {
    private String name;
    private String url;
    private String target;
    private boolean active;

    public Depth3Menu(String name, String url, String target, boolean active) {
        this.name = name;
        this.url = url;
        this.target = target;
        this.active = active;
    }

}