package metaint.replanet.rest.pay.repository;

import metaint.replanet.rest.pay.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayMemberRepository extends JpaRepository<Member, Long> {
    Member findByMemberCode(Long memberId);
}
