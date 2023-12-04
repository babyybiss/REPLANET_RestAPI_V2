package metaint.replanet.rest.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import metaint.replanet.rest.auth.entity.Member;

@Getter
@NoArgsConstructor
public class MemberPwResponseDto {

    private String email;
    private String newPassword;

    @Builder
    public MemberPwResponseDto(String email, String newPassword) {
        this.email = email;
        this.newPassword = newPassword;
    }

    public static MemberPwResponseDto of(Member member) {
        return MemberPwResponseDto.builder()
                .email(member.getEmail())
                .newPassword(member.getPassword())
                .build();
    }

}
