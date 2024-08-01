package registration.uz.hgpuserregistration.DetectorData;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/detector")
@AllArgsConstructor
public class DetectorRestController {

    private final DetectorService detectorService;

    @PostMapping("/send")
    public ResponseEntity<Object> send(@RequestBody DetectorDataDTO detectorData) {
        return ResponseEntity.ok(detectorService.sendToUser(detectorData));
    }
}
