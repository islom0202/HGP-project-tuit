package registration.uz.hgpuserregistration.Order;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import registration.uz.hgpuserregistration.DetectorData.DetectorData;
import registration.uz.hgpuserregistration.DetectorData.DetectorRepository;
import registration.uz.hgpuserregistration.Exception.UserProfileNotFoundException;
import registration.uz.hgpuserregistration.User.Entity.UserProfile;
import registration.uz.hgpuserregistration.User.Respository.UserProfileRepository;

import java.util.*;

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
            //saving order data in order table
            orderRepository.save(userOrder);
            return userProfile.getLogin();
        } else {
            throw new UserProfileNotFoundException("user not found");
        }
    }


    public List<UserOrderResponseDto> findAll() {
        List<UserOrder> order = orderRepository.findAll();
        List<UserOrderResponseDto> orderResponseDtos = new ArrayList<>();
        for (UserOrder orderItem : order) {
            UserOrderResponseDto orderResponseDto = new UserOrderResponseDto();
            orderResponseDto.setId(orderItem.getId());
            orderResponseDto.setOrderAddress(orderItem.getOrderAddress());
            orderResponseDto.setOrderDate(orderItem.getOrderDate());
            orderResponseDto.setDelivered(orderItem.isDone());
            orderResponseDto.setUserId(orderItem.getUserProfile().getId());
            orderResponseDtos.add(orderResponseDto);
        }
        orderResponseDtos.sort(Comparator.comparing(UserOrderResponseDto::getId)
                .thenComparing(UserOrderResponseDto::getOrderDate));
        return orderResponseDtos;
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

    public List<OrderStatistic> getOrderStatisticsByYear(int val) {
        String year = String.valueOf(val);
        try {
            String jsonArrayString = orderRepository.getMonthlyCountsByYear(year);
            ObjectMapper mapper = new ObjectMapper();

            List<OrderStatistic> orderStatistics = mapper.readValue(
                    jsonArrayString, new TypeReference<>() {
                    }
            );

            Set<Integer> existingMonths = new HashSet<>();
            for (OrderStatistic orderStatistic : orderStatistics) {
                existingMonths.add(Integer.parseInt(orderStatistic.getMonth()));
            }
            for (int i = 1; i <= 12; i++) {
                if (!existingMonths.contains(i)) {
                    OrderStatistic orderStatistic = new OrderStatistic();
                    orderStatistic.setYear(year);
                    orderStatistic.setMonth(String.format("%02d", i));
                    orderStatistic.setIncome(0);
                    orderStatistic.setNumber(0);
                    orderStatistics.add(orderStatistic);
                }
            }
            orderStatistics.sort(Comparator.comparing(OrderStatistic::getYear)
                    .thenComparing(OrderStatistic::getMonth, Comparator.comparingInt(Integer::parseInt)));

            return orderStatistics;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}