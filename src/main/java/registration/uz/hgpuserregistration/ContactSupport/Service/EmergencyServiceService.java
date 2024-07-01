package registration.uz.hgpuserregistration.ContactSupport.Service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import registration.uz.hgpuserregistration.ContactSupport.Entity.EmergencyService;
import registration.uz.hgpuserregistration.ContactSupport.Model.EmergencyServiceRequest;
import registration.uz.hgpuserregistration.ContactSupport.Repository.EmergencyRepository;
import registration.uz.hgpuserregistration.ContactSupport.Repository.EmergencyServiceRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class EmergencyServiceService {
    private final EmergencyServiceRepository emergencyServiceRepository;
    private final EmergencyRepository emergencyRepository;

    public List<EmergencyService> findAll(String name, String location) {
        return emergencyServiceRepository.findByFilter(name, location);
    }

    public boolean exists(String name) {
        return emergencyServiceRepository.exists(name);
    }

    public void save(EmergencyServiceRequest emergencyService) {
        EmergencyService emergencyServiceEntity = new EmergencyService();
        emergencyServiceEntity.setName(emergencyService.getName());
        emergencyServiceEntity.setLocation(emergencyService.getLocation());
        emergencyServiceEntity.setPhone(emergencyService.getPhone());
        emergencyRepository.save(emergencyServiceEntity);
    }
}
