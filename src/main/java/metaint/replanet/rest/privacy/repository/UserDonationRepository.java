package metaint.replanet.rest.privacy.repository;

import metaint.replanet.rest.privacy.entity.Donation;
import metaint.replanet.rest.privacy.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDonationRepository extends JpaRepository<Donation, Integer> {

    List<Donation> findByMemberCode(int memberCode);
}
