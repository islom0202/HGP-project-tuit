package registration.uz.hgpuserregistration.DetectorData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final DetectorWebSocketHandler detectorWebSocketHandler;

    @Autowired
    public WebSocketConfig(DetectorWebSocketHandler detectorWebSocketHandler) {
        this.detectorWebSocketHandler = detectorWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(detectorWebSocketHandler, "/ws/detector")
                .setAllowedOrigins("*"); // Adjust origins as needed
    }
}
