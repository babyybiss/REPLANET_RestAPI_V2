package metaint.replanet.rest.campaign.repository;

import metaint.replanet.rest.campaign.entity.CampaignDescFile;
import metaint.replanet.rest.campaign.entity.CampaignDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignFileRepository extends JpaRepository<CampaignDescFile, Integer> {

    @Query(value = "DELETE FROM tbl_campaign_desc_file WHERE campaign_code = :campaignCode", nativeQuery = true)
    void deleteByCampaignCode(int campaignCode);

    List<CampaignDescFile> findByCampaignCodeCampaignCode(int campaignCode);

    void deleteByCampaignCodeCampaignCode(int campaignCode);
}
