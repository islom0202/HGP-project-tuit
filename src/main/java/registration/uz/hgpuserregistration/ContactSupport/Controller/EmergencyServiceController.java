package registration.uz.hgpuserregistration.ContactSupport.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import registration.uz.hgpuserregistration.ContactSupport.Entity.EmergencyService;
import registration.uz.hgpuserregistration.ContactSupport.Model.EmergencyServiceRequest;
import registration.uz.hgpuserregistration.ContactSupport.Service.EmergencyServiceService;

import java.util.List;

@RestController
@RequestMapping("/api/emergency")
public class EmergencyServiceController {
    private final EmergencyServiceService emergencyServiceService;

    public EmergencyServiceController(EmergencyServiceService emergencyServiceService) {
        this.emergencyServiceService = emergencyServiceService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<EmergencyService>> getAllEmergencyServices(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location
    ) {
        return ResponseEntity.ok(emergencyServiceService.findAll(name, location));
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addEmergencyService(
            @RequestBody EmergencyServiceRequest emergencyService
    ) {
        if (!emergencyServiceService.exists(emergencyService.getName())) {
            emergencyServiceService.save(emergencyService);
            return ResponseEntity.ok("saved");
        } else
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("already exists");
    }
}
