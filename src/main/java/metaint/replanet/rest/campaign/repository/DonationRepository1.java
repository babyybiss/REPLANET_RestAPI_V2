package metaint.replanet.rest.campaign.repository;

import metaint.replanet.rest.campaign.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationRepository1 extends JpaRepository<Donation, Integer> {
    List<Donation> findByCampaignDescription_CampaignCode(int campaignCode);


}
