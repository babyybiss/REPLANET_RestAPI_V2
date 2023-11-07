package metaint.replanet.rest.pay.repository;

import metaint.replanet.rest.pay.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DonationRepository extends JpaRepository<Donation, Integer> {

}
