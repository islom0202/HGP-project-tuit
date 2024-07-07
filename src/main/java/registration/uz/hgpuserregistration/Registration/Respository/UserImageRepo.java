package registration.uz.hgpuserregistration.Registration.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import registration.uz.hgpuserregistration.Registration.Entity.UserImage;

@Repository
public interface UserImageRepo extends JpaRepository<UserImage, Long> {
    @Query(value = "delete from UserImage where userProfile.id=:id", nativeQuery = true)
    void deleteIm(@Param("id") Long id);

    void deleteByUserProfile_Id(Long id);
}
