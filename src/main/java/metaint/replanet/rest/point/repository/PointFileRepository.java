package metaint.replanet.rest.point.repository;

import metaint.replanet.rest.point.entity.PointFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointFileRepository extends JpaRepository<PointFile, Integer> {
}
