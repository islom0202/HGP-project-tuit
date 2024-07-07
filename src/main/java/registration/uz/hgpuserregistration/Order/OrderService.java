package registration.uz.hgpuserregistration.Order;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import registration.uz.hgpuserregistration.DetectorData.DetectorData;
import registration.uz.hgpuserregistration.DetectorData.DetectorRepository;
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
    private final DetectorRepository detectorRepository;

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
            userOrder.setOrderDate(new Date());
            userProfile.setLogin(userProfile.getPassportSerialNumber());
            //setting and saving data in detector_data table
            DetectorData detectorData = new DetectorData();
            detectorData.setDetectorId(userProfile.getPassportSerialNumber());
            detectorData.setUserId(userProfile);
            detectorRepository.save(detectorData);
            //userProfile.setDetectorData(detectorData);
            //saving order data in order table
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