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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    private final CustomUserDetailsService customUserDetailsService;

    public MemberResponseDto signup(MemberRequestDto requestDto, String memberRole, String kakaoTokenId) {
        log.info("[signup() memberRole] : ", memberRole);
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        if (memberRole == null && kakaoTokenId == null) {
            log.info("[signup()] 비회원 가입 진행 ======================");

            Member member = requestDto.toMember(passwordEncoder);
            return MemberResponseDto.of(memberRepository.save(member));

        } else if (memberRole == null && kakaoTokenId != null){
            log.info("[signup()] 소셜로그인 비회원 가입 진행 ======================");

            Member member = requestDto.toSocialMember(passwordEncoder, kakaoTokenId);

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
        log.info("[login()] AuthService 진입 ==================== ");

        Optional<Member> optionalMember = memberRepository.findByEmail(requestDto.getEmail());

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            log.info("[login()] member.getMemberRole() : " + member.getMemberRole());
            String memberRole = String.valueOf(member.getMemberRole());

            if (memberRole.equals("ROLE_ORG")) {
                log.info("[login()] 기부처회원 진입 =========================");
                Optional<Organization> organization = authOrgRepository.findById(Math.toIntExact(member.getMemberCode()));

                if(organization.isPresent()) {
                    Organization org = organization.get();

                    if (member.getWithdraw().equals("Y")) {
                        return handleWithdrawMember(member, requestDto);
                    }

                    if (org.getWithdrawReason() != null && !org.getWithdrawReason().isEmpty()) {
                        log.info("[login()] 탈퇴 신청한 ROLE_ORG 회원 ====================");
                        log.info("[login()] org.getWithdrawReason().isEmpty() : " + org.getWithdrawReason());
                        return TokenDto.builder().message("탈퇴 신청 상태에서는 로그인 하실 수 없습니다.").build();
                    }
                }
            }

            if (!passwordEncoder.matches(requestDto.getPassword(), member.getTempPwd())) {
                return handleWithdrawMember(member, requestDto);
            }

            UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
            Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

            member.setTempPwd(null);
            memberRepository.save(member);

            return tokenProvider.generateTokenDto(authentication, true);
        } else {
            return handleMemberNotFound();
        }
    }

    private TokenDto handleWithdrawMember(Member member, MemberRequestDto requestDto) {
        if ("N".equalsIgnoreCase(member.getWithdraw())) {
            log.info("[login()] 탈퇴하지 않은 멤버 로그인 ==================== ");
            log.info("[login()] member.getWithdraw(): " + member.getWithdraw());
            UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
            Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
            return tokenProvider.generateTokenDto(authentication, false);
        } else if ("Y".equalsIgnoreCase(member.getWithdraw())) {
            log.info("[login()] 탈퇴 멤버 로그인 시도 ==================== ");
            log.info("[login()] member.getWithdraw(): " + member.getWithdraw());
            return TokenDto.builder().message("찾을 수 없는 회원입니다.").build();
        } else {
            log.info("[login()] 알 수 없는 오류");
            log.info("[login()] member.getWithdraw(): " + member.getWithdraw());
            return TokenDto.builder().message("알 수 없는 오류가 발생했습니다.").build();
        }
    }

    private TokenDto handleMemberNotFound() {
        log.info("[login()] 해당하는 회원이 존재하지 않습니다.");
        return TokenDto.builder().message("해당하는 회원이 존재하지 않습니다.").build();
    }


    public TokenDto socialLogin(MemberRequestDto requestDto) {
        log.info("[socialLogin()] 소셜로그인 진행 ======================");
        String accessToken = requestDto.getAccessToken();
        String email = requestDto.getEmail();
        log.info("[socialLogin() accessToken] : " + accessToken);
        log.info("[socialLogin() email] : " + email);

        Authentication authentication = authenticateUserWithSocialToken(accessToken, email);

        return tokenProvider.generateTokenDto(authentication, false);
    }

    private Authentication authenticateUserWithSocialToken(String accessToken, String email) {
        log.info("[authenticateUserWithSocialToken()] ===========================");
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}