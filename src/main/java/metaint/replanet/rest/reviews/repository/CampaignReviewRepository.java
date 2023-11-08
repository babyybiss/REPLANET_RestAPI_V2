package metaint.replanet.rest.reviews.repository;

import metaint.replanet.rest.reviews.dto.CombineReviewDTO;
import metaint.replanet.rest.reviews.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CampaignReviewRepository extends JpaRepository<Campaign, Long> {

    @Query(value = "select * from tbl_campaign_description " +
                    "where campaign_title like %:searchFilter%", nativeQuery = true)
    List<Campaign> findFilteredReviews(@Param("searchFilter") String searchFilter);

}
