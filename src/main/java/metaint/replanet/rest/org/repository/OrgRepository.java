package metaint.replanet.rest.org.repository;

import metaint.replanet.rest.org.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgRepository extends JpaRepository<Organization,Integer> {
}
