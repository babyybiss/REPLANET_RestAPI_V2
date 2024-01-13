package metaint.replanet.rest.reviews.repository;

import metaint.replanet.rest.reviews.dto.CombineReviewDTO;
import metaint.replanet.rest.reviews.entity.Campaign;
import metaint.replanet.rest.reviews.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CampaignReviewRepository extends JpaRepository<Campaign, Long> {

    @Query("SELECT c FROM reviewPkg_entityCampaign c ORDER BY c.campaignCode DESC")
    List<Campaign> findAllOrderedByCampaignCodeDesc();

    @Query(value = "SELECT c.*, o.* FROM tbl_campaign_description c LEFT JOIN tbl_org o ON c.org_code = o.org_code WHERE c.campaign_code NOT IN (SELECT r.campaign_code FROM tbl_review r WHERE r.campaign_code IS NOT NULL );", nativeQuery = true)
    List<Campaign> findUnassociatedCampaigns();

    @Query(value = "SELECT c.*, o.* FROM tbl_campaign_description c LEFT JOIN tbl_org o ON c.org_code = o.org_code WHERE c.org_code = :orgCode AND c.campaign_code NOT IN (SELECT r.campaign_code FROM tbl_review r WHERE r.campaign_code IS NOT NULL );", nativeQuery = true)
    List<Campaign> findUnassociatedCampaignsByOrgCode(@Param("orgCode") Long orgCode);
}
