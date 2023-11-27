package metaint.replanet.rest.org.dto;

import lombok.*;
import metaint.replanet.rest.auth.entity.MemberRole;
import metaint.replanet.rest.org.entity.Organization;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrgRequestDTO {

    private int orgCode;                // 기부처 코드
    @NotEmpty(message = "필수 입력 항목입니다.")
    private String orgId;               // 아이디
    @NotEmpty(message = "필수 입력 항목입니다.")
    private String orgPassword;         // 비밀번호
    @NotEmpty(message = "필수 입력 항목입니다.")
    private String orgName;             // 기부처명
    private String orgTel;              // 전화번호
    private String orgDescription;      // 기부처 한줄소개
    private LocalDateTime joinDate;     // 등록일자
    private LocalDateTime withdrawDate; // 삭제일자


    public Organization toOrganization(PasswordEncoder passwordEncoder){
        return Organization.builder()
                .orgId(orgId)
                .orgPassword(orgPassword)
                .orgName(orgName)
                .orgTel(orgTel)
                .orgDescription(orgDescription)
                .memberRole(MemberRole.ROLE_ORG)
                .build();
    }

    public OrgRequestDTO(String orgId, String orgPassword, String orgName) {
        this.orgId = orgId;
        this.orgPassword = orgPassword;
        this.orgName = orgName;
    }
}
