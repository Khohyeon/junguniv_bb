package com.example.junguniv_bb.domain._auth.service;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.exception.ExceptionMessage;
import com.example.junguniv_bb._core.security.JWTType;
import com.example.junguniv_bb._core.security.JwtProvider;
import com.example.junguniv_bb.domain._auth.dto.AuthJoinReqDTO;
import com.example.junguniv_bb.domain._auth.dto.AuthLoginReqDTO;
import com.example.junguniv_bb.domain._auth.dto.AuthLoginResDTO;
import com.example.junguniv_bb.domain.member.model.Member;
import com.example.junguniv_bb.domain.member.model.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; // SecurityConfig @Bean 등록
    private final JwtProvider jwtProvider;


    // 로그아웃
    public void logout(HttpServletResponse response) {
        // 액세스 토큰 만료 쿠키 설정
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0); // 만료 시간 0으로 설정하여 즉시 만료

        // 리프레시 토큰 만료 쿠키 설정
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0); // 만료 시간 0으로 설정하여 즉시 만료

        // 만료된 쿠키들을 응답에 추가
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        // 세션 초기화
        response.setHeader("Set-Cookie", "JSESSIONID=; Path=/; HttpOnly; Secure; Max-Age=0; SameSite=None");

        // 클라이언트에 세션스토리지 정리 지시
        response.setHeader("Clear-Site-Data", "\"storage\"");
    }

    public Long memberCount() {
        return memberRepository.count();
    }

    public Optional<Member> findByUserId(String userId) {
        return memberRepository.findByUserId(userId);
    }


    // 로그인
    public AuthLoginResDTO login(AuthLoginReqDTO reqDTO) {

        // userId로 Member 엔티티 불러오기
        // 만약 존재하지 않는다면 로그인 실패(코드 400)
        Member memberPS = memberRepository.findByUserId(reqDTO.userId())
                .orElseThrow(() -> new Exception400(ExceptionMessage.NOT_FOUND_ID.getMessage()));

        // 패스워드가 일치하지 않는 경우(코드 400)
        if (!passwordEncoder.matches(reqDTO.pwd(), memberPS.getPwd())) {
            throw new Exception400(ExceptionMessage.LOGIN_FAIL.getMessage());
        }

        // 액세스 토큰과 리프레시 토큰 모두 생성
        String accessToken = jwtProvider.createToken(memberPS, JWTType.ACCESS_TOKEN);
        String refreshToken = jwtProvider.createToken(memberPS, JWTType.REFRESH_TOKEN);

        // 두 토큰을 모두 포함하여 응답
        return new AuthLoginResDTO(memberPS, accessToken, refreshToken);

    }

    // 회원가입
    @Transactional
    public void join(AuthJoinReqDTO reqDTO) {

        // 패스워드 암호화
        String encodedPwd = passwordEncoder.encode(reqDTO.pwd());

        // 멤버 저장
        memberRepository.save(reqDTO.toEntity(encodedPwd));
    }

}
