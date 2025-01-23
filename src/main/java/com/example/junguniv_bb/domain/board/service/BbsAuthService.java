package com.example.junguniv_bb.domain.board.service;

import com.example.junguniv_bb._core.util.AuthUtil;
import com.example.junguniv_bb.domain.board.model.BbsGroup;
import com.example.junguniv_bb.domain.member._enum.UserType;
import org.springframework.stereotype.Service;

@Service
public class BbsAuthService {

    public boolean canRead(BbsGroup bbsGroup, UserType userType) {
        return AuthUtil.hasAccess(bbsGroup.getReadAuth(), userType);
    }

    public boolean canWrite(BbsGroup bbsGroup, UserType userType) {
        return AuthUtil.hasAccess(bbsGroup.getWriteAuth(), userType);
    }

    public boolean canReply(BbsGroup bbsGroup, UserType userType) {
        return AuthUtil.hasAccess(bbsGroup.getReplyAuth(), userType);
    }

    public boolean canComment(BbsGroup bbsGroup, UserType userType) {
        return AuthUtil.hasAccess(bbsGroup.getCommentAuth(), userType);
    }
}
