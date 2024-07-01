package registration.uz.hgpuserregistration.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import registration.uz.hgpuserregistration.Registration.Entity.UserProfile;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Long> {

    boolean existsByUserProfile(UserProfile userProfile);
}
