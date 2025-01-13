package com.example.junguniv_bb.domain.auth.controller;

import com.example.junguniv_bb._core.exception.Exception400;
import com.example.junguniv_bb._core.security.CustomUserDetails;
import com.example.junguniv_bb._core.util.APIUtils;
import com.example.junguniv_bb.domain.auth.dto.ReqJoinDTO;
import com.example.junguniv_bb.domain.auth.dto.ReqLoginDTO;
import com.example.junguniv_bb.domain.auth.dto.ResJoinDTO;
import com.example.junguniv_bb.domain.auth.dto.ResLoginDTO;
import com.example.junguniv_bb.domain.auth.service.AuthService;
import com.example.junguniv_bb.domain.member._enum.UserType;
import com.example.junguniv_bb.domain.member.model.Member;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import com.example.junguniv_bb._core.security.JwtProvider;
import com.example.junguniv_bb._core.util.RequestUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthRestController {

    /* DI */
    private final AuthenticationManager authenticationManager;    
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthService authService;

        // 로그아웃
    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        // 모든 쿠키 삭제
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }

        // 서버 측 로그아웃 처리
        authService.logout(response);
        
        // CORS 헤더 설정
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        
        // 웹 요청인 경우 로그인 페이지로 리다이렉트
        if (!RequestUtils.isApiRequest(request)) {
            response.sendRedirect("/login");
        } else {
            // API 요청인 경우 JSON 응답
            response.setContentType("application/json");
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            response.getWriter().write("{\"message\":\"로그아웃 되었습니다.\"}");
        }
    }

    /* 로그인 */
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody @Valid ReqLoginDTO reqDTO,
            Errors errors) {
        try {
            if (errors.hasErrors()) {
                log.warn(errors.getAllErrors().get(0).getDefaultMessage());
                throw new Exception400(errors.getAllErrors().get(0).getDefaultMessage());
            }

            // Spring Security의 인증 처리
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(reqDTO.userId(), reqDTO.pwd())
                );

                // 인증 성공 시 JWT 토큰 생성 및 응답
                ResLoginDTO resDTO = authService.login(reqDTO);

                // JWT를 세션에 설정
                setJwtSession(resDTO.token().accessToken(), resDTO.token().refreshToken(), request);

                // // 사용자 타입 및 약관 동의 여부 확인
                // CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                // Member member = userDetails.getMember();
                // UserType userType = member.getUserType();
                //
                // log.info("로그인 성공 - 사용자: {}, 타입: {}", member.getUserId(), userType);

                // // 학생인 경우 약관 동의 여부와 기간 확인
                // if (userType == UserType.STUDENT) {
                //     LocalDateTime agreeDate = member.getAgreeDate();
                //     // 약관동의일이 없거나 2년이 지났다면 약관동의 페이지로 이동
                //     if (agreeDate == null || ChronoUnit.YEARS.between(agreeDate, LocalDateTime.now()) >= 2) {
                //         log.info("약관 동의 필요 - 사용자: {}, 마지막 동의일: {}", member.getUserId(), agreeDate);
                //         // 토큰과 함께 약관 동의 필요 정보 전달
                //         return ResponseEntity.ok(APIUtils.success(Map.of(
                //                 "token", resDTO.token(),
                //                 "member", resDTO.member(),
                //                 "needAgree", true,
                //                 "redirectUrl", "/nGsmart/student/member/agree-page",
                //                 "message", "약관 동의가 필요합니다."
                //         )));
                //     }
                // }

                return ResponseEntity.ok(APIUtils.success(resDTO));

            } catch (AuthenticationException e) {
                log.error("인증 실패", e);
                // 예외 메시지가 /id-stop으로 시작하는 경우 계정 정지 상태
                if (e.getMessage() != null && e.getMessage().startsWith("/id-stop")) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(APIUtils.error("계정이 정지되었습니다. 관리자에게 문의하세요.", HttpStatus.UNAUTHORIZED));
                }
                // 그 외의 인증 실패
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(APIUtils.error("아이디 또는 비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED));
            }

        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(APIUtils.error("로그인 처리 중 오류가 발생했습니다.", HttpStatus.UNAUTHORIZED));
        }
    }

    // 회원가입 - 김대홍
    @PostMapping("/join")
    public ResponseEntity<?> join(@ModelAttribute @Valid ReqJoinDTO reqDTO, Errors errors) {

        // 에러 로그 확인
        if (errors.hasErrors()) {
            log.warn(errors.getAllErrors()
                    .get(0)
                    .getDefaultMessage());
            throw new Exception400(errors.getAllErrors()
                    .get(0)
                    .getDefaultMessage());
        }

        ResJoinDTO resDTO = authService.join(reqDTO);

        return ResponseEntity.ok(APIUtils.success(resDTO));
    }

    // 세션에 JWT 저장
    private void setJwtSession(String accessToken, String refreshToken, HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        session.setAttribute("accessToken", accessToken);
        session.setAttribute("refreshToken", refreshToken);  // 리프레시 토큰도 세션에 저장
        session.setMaxInactiveInterval(1 * 60 * 60); // 세션 만료 시간 설정 (1시간)
    }

    @PostMapping("/refresh-access-token")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
        String currentToken = request.getHeader("Authorization");
        String refreshToken = (String) request.getSession().getAttribute("refreshToken");

        if (currentToken != null && currentToken.startsWith("Bearer ") && refreshToken != null) {
            try {
                if (!jwtProvider.validateRefreshToken(refreshToken)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(APIUtils.error("리프레시 토큰이 만료되었습니다. 다시 로그인해주세요.", HttpStatus.UNAUTHORIZED));
                }

                String newAccessToken = jwtProvider.refreshAccessToken(refreshToken);

                // 세션에 새 액세스 토큰 저장 및 세션 만료 시간 갱신
                HttpSession session = request.getSession(true);
                session.setAttribute("accessToken", newAccessToken);
                session.setAttribute("refreshToken", refreshToken);  // 리프레시 토큰도 다시 설정
                session.setMaxInactiveInterval(1 * 60 * 60);  // 세션 만료 시간 1시간으로 갱신

                return ResponseEntity.ok(Map.of("token", newAccessToken));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(APIUtils.error("토큰 갱신에 실패했습니다.", HttpStatus.UNAUTHORIZED));
            }
        }
        return ResponseEntity.badRequest().body(APIUtils.error("유효하지 않은 토큰입니다.", HttpStatus.BAD_REQUEST));
    }
}