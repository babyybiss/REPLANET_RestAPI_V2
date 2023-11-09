package metaint.replanet.rest.reviews.repository;

import metaint.replanet.rest.reviews.entity.ReviewFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewFileRepository extends JpaRepository<ReviewFile, Long> {
}
