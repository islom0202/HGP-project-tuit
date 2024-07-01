package registration.uz.hgpuserregistration.ContactSupport.Controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import registration.uz.hgpuserregistration.ContactSupport.Entity.Experts;
import registration.uz.hgpuserregistration.ContactSupport.Model.ExpertsDto;
import registration.uz.hgpuserregistration.ContactSupport.Service.ExpertsService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/experts")
public class ExpertsController {

    private final ExpertsService expertsService;

    public ExpertsController(ExpertsService expertsService) {
        this.expertsService = expertsService;
    }

    @PostMapping("/add/details")
    public ResponseEntity<Object> addExpert(
            @RequestParam("fullName") String fullName,
            @RequestParam("phone") String phone,
            @RequestParam("jobTitle") String jobTitle,
            @RequestParam("email") String email,
            @RequestParam("image") MultipartFile image) throws IOException {

        if (!expertsService.exists(fullName)) {
            expertsService.save(fullName, phone, jobTitle, email, image);
            return ResponseEntity.ok("saved");
        } else
            return ResponseEntity.badRequest().body("expert already exists");
    }

    @GetMapping("/get")
    public ResponseEntity<List<ExpertsDto>> getAllExperts() {
        return ResponseEntity.ok(expertsService.findAll());
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Optional<Experts> expert = expertsService.findById(id);
        if (expert.isPresent() && expert.get().getImage() != null) {
            byte[] image = expert.get().getImage();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"image.jpg\"")
                    .body(image);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
