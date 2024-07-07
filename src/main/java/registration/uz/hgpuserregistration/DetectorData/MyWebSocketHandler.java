package registration.uz.hgpuserregistration.DetectorData;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {
    private final WebSocketDataService webSocketDataService;

    public MyWebSocketHandler(WebSocketDataService webSocketDataService) {
        this.webSocketDataService = webSocketDataService;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        System.out.println("Received: " + payload);

        webSocketDataService.handleIncomingData(payload);
        session.sendMessage(new TextMessage("Data received successfully"));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("Connected to WebSocket");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("Disconnected from WebSocket");
    }
}