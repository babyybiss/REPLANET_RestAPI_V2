package metaint.replanet.rest.campaign.repository;

import metaint.replanet.rest.campaign.entity.Campaign;
import metaint.replanet.rest.campaign.entity.CampaignDescription;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Integer> {


    List<Campaign> findByEndDateAfter(LocalDateTime currentDate);

    List<Campaign> findByEndDateBefore(LocalDateTime currentDate);

    @Query(value = "SELECT * " +
            "FROM tbl_campaign_description campaign " +
            "LEFT OUTER JOIN " +
            "tbl_campaign_desc_file file " +
            "ON campaign.campaign_code = file.campaign_code " +
            "WHERE campaign.end_date >= :currentDate ORDER BY campaign.end_date asc", nativeQuery = true)
    List<Campaign> findCampaignSort(LocalDateTime currentDate);
    List<Campaign> findByCampaignCode(int campaignCode);
    void deleteByCampaignCode(int campaignCode);

    @Query(value = "SELECT *  FROM " +
            "tbl_campaign_description d " +
            "left outer join " +
            "tbl_campaign_desc_file cf " +
            "on d.campaign_code = cf.campaign_code " +
            "left outer join " +
            "tbl_org o " +
            "on d.org_code = o.org_code " +
            "left outer join " +
            "tbl_member m " +
            "on o.org_code = m.member_code " +
            "WHERE d.end_date > now() AND o.org_code = :orgCode "
    ,nativeQuery = true)
    List<Campaign> findByOrgCode(int orgCode);

    @Query(value = "SELECT *  FROM " +
            "tbl_campaign_description d " +
            "left outer join " +
            "tbl_campaign_desc_file cf " +
            "on d.campaign_code = cf.campaign_code " +
            "left outer join " +
            "tbl_org o " +
            "on d.org_code = o.org_code " +
            "left outer join " +
            "tbl_member m " +
            "on o.org_code = m.member_code " +
            "WHERE d.end_date < now() AND o.org_code = :orgCode "
            ,nativeQuery = true)
    List<Campaign> findByOrgCodeDone(int orgCode);

//    @Query(value = "SELECT *  FROM  tbl_campaign_description d  RIGHT JOIN  tbl_campaign_desc_file cf on d.campaign_code = cf.campaign_code WHERE d.end_date > now() "
//           ,nativeQuery = true)
//    List<CampaignAndFile> findByEndDateAfter1(LocalDateTime currentDate);

}
