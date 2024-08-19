package registration.uz.hgpuserregistration.ContactSupport.Controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import registration.uz.hgpuserregistration.ContactSupport.Model.ContactUsResponseDto;
import registration.uz.hgpuserregistration.ContactSupport.Model.MessageDto;
import registration.uz.hgpuserregistration.ContactSupport.Model.MessageResponse;
import registration.uz.hgpuserregistration.ContactSupport.Service.ContactUsService;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@AllArgsConstructor
public class ContactUsController {

    private final ContactUsService contactUsService;

    @PostMapping("/send/message")
    public ResponseEntity<Object> sendMessage(@RequestBody MessageDto message) {
        contactUsService.save(message);
        return ResponseEntity.ok("Message sent successfully");
    }

    @GetMapping("/get/messages")
    public ResponseEntity<List<ContactUsResponseDto>> getMessages() {
        return ResponseEntity.ok(contactUsService.findAll());
    }

    @PostMapping("/read")
    public ResponseEntity<MessageResponse> read(@RequestBody MessageResponse response) {
            contactUsService.isRead(response);
            return ResponseEntity.ok(new MessageResponse(response.getUserId(), response.getMessageId()));
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> count() {
        return ResponseEntity.ok(contactUsService.countMessage());
    }

}
