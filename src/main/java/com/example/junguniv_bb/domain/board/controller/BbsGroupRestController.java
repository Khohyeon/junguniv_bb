package com.example.junguniv_bb.domain.board.controller;

import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.board.dto.BbsGroupSaveReqDTO;
import com.example.junguniv_bb.domain.board.dto.BbsGroupUpdateReqDTO;
import com.example.junguniv_bb.domain.board.model.BbsGroup;
import com.example.junguniv_bb.domain.board.service.BbsGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterpage_sys/bbsGroup/api")
@RequiredArgsConstructor
public class BbsGroupRestController {

    private final BbsGroupService bbsGroupService;

    @PostMapping("/save")
    public ResponseEntity<APIUtils.APIResult<String>> bbsGroupSave(@RequestBody BbsGroupSaveReqDTO bbsGroupSaveReqDTO) {
        bbsGroupService.bbsGroupSave(bbsGroupSaveReqDTO);

        return ResponseEntity.ok(APIUtils.success("게시판등록이 성공적으로 완료되었습니다."));
    }

    @PutMapping("/update")
    public ResponseEntity<APIUtils.APIResult<String>> bbsGroupUpdate(@RequestBody List<BbsGroupUpdateReqDTO> bbsGroupUpdateReqDTOList) {
        bbsGroupService.bbsGroupUpdate(bbsGroupUpdateReqDTOList);

        return ResponseEntity.ok(APIUtils.success("게시판수정이 성공적으로 완료되었습니다."));
    }
}
