package registration.uz.hgpuserregistration.Order;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import registration.uz.hgpuserregistration.Email.EmailService;
import registration.uz.hgpuserregistration.Exception.UserProfileNotFoundException;
import registration.uz.hgpuserregistration.User.Service.UserProfileService;

import java.util.List;

@RestController
@SecurityRequirement(
        name = "bearerAuth"
)
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserProfileService userProfileService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

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
            String text = "Hi, Order has been successfully accepted! \n"+"Your Login is ready :" + login;
            emailService.sendEmail(userProfileService.getByPassportSerialNumber(request.getPassportSerialNumber()).getEmail(), "Login request", text);
            return ResponseEntity.ok("your login has been sent to your email!");
        } catch (UserProfileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserOrder>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @PostMapping("/done")
    public ResponseEntity<Object> isDone(@RequestParam("userId") String userId) {
        Long id = Long.parseLong(userId);
        orderService.isDone(id);
        return ResponseEntity.ok("done");
    }

    @GetMapping("/order-statis")
    public ResponseEntity<OrderStatistic> getOrderStatistics() {
        return ResponseEntity.ok(orderService.getOrderStatistics());
    }
}