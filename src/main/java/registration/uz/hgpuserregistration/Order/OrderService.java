package registration.uz.hgpuserregistration.Order;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import registration.uz.hgpuserregistration.DetectorData.DetectorData;
import registration.uz.hgpuserregistration.DetectorData.DetectorRepository;
import registration.uz.hgpuserregistration.Exception.UserProfileNotFoundException;
import registration.uz.hgpuserregistration.User.Entity.UserProfile;
import registration.uz.hgpuserregistration.User.Respository.UserProfileRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
            userOrder.setDone(false);
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

    @Transactional
    public void isDone(Long id) {
        Optional<UserProfile> userProfile = profileRepository.findById(id);
        UserOrder userOrder = orderRepository.findByUserProfile(userProfile);
        if (userProfile.isPresent()) {
            UserProfile userProfile1 = userProfile.get();
            userProfile1.setAccessStatus(true);
            profileRepository.save(userProfile1);
            userOrder.setDone(true);
            orderRepository.save(userOrder);
        }
    }

//    public OrderStatistic getOrderStatistics() {
//        OrderStatistic orderStatistic = new OrderStatistic();
//        //todo: set order json including number of the device sold and when they were sold
//    }
}