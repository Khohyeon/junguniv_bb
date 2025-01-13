package com.example.junguniv_bb.masterpage.sys;

import com.example.junguniv_bb.masterpage.sys.dto.Menu;
import com.example.junguniv_bb.masterpage.uznBranch.model.UznBranch;
import com.example.junguniv_bb.masterpage.uznBranch.model.UznBranchRepository;
import com.example.junguniv_bb.masterpage.uznBranch.model.UznBranchSub;
import com.example.junguniv_bb.masterpage.uznBranch.model.UznBranchSubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SysController {

    private final UznBranchRepository uznBranchRepository;
    private final UznBranchSubRepository uznBranchSubRepository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("title", "Home Page");
        model.addAttribute("name", "World");
        return "index";
    }


    /**
     * 시스템 설정 탭의 Header에 고정으로 넣는 데이터 값
     * 모델에 항상 띄워 놓은 후 Unz_Branch로 부터 메뉴 와 url을 불러옵니다.
     */
    @ModelAttribute("xbigMenus")
    public List<Menu> populateBigMenus() {
        // 데이터베이스에서 상위 메뉴 조회
        List<UznBranch> branches = uznBranchRepository.findAllByChkUseOrderBySortno("Y");

        return branches.stream().map(branch -> {
            // 각 상위 메뉴에 해당하는 하위 메뉴 조회
            List<UznBranchSub> subMenus = uznBranchSubRepository.findAllByBranch_BranchIdxAndChkUseOrderBySortno(branch.getBranchIdx(), "Y");

            // 하위 메뉴를 Menu.SubMenu 객체로 변환
            List<Menu.SubMenu> subMenuList = subMenus.stream()
                    .map(sub -> new Menu.SubMenu(
                            String.valueOf(sub.getBranchSubIdx()),
                            sub.getSubName(),
                            sub.getUrl()
                    ))
                    .toList();

            // 상위 메뉴의 URL은 하위 메뉴 중 첫 번째 메뉴의 URL로 설정
            String mainMenuUrl = subMenus.isEmpty() ? "#" : subMenus.get(0).getUrl();

            // 상위 메뉴를 Menu 객체로 변환
            return new Menu(
                    String.valueOf(branch.getBranchIdx()),
                    branch.getBranchName(),
                    mainMenuUrl,
                    "_self",
                    subMenuList
            );
        }).toList();
    }

}
