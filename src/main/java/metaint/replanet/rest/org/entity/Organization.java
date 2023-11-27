package metaint.replanet.rest.org.entity;

import com.sun.istack.NotNull;
import lombok.*;
import metaint.replanet.rest.auth.entity.MemberRole;

import javax.persistence.*;
import java.time.LocalDateTime;

@ToString
@NoArgsConstructor
@Getter
@Entity(name = "organization")
@Table(name = "tbl_org")
public class Organization {
    @Id
    @Column(name = "org_code")
    @GeneratedValue
    private int orgCode;                // 기부처 코드
    @NotNull
    @Column(name = "org_id")
    private String orgId;               // 아이디
    @NotNull
    @Column(name = "org_password")
    private String orgPassword;         // 비밀번호
    @NotNull
    @Column(name = "org_name")
    private String orgName;             // 기부처명

    @Column(name = "org_tel")
    private String orgTel;              // 전화번호

    @Column(name = "org_description")
    private String orgDescription;      // 기부처 한줄소개

    @NonNull
    @Column(name = "join_dage")
   // @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime joinDate;     // 등록일자

    @Column(name = "withdraw_date")
    private LocalDateTime withdrawDate; // 삭제일자

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role")
    private MemberRole memberRole;          // 권환

    @Builder
    public Organization(int orgCode, String orgId, String orgPassword, String orgName, String orgTel, String orgDescription, LocalDateTime joinDate, LocalDateTime withdrawDate, MemberRole memberRole) {
        this.orgCode = orgCode;
        this.orgId = orgId;
        this.orgPassword = orgPassword;
        this.orgName = orgName;
        this.orgTel = orgTel;
        this.orgDescription = orgDescription;
        this.joinDate = joinDate;
        this.withdrawDate = withdrawDate;
        this.memberRole = memberRole;
    }

    public Organization orgPassword (String val){
        this.orgPassword = val;
        return this;
    }
}
