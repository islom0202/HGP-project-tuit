package registration.uz.hgpuserregistration.Order;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import registration.uz.hgpuserregistration.Exception.UserProfileNotFoundException;
import registration.uz.hgpuserregistration.Registration.Entity.UserProfile;
import registration.uz.hgpuserregistration.Registration.Respository.UserProfileRepository;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserProfileRepository profileRepository;

    public boolean existsOrder(String passportSerialNumber) {
        UserProfile userProfile = profileRepository.findByPassportSerialNumber(passportSerialNumber);
        return orderRepository.existsByUserProfile(userProfile);
    }

    @Transactional
    public String saveOrder(OrderRequest request) throws UserProfileNotFoundException {
        UserProfile userProfile = profileRepository.findByPassportSerialNumber(request.getPassportSerialNumber());
        if (userProfile != null) {
            UserOrder userOrder = new UserOrder();
            userOrder.setOrderAddress(request.getOrderAddress());
            userOrder.setUserProfile(userProfile);
            userProfile.setDetectorId(userProfile.getPassportSerialNumber());
            userOrder.setOrderDate(new Date());
            userProfile.setLogin(userProfile.getDetectorId());

            orderRepository.save(userOrder);
            return userProfile.getLogin();
        } else {
            throw new UserProfileNotFoundException("user not found");
        }
    }


    public List<UserOrder> findAll() {
        return orderRepository.findAll();
    }
}