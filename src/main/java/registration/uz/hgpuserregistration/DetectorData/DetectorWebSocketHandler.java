package registration.uz.hgpuserregistration.DetectorData;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class DetectorWebSocketHandler extends TextWebSocketHandler {

    private final WebSocketDataService webSocketDataService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public DetectorWebSocketHandler(WebSocketDataService webSocketDataService) {
        this.webSocketDataService = webSocketDataService;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            JsonNode jsonNode = objectMapper.readTree(payload);

            // Handle incoming data
            webSocketDataService.handleIncomingData(payload);

            // Optionally send a response back to the client
            session.sendMessage(message);
        } catch (Exception e) {
            // Log and handle the exception
            System.err.println("Failed to handle WebSocket message: " + e.getMessage());
            session.sendMessage(new TextMessage("Error processing message"));
        }
    }
}
