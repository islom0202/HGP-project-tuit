package registration.uz.hgpuserregistration.DetectorData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetectorRepository extends JpaRepository<DetectorData, String> {
}
