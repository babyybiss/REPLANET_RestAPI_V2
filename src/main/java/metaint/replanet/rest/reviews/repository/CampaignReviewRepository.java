package metaint.replanet.rest.reviews.repository;

import metaint.replanet.rest.reviews.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignReviewRepository extends JpaRepository<Campaign, Long> {
}
