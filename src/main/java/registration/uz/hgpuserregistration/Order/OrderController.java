package registration.uz.hgpuserregistration.Order;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import registration.uz.hgpuserregistration.Email.EmailService;
import registration.uz.hgpuserregistration.Exception.UserProfileNotFoundException;
import registration.uz.hgpuserregistration.User.Service.UserProfileService;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final EmailService emailService;
    private final UserProfileService userProfileService;

    @PostMapping("/order")
    public ResponseEntity<String> order(@RequestBody OrderRequest request) {
        try {
            if (request == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body is missing");
            }

            if (orderService.existsOrder(request.getPassportSerialNumber())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already ordered");
            }

            String login = orderService.saveOrder(request);
            String text = "Hi, Order has been successfully accepted! \n" + "Your Login is ready :" + login;
            emailService.sendEmail(userProfileService.getByPassportSerialNumber(request.getPassportSerialNumber()).getEmail(), "Login request", text);
            return ResponseEntity.ok("your login has been sent to your email!");
        } catch (UserProfileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/order/list")
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @PostMapping("/delivered")
    public ResponseEntity<Object> isDone(@RequestParam("userId") Long userId) {
        orderService.isDone(userId);
        return ResponseEntity.ok("done");
    }

    @GetMapping("/order-statis")
    public ResponseEntity<List<OrderStatistic>> getOrderStatistics(@RequestParam("year") int year) {
        return ResponseEntity.ok(orderService.getOrderStatisticsByYear(year));
    }
}