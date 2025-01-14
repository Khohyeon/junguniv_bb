package com.example.junguniv_bb._core.security;

import com.example.junguniv_bb._core.exception.Exception401;
import com.example.junguniv_bb.domain.member.model.Member;
import com.example.junguniv_bb.domain.member.model.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new Exception401("존재하지 않는 아이디입니다."));

        // 계정 상태 확인
        if ("N".equals(member.getMemberState())) {
            throw new Exception401("/id-stop");  // 리다이렉트할 URL을 예외 메시지로 전달
        }

        return new CustomUserDetails(member);
    }


}