package metaint.replanet.rest.org.repository;

import metaint.replanet.rest.auth.entity.MemberRole;
import metaint.replanet.rest.org.entity.Organization;
import metaint.replanet.rest.pay.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrgMemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "SELECT m.member_code, " +
            "m.member_email, " +
            "m.member_name, " +
            "m.join_date, " +
            "m.phone, " +
            "m.withdraw_date, " +
            "o.withdraw_req_date, " +
            "o.withdraw_reason " +
            "FROM tbl_member m " +
            "JOIN tbl_org o ON m.member_code = o.org_code " +
            "ORDER BY m.member_code DESC"
    , nativeQuery = true)
    List<Object[]> findAllByMemberRole(MemberRole memberRole);

    @Modifying
    @Query("UPDATE pay_member c SET c.withdraw = 'Y', c.withdrawDate = current_timestamp WHERE c.memberCode = :memberCode")
    int deleteOrgMemberByMemberCode(Long memberCode);
}
