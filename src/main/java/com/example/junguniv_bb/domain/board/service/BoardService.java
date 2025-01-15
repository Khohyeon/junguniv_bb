package com.example.junguniv_bb.domain.board.service;

import com.example.junguniv_bb.domain.board.dto.BbsGroupPageResDTO;
import com.example.junguniv_bb.domain.board.dto.BoardSearchResDTO;
import com.example.junguniv_bb.domain.board.model.Bbs;
import com.example.junguniv_bb.domain.board.model.BbsGroup;
import com.example.junguniv_bb.domain.board.model.BbsGroupRepository;
import com.example.junguniv_bb.domain.board.model.BbsRepository;
import com.example.junguniv_bb.domain.counsel.dto.CounselPageResDTO;
import com.example.junguniv_bb.domain.counsel.model.Counsel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BbsRepository bbsRepository;
    private final BbsGroupRepository bbsGroupRepository;

    /**
     * 공지사항 검색 조회
     * 응답 형태 : Page<BoardSearchReqDTO>
     */
    public Page<BoardSearchResDTO> searchByName(String title, String boardType, Pageable pageable) {
        BbsGroup bbsGroup = bbsGroupRepository.findByBbsGroupName(boardType);

        Page<Bbs> bbsPage = bbsRepository.findByTitleContainingIgnoreCaseAndBbsGroupIdx(title, bbsGroup.getBbsGroupIdx(),pageable);
        return bbsPage.map(bbs ->
                new BoardSearchResDTO(
                        bbs.getBbsIdx(),
                        bbs.getBbsGroupIdx(),
                        bbs.getTitle(),
                        bbs.getFormattedCreatedDate2(),
                        bbs.getReadNum()
                ));
    }

    /**
     * 홈페이지게시판 리스트 조회
     * 응답 형태 : Page<BbsGroupPageResDTO>
     */
    public Page<BbsGroupPageResDTO> getBbsGroupPage(Pageable pageable) {
        Page<BbsGroup> bbsGroupPage = bbsGroupRepository.findAll(pageable);

        return bbsGroupPage.map(bbsGroup ->
                new BbsGroupPageResDTO(
                        bbsGroup.getBbsGroupIdx(),
                        bbsGroup.getBbsId(),
                        bbsGroup.getBbsGroupName(),
                        bbsGroup.getFileNum()
                ));
    }

}
