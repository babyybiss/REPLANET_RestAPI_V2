package metaint.replanet.rest.reviews.repository;

import metaint.replanet.rest.reviews.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {


    @Query(value = "select * from tbl_campaign_review " +
            "where review_title like %:searchFilter%", nativeQuery = true)
    List<Review> findFilteredReviews(@Param("searchFilter") String searchFilter);

}
