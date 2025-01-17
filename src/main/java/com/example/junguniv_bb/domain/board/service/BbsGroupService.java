package com.example.junguniv_bb.domain.board.service;

import com.example.junguniv_bb.domain.board.dto.BbsGroupSaveReqDTO;
import com.example.junguniv_bb.domain.board.dto.BbsGroupUpdateReqDTO;
import com.example.junguniv_bb.domain.board.model.BbsGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BbsGroupService {

    private final BbsGroupRepository bbsGroupRepository;

    @Transactional
    public void bbsGroupSave(BbsGroupSaveReqDTO bbsGroupSaveReqDTO) {
        bbsGroupRepository.save(bbsGroupSaveReqDTO.saveEntity());
    }

    @Transactional
    public void bbsGroupUpdate(List<BbsGroupUpdateReqDTO> bbsGroupUpdateReqDTOList) {
        for (BbsGroupUpdateReqDTO bbsGroupUpdateReqDTO : bbsGroupUpdateReqDTOList) {
            bbsGroupRepository.save(bbsGroupUpdateReqDTO.updateEntity());
        }
    }
}
