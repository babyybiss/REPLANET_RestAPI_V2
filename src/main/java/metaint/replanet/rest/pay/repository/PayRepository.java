package metaint.replanet.rest.pay.repository;

import metaint.replanet.rest.pay.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PayRepository extends JpaRepository<Pay, Integer> {

    Pay findByPayCode(int payCode);

    Pay findByRefDonation_DonationCode(int donationCode);
}
