package metaint.replanet.rest.chart.repository;

import metaint.replanet.rest.chart.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationChartRepository extends JpaRepository<Donation, Integer> {

    @Query(value = "SELECT DATE_FORMAT(d.donation_date_time, '%Y-%m-%d %H:00:00') AS donation_time, " +
            "SUM(d.donation_point) AS sum_donation_point, " +
            "SUM(COALESCE(p.pay_amount, 0)) AS sum_pay_amount, " +
            "SUM(d.donation_point + COALESCE(p.pay_amount, 0)) AS sum_donation_amount " +
            "FROM tbl_donation d " +
            "LEFT JOIN tbl_pay p ON(d.donation_code = p.donation_code) " +
            "GROUP BY donation_time " +
            "ORDER BY donation_time"
            , nativeQuery = true)
    List<Object[]> donateByTime();
}
