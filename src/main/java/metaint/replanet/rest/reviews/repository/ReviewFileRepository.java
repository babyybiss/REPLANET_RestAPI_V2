package metaint.replanet.rest.reviews.repository;

import metaint.replanet.rest.reviews.entity.ReviewFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewFileRepository extends JpaRepository<ReviewFile, Long> {
    @Query(value = "select * from tbl_campaign_rev_file where review_code = :reviewCode", nativeQuery = true)
    ReviewFile findByReviewCode(Long reviewCode);
}
