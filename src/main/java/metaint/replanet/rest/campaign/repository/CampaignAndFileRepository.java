package metaint.replanet.rest.campaign.repository;

import metaint.replanet.rest.campaign.entity.CampaignAndFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignAndFileRepository extends JpaRepository<CampaignAndFile, Integer> {
}
