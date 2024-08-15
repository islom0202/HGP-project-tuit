package registration.uz.hgpuserregistration.DetectorData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import registration.uz.hgpuserregistration.User.Entity.UserProfile;

@Repository
public interface DetectorRepository extends JpaRepository<DetectorData, String> {
    DetectorData findByUserId(UserProfile userId);

    @Query("select d.detectorId from DetectorData d where d.userId.id=:id")
    String getDetectorId(@Param("id") Long id);
}
