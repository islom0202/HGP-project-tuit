package registration.uz.hgpuserregistration.DetectorData;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import registration.uz.hgpuserregistration.User.Entity.UserProfile;
import registration.uz.hgpuserregistration.User.Respository.UserProfileRepository;

@Service
@AllArgsConstructor
public class DetectorService {

    private final DetectorRepository detectorRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserProfileRepository userProfileRepository;

    @Transactional
    public String sendToUser(DetectorDataDTO dto) {
        UserProfile userProfile = userProfileRepository.findByLogin(dto.getDetectorId());
        DetectorData detectorData = new DetectorData();
        detectorData.setDetectorId(dto.getDetectorId());
        detectorData.setTemperature(dto.getTemperature());
        detectorData.setBattery(dto.getBattery());
        detectorData.setPrice(dto.getPrice());
        detectorData.setGasPressure(dto.getGasPressure());
        detectorData.setSpeed(dto.getSpeed());
        detectorData.setUserId(userProfile);
        detectorRepository.save(detectorData);

        simpMessagingTemplate.convertAndSend("/topic/message", dto);
        return "sent";
    }
}
