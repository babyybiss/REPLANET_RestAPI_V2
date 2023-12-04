package metaint.replanet.rest.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import metaint.replanet.rest.auth.entity.MemberRole;

import java.util.Date;

@Getter
@NoArgsConstructor
public class MemberPwRequestDto {

    private String email;
    private String phone;
    private String newPassword;

    @Builder
    public MemberPwRequestDto(String email, String phone, String newPassword) {
        this.email = email;
        this.phone = phone;
        this.newPassword = newPassword;
    }

}
