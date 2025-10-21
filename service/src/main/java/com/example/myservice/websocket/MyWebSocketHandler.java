package com.example.myservice.websocket;// MyWebSocketHandler.java
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import java.nio.charset.StandardCharsets;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private SessionRegistry sessionRegistry;

    // lưu lastPong dùng cho heartbeat (phần B)
    @Autowired
    private SessionHeartbeatTracker heartbeatTracker;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // userId được gắn ở HandshakeInterceptor sau khi verify token
        String userId = (String) session.getAttributes().get("userId");
        if (userId == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Missing userId"));
            return;
        }
        sessionRegistry.addSession(userId, session);
        heartbeatTracker.onOpen(session); // bắt đầu theo dõi heartbeat
        session.sendMessage(new TextMessage(("WELCOME " + userId).getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // ví dụ echo
        session.sendMessage(new TextMessage(("ECHO: " + message.getPayload())));
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        heartbeatTracker.onPong(session); // cập nhật lastPong
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        heartbeatTracker.onClose(session);
        sessionRegistry.removeSession(session);
    }
}
