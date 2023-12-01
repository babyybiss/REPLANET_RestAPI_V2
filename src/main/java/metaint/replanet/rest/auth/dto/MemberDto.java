package metaint.replanet.rest.auth.dto;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import metaint.replanet.rest.auth.entity.MemberRole;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
public class MemberDto {

    private Long memberCode;

    private String email;

    private String memberName;

    private String password;

    private String phone;

    private Date joinDate;

    private MemberRole memberRole;

    private String withdraw;

    private Date withdrawDate;

    private int currentPoint;

    private String provider;

    private String providerId;

    @Builder
    public MemberDto(Long memberCode, String email, String memberName, String password, String phone, Date joinDate, MemberRole memberRole, String withdraw, Date withdrawDate, int currentPoint,
                     String provider, String providerId) {
        this.memberCode = memberCode;
        this.email = email;
        this.memberName = memberName;
        this.password = password;
        this.phone = phone;
        this.joinDate = joinDate;
        this.memberRole = memberRole;
        this.withdraw = withdraw;
        this.withdrawDate = withdrawDate;
        this.currentPoint = currentPoint;
        this.provider = provider;
        this.providerId = providerId;
    }

    public MemberDto(String email, String memberName, MemberRole memberRole) {
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
