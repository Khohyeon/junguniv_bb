package com.example.junguniv_bb.masterpage.sys;

import com.example.junguniv_bb.masterpage.sys.dto.Depth3Menu;
import com.example.junguniv_bb.masterpage.sys.dto.Menu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class SysController {

    @GetMapping
    public String index(Model model) {
        model.addAttribute("title", "Home Page");
        model.addAttribute("name", "World");
        return "index";
    }


    // TODO : 메인 메뉴에 띄울 데이터를 넣어 줘야함
    @ModelAttribute("xbigMenus")
    public List<Menu> populateBigMenus() {
        // 공통 데이터 생성
        return List.of(
                new Menu("Code1", "Main Menu 1", "/main1", "_self", List.of(
                        new Menu.SubMenu("SubCode1", "Sub Menu 1", "/sub1"),
                        new Menu.SubMenu("SubCode2", "Sub Menu 2", "/sub2")
                )),
                new Menu("Code2", "Main Menu 2", "/main2", "_self", List.of(
                        new Menu.SubMenu("SubCode3", "Sub Menu 3", "/sub3"),
                        new Menu.SubMenu("SubCode4", "Sub Menu 4", "/sub4")
                ))
        );
    }

    @ModelAttribute("sysMenuGroup")
    public String populateSysMenuGroup() {
        return "Code1"; // 활성화된 메뉴 그룹
    }

    @ModelAttribute("sysBranchSubIdx")
    public String populateSysBranchSubIdx() {
        return "SubCode1"; // 활성화된 서브 메뉴
    }

    @ModelAttribute("depth3Menus")
    public List<Depth3Menu> populateDepth3Menus() {
        return List.of(
                new Depth3Menu("Menu1", "/menu1", "_self", false),
                new Depth3Menu("Menu2", "/menu2", "_self", true), // 활성화된 메뉴
                new Depth3Menu("Menu3", "/menu3", "_self", false)
        );
    }
}
