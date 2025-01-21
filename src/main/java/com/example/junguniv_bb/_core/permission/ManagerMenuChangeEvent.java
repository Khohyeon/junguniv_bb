package com.example.junguniv_bb._core.permission;

import org.springframework.context.ApplicationEvent;

public class ManagerMenuChangeEvent extends ApplicationEvent {
    public ManagerMenuChangeEvent(Object source) {
        super(source);
    }
}