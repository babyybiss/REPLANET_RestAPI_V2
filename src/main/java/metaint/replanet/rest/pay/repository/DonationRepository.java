package metaint.replanet.rest.pay.repository;

import metaint.replanet.rest.pay.entity.Donation;
import metaint.replanet.rest.pay.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface DonationRepository extends JpaRepository<Donation, Integer> {

    Member findByRefMember(String memberCode);

    List<Donation> findAllByDonationDateTimeBetween(LocalDateTime stardDateTime, LocalDateTime endDateTime);
}
