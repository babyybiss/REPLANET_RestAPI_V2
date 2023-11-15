package metaint.replanet.rest.campaign.repository;

import metaint.replanet.rest.campaign.entity.CampaignAndFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CampaignAndFileRepository extends JpaRepository<CampaignAndFile, Integer> {
    List<CampaignAndFile> findByEndDateAfter(LocalDateTime currentDate);

    List<CampaignAndFile> findByEndDateBefore(LocalDateTime currentDate);
}
