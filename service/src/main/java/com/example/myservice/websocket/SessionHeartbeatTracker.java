package com.example.myservice.websocket;// SessionHeartbeatTracker.java
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.scheduling.annotation.Scheduled;
import java.nio.ByteBuffer;
import java.time.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionHeartbeatTracker {

    // sessionId -> lastPong
    private final ConcurrentHashMap<String, Instant> lastPongMap = new ConcurrentHashMap<>();

    // cấu hình heartbeat
    private static final Duration PING_INTERVAL = Duration.ofSeconds(30);
    private static final Duration PONG_TIMEOUT  = Duration.ofSeconds(45);

    public void onOpen(WebSocketSession session) {
        lastPongMap.put(session.getId(), Instant.now()); // mark connected
    }

    public void onPong(WebSocketSession session) {
        lastPongMap.put(session.getId(), Instant.now());
    }

    public void onClose(WebSocketSession session) {
        lastPongMap.remove(session.getId());
    }

    // Gửi Ping định kỳ và kiểm tra timeout
    @Scheduled(fixedDelay = 30000) // ~ PING_INTERVAL
    public void sendPingAndSweep() {
        // Lưu ý: cần có nơi lấy danh sách session hiện tại.
        // Ở đây ta sử dụng một Hook đơn giản là giữ tham chiếu từ SessionRegistry (tiêm vào nếu cần).
        // Để đơn giản, ta broadcast ping cho tất cả session mà ta biết lastPong.
        lastPongMap.keySet().forEach(sessionId -> {
            // Bạn nên có một SessionRepository để lấy WebSocketSession từ id.
            // Ở đây demo: bỏ qua bước lấy session từ id.
        });
    }
}
