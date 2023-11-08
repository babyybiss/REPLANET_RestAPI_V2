package metaint.replanet.rest.chart.repository;

import metaint.replanet.rest.chart.entity.CampaignDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ChartRepository extends JpaRepository<CampaignDescription, Integer> {

   /* interface CampaignDescriptionDTO {
        int getCampaignCode();
        String getCampaignTitle();
        String getCampaignContent();
        LocalDate getStartDate();
        LocalDate getEndDate();
        String getCampaignCategory();

    }
*/
    @Query(value = "SELECT DISTINCT campaign_category FROM tbl_campaign_description"
            , nativeQuery = true)
    public List<Object[]> findAllCampaingCategory();


    @Query(value = "SELECT b.campaign_category, COUNT(*) AS campaign_count " +
                    "FROM tbl_campaign_description b " +
                    "GROUP BY b.campaign_category"
                    , nativeQuery = true)
    public List<Object[]> countCampaignDescriptionByCampaignCategory();

    @Query(value = "SELECT c.campaign_category, SUM(current_budget) AS sum_current_budget " +
                    "FROM tbl_campaign_description c " +
                    "GROUP BY c.campaign_category"
                    , nativeQuery = true)
    public List<Object[]> sumCurrentBudgetByCampaignCategory();

    @Query(value = "SELECT c.campaign_category, SUM(goal_budget) AS sum_goal_budget " +
                   "FROM tbl_campaign_description c " +
                   "GROUP BY c.campaign_category"
                   , nativeQuery = true)
    public List<Object[]> sumGoalBudgetByCampaignCategory();

    @Query(value = "SELECT DATE_FORMAT(start_date, '%Y-%m') AS monthly, COUNT(*) AS campaigns " +
                   "FROM tbl_campaign_description " +
                   "WHERE DATE_FORMAT(start_date, '%Y-%m') BETWEEN '2023-01' AND '2023-12' " +
                   "GROUP BY monthly"
                   , nativeQuery = true)
    public List<Object[]> countCampaignByCurrentyear();
}
