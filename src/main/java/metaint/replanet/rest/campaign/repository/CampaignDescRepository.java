
package metaint.replanet.rest.campaign.repository;
import metaint.replanet.rest.campaign.entity.CampaignDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CampaignDescRepository extends JpaRepository<CampaignDescription, Integer> {}
