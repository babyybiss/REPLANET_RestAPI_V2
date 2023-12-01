package metaint.replanet.rest.chart.repository;

import metaint.replanet.rest.chart.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationChartRepository extends JpaRepository<Donation, Integer> {

    @Query(value = "SELECT d.campaign_code, " +
            "d.donation_date_time AS donation_time, " +
            "SUM(d.donation_point) AS sum_donation_point, " +
            "SUM(p.pay_amount) AS sum_pay_amount, " +
            "SUM(d.donation_point + p.pay_amount) AS sum_donation_amount, " +
            "c.campaign_category " +
            "FROM tbl_donation d " +
            "LEFT JOIN tbl_pay p ON(d.donation_code = p.donation_code) " +
            "LEFT JOIN tbl_campaign_description c ON(d.campaign_code = c.campaign_code) " +
            "GROUP BY d.donation_date_time, d.campaign_code " +
            "ORDER BY d.donation_date_time"
            , nativeQuery = true)
    List<Object[]> donateByTime();
}
