package metaint.replanet.rest.auth.service;

import lombok.extern.slf4j.Slf4j;
import metaint.replanet.rest.auth.dto.MemberRequestDto;
import metaint.replanet.rest.auth.dto.MemberResponseDto;
import metaint.replanet.rest.auth.dto.TokenDto;

import metaint.replanet.rest.auth.entity.Member;

import metaint.replanet.rest.auth.jwt.TokenProvider;
import metaint.replanet.rest.auth.repository.AuthOrgRepository;
import metaint.replanet.rest.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import metaint.replanet.rest.org.entity.Organization;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final MemberRepository memberRepository;
    private final AuthOrgRepository authOrgRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public MemberResponseDto signup(MemberRequestDto requestDto, String memberRole) {
        log.info("[signup() memberRole] : ", memberRole);
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        if (memberRole == null) {
            log.info("[signup()] 비회원 가입 진행 ======================");

            Member member = requestDto.toMember(passwordEncoder);
            return MemberResponseDto.of(memberRepository.save(member));

        } else if ("ROLE_ADMIN".equals(memberRole)) {
            log.info("[signup()] 기부처 가입 진행 ======================");

            Member member = requestDto.toOrgMember(passwordEncoder);
            Member savedmember = memberRepository.save(member);

            int memberCode = Math.toIntExact(savedmember.getMemberCode());
            log.info("[signup()] memberCode : " + memberCode);

            Organization organization = Organization.builder()
                                                    .orgCode(memberCode)
                                                    .build();
            authOrgRepository.save(organization);

            return MemberResponseDto.of(savedmember);

        }

        log.error("[signup()] 가입 실패");
        return null;
    }

    public TokenDto login(MemberRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();

        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        return tokenProvider.generateTokenDto(authentication);
    }

}