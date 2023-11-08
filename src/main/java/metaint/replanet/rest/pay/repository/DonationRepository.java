package metaint.replanet.rest.pay.repository;

import metaint.replanet.rest.pay.entity.Donation;
import metaint.replanet.rest.pay.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DonationRepository extends JpaRepository<Donation, Integer> {

    Member findByRefMember(String memberCode);
}
