package metaint.replanet.rest.auth.controller;

import lombok.extern.slf4j.Slf4j;
import metaint.replanet.rest.auth.config.OAuth2Config;
import metaint.replanet.rest.auth.dto.KakaoTokenRequest;
import metaint.replanet.rest.auth.oauth2.PrincipalDetails;
import metaint.replanet.rest.auth.oauth2.PrincipalOauth2UserService;
import metaint.replanet.rest.auth.repository.MemberRepository;
import metaint.replanet.rest.auth.entity.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/kakaologin")
@CrossOrigin(origins = "http://localhost:3000")
public class KakaoController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PrincipalOauth2UserService principalOauth2UserService;
    private final OAuth2Config oAuth2Config;

    public KakaoController(MemberRepository memberRepository, PasswordEncoder passwordEncoder, PrincipalOauth2UserService principalOauth2UserService, ClientRegistrationRepository clientRegistrationRepository, OAuth2Config oAuth2Config) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.principalOauth2UserService = principalOauth2UserService;
        this.oAuth2Config = oAuth2Config;
    }

    @PostMapping("/findMember")
    public ResponseEntity<Map<String, Object>> findMemberByKakaoTokenId(@RequestBody KakaoTokenRequest request) {
        log.info("[findMemberByKakaoTokenId request] =================================================== ");
        log.info("[findMemberByKakaoTokenId request] : " + request);
        String accessToken = request.getAccess_token();

        ClientRegistration clientRegistration = oAuth2Config.getKakaoClientRegistration();

        OAuth2UserRequest userRequest = new OAuth2UserRequest(
                clientRegistration,
                new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessToken, Instant.now(),
                        Instant.now().plusSeconds(request.getExpires_in()), Collections.emptySet()),
                Collections.emptyMap()
        );

        OAuth2User oAuth2User = principalOauth2UserService.loadUser(userRequest);
        PrincipalDetails principalDetails = (PrincipalDetails) oAuth2User;

        Map<String, Object> attributes = principalDetails.getAttributes();
        log.info("[findMemberByKakaoTokenId] attributes : " + attributes);
        Long providerId = (Long) attributes.get("id");
        log.info("[findMemberByKakaoTokenId] providerId : " + providerId);
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        String email = (String) kakao_account.get("email");
        log.info("[findMemberByKakaoTokenId] email : " + email);

        Member foundMember = memberRepository.findByProviderId(String.valueOf(providerId));

        if (foundMember != null) {
            log.info("[findMemberByKakaoTokenId] 기존 회원 발견");
            // 이미 존재하는 회원이라면 로그인 처리
            log.info("[findMemberByKakaoTokenId] 기존 회원 조회: " + foundMember);
            // 여기에 로그인 처리 로직을 추가하면 됩니다.
            return ResponseEntity.ok(Map.of("status", "success", "message", "로그인 성공", "email", email, "providerId", providerId));
        } else {
            log.info("[findMemberByKakaoTokenId] 신규 카카오 소셜 로그인 회원");
            // 존재하지 않는 회원이라면 프론트엔드에 응답
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "필수 사항을 입력해주세요.");
            response.put("redirectTo", "/SocialSignUp"); // 프론트에서 필요한 페이지로 리다이렉트
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

}
