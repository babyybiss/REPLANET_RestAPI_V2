package metaint.replanet.rest.org.repository;

import metaint.replanet.rest.auth.entity.MemberRole;
import metaint.replanet.rest.org.entity.Organization;
import metaint.replanet.rest.pay.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrgMemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByMemberRole(MemberRole memberRole);

    @Modifying
    @Query("UPDATE pay_member c SET c.withdraw = 'Y', c.withdrawDate = current_timestamp WHERE c.memberCode = :memberCode")
    int deleteOrgMemberByMemberCode(Long memberCode);
}
