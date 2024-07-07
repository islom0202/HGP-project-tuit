package registration.uz.hgpuserregistration.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import registration.uz.hgpuserregistration.Registration.Entity.UserProfile;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Long> {

    boolean existsByUserProfile(UserProfile userProfile);

    void deleteByUserProfile_Id(Long id);
}
