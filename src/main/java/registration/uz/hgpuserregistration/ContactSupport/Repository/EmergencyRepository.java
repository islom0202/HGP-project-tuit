package registration.uz.hgpuserregistration.ContactSupport.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import registration.uz.hgpuserregistration.ContactSupport.Entity.EmergencyService;

@Repository
public interface EmergencyRepository extends JpaRepository<EmergencyService, Long> {
}
