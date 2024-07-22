package registration.uz.hgpuserregistration.DetectorData;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import registration.uz.hgpuserregistration.User.Entity.UserProfile;
import registration.uz.hgpuserregistration.User.Respository.UserProfileRepository;

import java.util.Optional;

@Service
public class WebSocketDataService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private DetectorRepository detectorDataRepository;

    @Autowired
    private EntityManager entityManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void handleIncomingData(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);

            String detectorId = jsonNode.get("detectorId").asText();
            double gasPressure = jsonNode.get("gasPressure").asDouble();
            int battery = jsonNode.get("battery").asInt();
            double temperature = jsonNode.get("temperature").asDouble();
            double speed = jsonNode.get("speed").asDouble();
            double price = jsonNode.get("price").asDouble();

            // Fetch UserProfile associated with this detectorId
            Optional<UserProfile> userProfileOptional = userProfileRepository.findByDetectorData_DetectorId(detectorId);
            if (userProfileOptional.isPresent()) {
                UserProfile userProfile = userProfileOptional.get();

                // Check if DetectorData already exists
                Optional<DetectorData> existingDetectorDataOptional = detectorDataRepository.findById(detectorId);
                DetectorData detectorData;
                if (existingDetectorDataOptional.isPresent()) {
                    detectorData = existingDetectorDataOptional.get();
                    entityManager.detach(detectorData);
                } else {
                    detectorData = new DetectorData();
                    detectorData.setDetectorId(detectorId);
                }

                // Update DetectorData with new values
                detectorData.setGasPressure(gasPressure);
                detectorData.setBattery(battery);
                detectorData.setTemperature(temperature);
                detectorData.setSpeed(speed);
                detectorData.setPrice(price);
                detectorData.setUserId(userProfile);

                // Save the updated or new DetectorData
                detectorDataRepository.save(detectorData);

                // Update UserProfile with the DetectorData reference if needed
                userProfile.setDetectorData(detectorData);
                userProfileRepository.save(userProfile);

                System.out.println("Detector data saved successfully");
            } else {
                System.out.println("User profile not found for detector ID: " + detectorId);
            }
        } catch (Exception e) {
            System.err.println("Failed to process incoming data: " + e.getMessage());
        }
    }
}
