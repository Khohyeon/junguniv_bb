package com.example.junguniv_bb._core.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.junguniv_bb._core.exception.Exception401;
import com.example.junguniv_bb.domain.member._enum.UserType;
import com.example.junguniv_bb.domain.member.model.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;


// 토큰 발행 및 유효성 확인 유틸
@Component
public class JwtProvider {

    private static final Long ACCESS_EXP = 2400000L * 60 * 60 * 1; // 액세스 토큰 생명주기
    private static final Long REFRESH_EXP = 1000L * 60 * 60 * 72; // 리프레시 토큰 생명주기
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";
    private static final String SECRET_KEY = "secret_key1234567890"; // TODO 시크릿키 환경변수로 등록 필수!!

    private final CustomUserDetailsService userDetailsService;

    public JwtProvider(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // 토큰 생성 메서드
    public String createToken(Member member, JWTType jwtType) {
        // JWT Builder 시작
        var tokenBuilder = JWT.create()
                .withSubject(String.valueOf(member.getMemberIdx()))
                .withExpiresAt(makeExpiresAt(jwtType))
                .withClaim("userId", member.getUserId())
                .withClaim("userType", member.getUserType().toString())
                .withClaim("token-type", jwtType.name());
        
        // authLevel이 있는 경우에만 claim에 추가 (관리자만 있음, 학생, 교강사는 없음)
        if (member.getAuthLevel() != null) {
            tokenBuilder.withClaim("authLevel", member.getAuthLevel().toString());
        }

        // 토큰 생성 및 반환
        String jwt = tokenBuilder.sign(Algorithm.HMAC512(SECRET_KEY));
        return TOKEN_PREFIX + jwt;
    }

    // 토큰 기한 설정
    private Date makeExpiresAt(JWTType jwtType) {
        if (jwtType.equals(JWTType.ACCESS_TOKEN)) {
            return new Date(System.currentTimeMillis() + ACCESS_EXP);
        } else if (jwtType.equals(JWTType.REFRESH_TOKEN)) {
            return new Date(System.currentTimeMillis() + REFRESH_EXP);
        } else {
            throw new IllegalArgumentException("올바르지 않은 토큰 타입입니다.");
        }
    }

    // 토큰 디코딩
    public static DecodedJWT verify(String jwt) throws SignatureVerificationException, TokenExpiredException {

        if (jwt.startsWith(TOKEN_PREFIX)) {
            jwt = jwt.substring(TOKEN_PREFIX.length());
        }

        return JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(jwt);
    }

    public boolean validateToken(String jwt) {
        try {
            var verify = verify(jwt);

            if (new Date().after(verify.getExpiresAt())) {
                throw new Exception401("세션이 만료되었습니다.");
            }

            if (!verify.getClaim("token-type").asString().equals(JWTType.ACCESS_TOKEN.name())) {
                throw new Exception401("올바르지 않은 토큰입니다.");
            }

        } catch (JWTVerificationException e) {
            return false;
        }
        return true;
    }

    public Authentication getAuthentication(String token) {

        var decodedJWT = verify(token);
        var userId = decodedJWT.getClaim("userId").asString();
        var userDetails = userDetailsService.loadUserByUsername(userId);

        // JWT에서 member.authLevel도 함께 가져와서 필요시 활용 가능
        var authLevel = decodedJWT.getClaim("authLevel").asString();

        // Authentication 객체를 반환 (사용자 세부 정보와 권한 포함)
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /* 액세스 토큰  갱신 */
    public String refreshAccessToken(String refreshToken) {
        try {
            // 1. refreshToken 검증
            DecodedJWT decodedJWT = verify(refreshToken);

            // 2. refreshToken이 유효하면(72시간 이내) 새로운 액세스 토큰 발급
            String userId = decodedJWT.getClaim("userId").asString();
            String userType = decodedJWT.getClaim("userType").asString();
            
            // authLevel claim이 없을 수 있으므로 null 체크 추가
            String authLevel = decodedJWT.getClaim("authLevel").asString();
            
            Member member = Member.builder()
                    .userId(userId)
                    .userType(UserType.valueOf(userType))
                    .authLevel(authLevel != null ? Long.parseLong(authLevel) : null)  // null일 수 있음
                    .build();

            // 새로운 액세스 토큰만 생성
            return createToken(member, JWTType.ACCESS_TOKEN);
        } catch (Exception e) {
            throw new Exception401("토큰 갱신에 실패했습니다.");
        }
    }

    // 토큰의 남은 유효시간을 확인하는 메서드
    public long getTokenTimeToLive(String token) {
        try {
            DecodedJWT jwt = verify(token);
            Date expiresAt = jwt.getExpiresAt();
            return (expiresAt.getTime() - System.currentTimeMillis()) / 1000; // 초 단위로 반환
        } catch (Exception e) {
            return -1;
        }
    }

    // 리프레시 토큰 검증
    public boolean validateRefreshToken(String refreshToken) {
        try {
            DecodedJWT jwt = verify(refreshToken);
            
            // 리프레시 토큰 만료 시간 체크 (72시간)
            if (new Date().after(jwt.getExpiresAt())) {
                return false;
            }

            // 토큰 타입 확인
            return jwt.getClaim("token-type").asString().equals(JWTType.REFRESH_TOKEN.name());
        } catch (Exception e) {
            return false;
        }
    }
}

