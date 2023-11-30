package metaint.replanet.rest.chart.repository;

import metaint.replanet.rest.chart.entity.CampaignDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChartRepository extends JpaRepository<CampaignDescription, Integer> {

    /* ------------ RepositoryTest start ------------- */

    @Query("SELECT DISTINCT campaignCategory FROM tblCampaignDescription")
    List<Object[]> findDistinctByCampaignCategory();

    @Query(value = "SELECT DISTINCT campaign_category FROM tbl_campaign_description"
            , nativeQuery = true)
    List<Object[]> findAllCampaingCategory();

    @Query(value = "SELECT sub2.campaign_category, " +
            "sub2.campaigns, " +
            "sub2.sum_current_budget, " +
            "sub2.sum_goal_budget, " +
            "sub2.display_sum_current_budget, " +
            "sub2.sum_goal_budget-sub2.display_sum_current_budget AS sum_expect_budget, " +
            "ROUND(sub2.display_sum_current_budget/sub2.sum_goal_budget*100, 2) AS progress " +
            "FROM ( " +
            "SELECT sub1.campaign_category, " +
            "sub1.campaigns, " +
            "sub1.sum_current_budget, " +
            "sub1.sum_goal_budget, " +
            "CASE WHEN sub1.sum_current_budget > sub1.sum_goal_budget THEN sub1.sum_goal_budget ELSE sub1.sum_current_budget " +
            "END AS display_sum_current_budget " +
            "FROM ( " +
            "SELECT c.campaign_category, " +
            "COUNT(*) AS campaigns, " +
            "SUM(c.current_budget) AS sum_current_budget, " +
            "SUM(c.goal_budget) AS sum_goal_budget " +
            "FROM tbl_campaign_description c " +
            "GROUP BY campaign_category " +
            ") AS sub1 " +
            ") AS sub2"
            , nativeQuery = true)
    List<Object[]> countAndSumByCategory();

    @Query(value = "SELECT sub2.full_year, " +
            "sub2.monthly, " +
            "sub2.campaigns, " +
            "sub2.sum_current_budget, " +
            "sub2.sum_goal_budget, " +
            "sub2.display_sum_current_budget, " +
            "sub2.sum_goal_budget-sub2.display_sum_current_budget AS sum_expect_budget, " +
            "ROUND(sub2.display_sum_current_budget/sub2.sum_goal_budget*100, 2) AS progress " +
            "FROM ( " +
            "SELECT sub1.full_year, " +
            "sub1.monthly, " +
            "sub1.campaigns, " +
            "sub1.sum_current_budget, " +
            "sub1.sum_goal_budget, " +
            "CASE WHEN sub1.sum_current_budget > sub1.sum_goal_budget THEN sub1.sum_goal_budget ELSE sub1.sum_current_budget " +
            "END AS display_sum_current_budget " +
            "FROM ( " +
            "SELECT YEAR(d.start_date) AS full_year, " +
            "MONTH(d.start_date) AS monthly, " +
            "COUNT(*) AS campaigns, " +
            "SUM(d.current_budget) AS sum_current_budget, " +
            "SUM(d.goal_budget) AS sum_goal_budget " +
            "FROM tbl_campaign_description d " +
            "GROUP BY monthly " +
            "ORDER BY monthly " +
            ") AS sub1 " +
            "WHERE full_year = 2023 " +
            ") AS sub2"
            , nativeQuery = true)
    List<Object[]> countAndSumByCurrentYear();

    @Query(value = "SELECT sub2.full_year, " +
            "sub2.monthly, " +
            "sub2.campaigns, " +
            "sub2.sum_current_budget, " +
            "sub2.sum_goal_budget, " +
            "sub2.display_sum_current_budget, " +
            "sub2.sum_goal_budget-sub2.display_sum_current_budget AS sum_expect_budget, " +
            "ROUND(sub2.display_sum_current_budget/sub2.sum_goal_budget*100, 2) AS progress " +
            "FROM ( " +
            "SELECT sub1.full_year, " +
            "sub1.monthly, " +
            "sub1.campaigns, " +
            "sub1.sum_current_budget, " +
            "sub1.sum_goal_budget, " +
            "CASE WHEN sub1.sum_current_budget > sub1.sum_goal_budget THEN sub1.sum_goal_budget ELSE sub1.sum_current_budget " +
            "END AS display_sum_current_budget " +
            "FROM ( " +
            "SELECT YEAR(d.start_date) AS full_year, " +
            "MONTH(d.start_date) AS monthly, " +
            "COUNT(*) AS campaigns, " +
            "SUM(d.current_budget) AS sum_current_budget, " +
            "SUM(d.goal_budget) AS sum_goal_budget " +
            "FROM tbl_campaign_description d " +
            "GROUP BY monthly " +
            "ORDER BY monthly " +
            ") AS sub1 " +
            "WHERE full_year = 2022 " +
            ") AS sub2"
            , nativeQuery = true)
    List<Object[]> countAndSumByPreviousYear();
    /* ------------ RepositoryTest END ------------- */



}
