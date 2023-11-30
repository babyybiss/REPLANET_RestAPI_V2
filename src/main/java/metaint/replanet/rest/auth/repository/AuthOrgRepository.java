package metaint.replanet.rest.auth.repository;

import metaint.replanet.rest.org.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthOrgRepository  extends JpaRepository<Organization,Integer> {
}
