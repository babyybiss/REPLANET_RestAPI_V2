package metaint.replanet.rest.auth.dto;

import metaint.replanet.rest.auth.entity.MemberRole;
import metaint.replanet.rest.auth.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

    @NotEmpty(message = "필수 입력 항목입니다.")
    private String email;

    @NotEmpty(message = "필수 입력 항목입니다.")
    private String password;

    @NotEmpty(message = "필수 입력 항목입니다.")
    private String memberName;

    @NotEmpty(message = "필수 입력 항목입니다.")
    private String phone;

    private String memberRole;
    private String kakaoTokenId;
    private String providerId;
    private String accessToken;


    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .memberName(memberName)
                .phone(phone)
                .memberRole(MemberRole.ROLE_USER)
                .build();
    }

    public Member toOrgMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .tempPwd(passwordEncoder.encode(password))
                .memberName(memberName)
                .phone(phone)
                .memberRole(MemberRole.ROLE_ORG)
                .build();
    }

    public Member toSocialMember(PasswordEncoder passwordEncoder, String kakaoTokenId) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .memberName(memberName)
                .phone(phone)
                .memberRole(MemberRole.ROLE_USER)
                .provider("KAKAO")
                .providerId(kakaoTokenId)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }

    public UsernamePasswordAuthenticationToken toSocialAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, "");
    }
}
