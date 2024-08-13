package registration.uz.hgpuserregistration.DetectorData;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/detector")
@AllArgsConstructor
public class DetectorRestController {

    private final DetectorService detectorService;

    @PostMapping("/send")
    public ResponseEntity<Object> send(@RequestBody DetectorDataDTO detectorData) {
        return ResponseEntity.ok(detectorService.sendToUser(detectorData));
    }

    @PostMapping("/turn")
    public ResponseEntity<?> turn(@RequestParam("val") int val) {
        return ResponseEntity.status(200).build();
    }
}
