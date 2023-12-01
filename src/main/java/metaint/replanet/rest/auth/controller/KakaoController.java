package metaint.replanet.rest.auth.controller;

import lombok.extern.slf4j.Slf4j;
import metaint.replanet.rest.auth.config.OAuth2Config;
import metaint.replanet.rest.auth.dto.KakaoTokenRequest;
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
    public ResponseEntity<String> findMemberByKakaoTokenId(@RequestBody KakaoTokenRequest request) {

        log.info("[exchangeCodeForToken request] =================================================== ");
        log.info("[exchangeCodeForToken request] : " + request);
        String accessToken = request.getAccess_token();

        ClientRegistration clientRegistration = oAuth2Config.getKakaoClientRegistration();

        OAuth2UserRequest userRequest = new OAuth2UserRequest(
                clientRegistration, // 여기에 클라이언트 등록 정보가 들어가야 합니다.
                new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessToken, Instant.now(),
                        Instant.now().plusSeconds(request.getExpires_in()), Collections.emptySet()),
                Collections.emptyMap()
        );

        OAuth2User oAuth2User = principalOauth2UserService.loadUser(userRequest);
        log.info("[exchangeCodeForToken oAuth2User] : " + oAuth2User.toString());

        if (false) {
            // 이미 존재하는 회원이라면 로그인 처리
//            Member existingMember = optionalMember.get();
//            log.info("[exchangeCodeForToken] 기존 회원 조회: " + existingMember.getEmail());
            return null;
        } else {
            // 존재하지 않는 회원이라면 프론트엔드에 응답
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("필수 사항을 입력해주세요.");
        }
    }
}
