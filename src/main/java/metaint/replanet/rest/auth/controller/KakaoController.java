package metaint.replanet.rest.auth.controller;

import lombok.extern.slf4j.Slf4j;
import metaint.replanet.rest.auth.dto.MemberResponseDto;
import metaint.replanet.rest.auth.repository.MemberRepository;
import metaint.replanet.rest.pay.entity.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/kakaologin")
public class KakaoController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public KakaoController(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/findMember")
    public ResponseEntity<Member> findMemberByKakaoTokenId(@RequestBody Map<String, String> requestBody) {

        log.info("[exchangeCodeForToken requestBody] : " + requestBody);

        String kakaoTokenId = requestBody.get("kakaoTokenId");
        String email = requestBody.get("email");
        log.info("[exchangeCodeForToken kakaoTokenId] : " + kakaoTokenId);
        log.info("[exchangeCodeForToken email] : " + email);

        Member foundMember = memberRepository.findByProviderId(kakaoTokenId);

        if (foundMember != null) {
            return ResponseEntity.ok(foundMember);
        } else {
            // provider_id를 찾았는데 없을때 프론트 가서 다시 입력받을거 받아서 다른 api로 호출해서 member 생성할 예정
        }
        return null;
    }
}
