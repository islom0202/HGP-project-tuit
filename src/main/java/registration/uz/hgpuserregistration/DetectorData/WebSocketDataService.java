package registration.uz.hgpuserregistration.DetectorData;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import registration.uz.hgpuserregistration.Registration.Entity.UserProfile;
import registration.uz.hgpuserregistration.Registration.Respository.UserProfileRepository;

import java.util.Optional;

@Service
public class WebSocketDataService {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private DetectorRepository detectorRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void handleIncomingData(String payload) {
        try {
            JsonNode jsonNode = objectMapper.readTree(payload);

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
                Optional<DetectorData> existingDetectorDataOptional = detectorRepository.findById(detectorId);
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
//                detectorData.setUserId(userProfile);

                // Save the updated or new DetectorData
                detectorRepository.save(detectorData);

                // Update UserProfile with the DetectorData reference if needed
//                userProfile.setDetectorData(detectorData);
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
