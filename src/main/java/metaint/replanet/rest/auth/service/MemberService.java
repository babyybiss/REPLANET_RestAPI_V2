package metaint.replanet.rest.auth.service;

import com.twilio.rest.api.v2010.Account;
import lombok.extern.slf4j.Slf4j;
import metaint.replanet.rest.auth.dto.MemberDto;
import metaint.replanet.rest.auth.dto.MemberPwRequestDto;
import metaint.replanet.rest.auth.dto.MemberPwResponseDto;
import metaint.replanet.rest.auth.dto.MemberResponseDto;
import metaint.replanet.rest.auth.entity.Member;
import metaint.replanet.rest.auth.exception.BadRequestException;
import metaint.replanet.rest.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import metaint.replanet.rest.auth.util.SecurityUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberResponseDto getMyInfoBySecurity() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

    @Transactional
    public MemberResponseDto changeMemberPassword(String exPassword, String newPassword) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        if (!passwordEncoder.matches(exPassword, member.getPassword())) {
            throw new RuntimeException("비밀번호가 맞지 않습니다");
        }
        member.setPassword(passwordEncoder.encode((newPassword)));
        return MemberResponseDto.of(memberRepository.save(member));
    }

    @Transactional
    public boolean existsByEmail(String email) {
        log.info(email);
        return memberRepository.existsByEmail(email);

    }


    @Transactional
    public boolean existsByPhone(String phone) {
        log.info(phone);
        return memberRepository.existsByPhone(phone);

    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }


    public Member findEmailByPhone(String phone) {
        log.info(phone);
        return memberRepository.findEmailByPhone(phone);
    }

    public Member findPhoneByEmail(String email) {
        log.info(email);
        return memberRepository.findPhoneByEmail(email);
    }


    public void memberCheck(MemberPwRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail()).get();
        if (member == null && !member.getPhone().equals(requestDto.getPhone())) {
            throw new BadRequestException();
        } else {

        }
    }

//    @Transactional
//    public void modifyPassword(String email, String password, MemberDto memberDto) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
//    }

    @Transactional
    public MemberPwResponseDto updatePassword(String email, String newPassword) {
//      Member member = memberRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
//      member.setPassword(passwordEncoder.encode(newPassword));
//      memberRepository.save(member);

        System.out.println("aaa" + newPassword);
        Member member = memberRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
        member.setPassword(passwordEncoder.encode((newPassword)));
        return MemberPwResponseDto.of(memberRepository.save(member));


    }

}