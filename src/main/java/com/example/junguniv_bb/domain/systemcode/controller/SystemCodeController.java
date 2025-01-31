package com.example.junguniv_bb.domain.systemcode.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.junguniv_bb.domain.systemcode.service.SystemCodeService;

@Controller
@RequestMapping("/masterpage_sys/system_code")
@RequiredArgsConstructor
@Slf4j
public class SystemCodeController {

    /* DI */
    private final SystemCodeService systemCodeService;

    @GetMapping("/basic")
    public String basic() {

        return null;
    }

    /* 다중 삭제 */

    
    /* 삭제 */


    /* 수정 */


    /* 조회 */
    
    
    /* 저장 */
}
