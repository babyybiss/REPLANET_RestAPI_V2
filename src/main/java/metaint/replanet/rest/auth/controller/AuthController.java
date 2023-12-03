package metaint.replanet.rest.auth.controller;


import lombok.extern.slf4j.Slf4j;
import metaint.replanet.rest.auth.dto.MemberRequestDto;
import metaint.replanet.rest.auth.dto.MemberResponseDto;
import metaint.replanet.rest.auth.dto.TokenDto;

import metaint.replanet.rest.auth.jwt.CustomUserDetails;
import metaint.replanet.rest.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto requestDto) {
        log.info("[/signup memberRole] : " + requestDto.getMemberRole());

        String memberRole = requestDto.getMemberRole();

        if ("ROLE_ADMIN".equals(memberRole)) {
            // 여기서 ROLE_ADMIN으로 확인되면 기부처를 등록하는 signup이기 때문에 ROLE_ORG로 세팅해주는 로직을 짠다
            log.info("[signup()] 기부처 가입 진행 ======================");
            return ResponseEntity.ok(authService.signup(requestDto, memberRole, null));
        } else if (memberRole == null) {
            // 나머지 memberRole이 없는 경우 비회원이기에 ROLE_USER 디폴트로 가입 시키는 로직으로 넘긴다.
            log.info("[signup()] 비회원 가입 진행 ======================");
            return ResponseEntity.ok(authService.signup(requestDto, null, null));
        }

        log.error("[signup()] 가입실패 ======================");
        return null;
    }

    @PostMapping("/socialSignup")
    public ResponseEntity<MemberResponseDto> socialSignup(@RequestBody MemberRequestDto requestDto) {
        log.info("[/socialSignup memberRole] : " + requestDto.getMemberRole());
        log.info("[/socialSignup kakaoTokenId] : " + requestDto.getKakaoTokenId());

        String memberRole = requestDto.getMemberRole();
        String kakaoTokenId = requestDto.getKakaoTokenId();

        if (memberRole == null) {
            // 나머지 memberRole이 없는 경우 비회원이기에 ROLE_USER 디폴트로 가입 시키는 로직으로 넘긴다.
            log.info("[signup()] 비회원 소셜로그인 가입 진행 ======================");
            return ResponseEntity.ok(authService.signup(requestDto, null, kakaoTokenId));
        }

        log.error("[signup()] 가입실패 ======================");
        return null;
    }

    @CrossOrigin(origins = "http://localhost:3000", exposedHeaders = "Authorization")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberRequestDto requestDto,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(authService.login(requestDto));
    }
}