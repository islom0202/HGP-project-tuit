package registration.uz.hgpuserregistration.DetectorData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import registration.uz.hgpuserregistration.User.Entity.UserProfile;

@Repository
public interface DetectorRepository extends JpaRepository<DetectorData, String> {
    DetectorData findByUserId(UserProfile userId);

}
