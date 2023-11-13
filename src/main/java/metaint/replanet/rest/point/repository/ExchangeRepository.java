package metaint.replanet.rest.point.repository;

import metaint.replanet.rest.point.dto.PointHistoryDTO;
import metaint.replanet.rest.point.entity.Exchange;
import metaint.replanet.rest.point.entity.PointFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Integer> {

    List<Exchange> findByMemberCode(int memberCode);

    List<Exchange> findByStatus(String status);

    @Query(value = "SELECT CASE WHEN e.points != 0 THEN e.processing_date ELSE d.donation_date_time END AS changeDate, " +
            "CASE WHEN e.points != 0 THEN e.title ELSE c.campaign_title END AS content, " +
            "CASE WHEN e.points != 0 THEN e.points ELSE d.donation_point END AS changePoint, " +
            "m.current_point AS remainingPoint " +
            "FROM tbl_member m " +
            "LEFT JOIN tbl_point_exchange e ON m.member_code = e.member_code " +
            "LEFT JOIN tbl_donation d ON m.member_code = d.member_code " +
            "LEFT JOIN tbl_campaign_description c ON d.campaign_code = c.campaign_code " +
            "WHERE m.member_code = ?1 " +
            "ORDER BY COALESCE(e.processing_date, d.donation_date_time) DESC"
            , nativeQuery = true)
    List<PointHistoryDTO> findHistoryByMemberCode(int memberCode);

}
