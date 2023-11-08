package metaint.replanet.rest.chart.repository;

import metaint.replanet.rest.chart.entity.CampaignDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChartRepository extends JpaRepository<CampaignDescription, Integer> {

    @Query(value = "SELECT COUNT(campaign_code) FROM tbl_campaign_description"
            , nativeQuery = true)
    public int countAllCampaign();


    @Query(value = "SELECT DISTINCT (campaign_category) FROM tbl_campaign_description"
            , nativeQuery = true)
    public List<String> findAllCampaingCategory();


}
