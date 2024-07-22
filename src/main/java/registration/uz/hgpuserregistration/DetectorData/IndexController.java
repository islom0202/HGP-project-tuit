//package registration.uz.hgpuserregistration.DetectorData;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import registration.uz.hgpuserregistration.User.Entity.UserProfile;
//
//@Controller
//@RequestMapping()
//public class IndexController {
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//    @Autowired
//    private DetectorRepository detectorRepository;
//
//    @MessageMapping("/detector-data")
//    public void handleDetectorData(@Payload DetectorDataDTO detectorDataDTO) {
//        UserProfile userProfile = detectorRepository.findById(detectorDataDTO.getDetectorId()).get().getUserId();
//        String username = userProfile.getLogin();
//        messagingTemplate.convertAndSendToUser(username, "/queue/reply", detectorDataDTO);
//    }
//
//    @GetMapping
//    public String index() {
//        return "index";
//    }
//}
