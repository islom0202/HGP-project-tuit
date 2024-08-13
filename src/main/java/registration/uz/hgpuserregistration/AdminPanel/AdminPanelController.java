package registration.uz.hgpuserregistration.AdminPanel;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import registration.uz.hgpuserregistration.User.Entity.Gender;
import registration.uz.hgpuserregistration.User.Model.UserProfileResponseDto;
import registration.uz.hgpuserregistration.User.Model.UserStatistics;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminPanelController {

    private final AdminPanelService adminPanelService;

    @GetMapping("/user-list")
    public ResponseEntity<List<UserProfileResponseDto>> getUserList() {
        return ResponseEntity.ok(adminPanelService.getUserList());
    }

    @GetMapping("/user-statis")
    public ResponseEntity<UserStatistics> getUserStatistics() {
        return ResponseEntity.ok(adminPanelService.getUserStatistics());
    }

    @GetMapping("/searching")
    public ResponseEntity<List<UserProfileResponseDto>> searchUser(
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String lastname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) String enabled) {
        return ResponseEntity.ok(adminPanelService.findBySearching(
                firstname,
                lastname,
                email,
                gender,
                enabled
        ));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAdmin(@RequestBody AdminRequest request) {
        return ResponseEntity.ok(adminPanelService.save(request));
    }
}
