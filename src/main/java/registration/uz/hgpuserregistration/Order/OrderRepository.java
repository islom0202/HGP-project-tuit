package registration.uz.hgpuserregistration.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import registration.uz.hgpuserregistration.Registration.Entity.UserProfile;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Long> {

    boolean existsByUserProfile(UserProfile userProfile);

    @Query(value = "delete from UserOrder where userProfile.id=:id", nativeQuery = true)
    void deleteOrder(@Param("id") Long id);

    void deleteByUserProfile_Id(Long id);
}
