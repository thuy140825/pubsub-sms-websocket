package com.example.myservice.websocket;// SessionRegistry.java
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import java.util.concurrent.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SessionRegistry {

    // userId -> set of sessions (đa thiết bị)
    private final ConcurrentMap<String, Set<WebSocketSession>> userSessions = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, List<WebSocketSession>> anonymousSessions = new ConcurrentHashMap<>();;
    // sessionId -> userId (để remove nhanh)
    private final ConcurrentMap<String, String> sessionUser = new ConcurrentHashMap<>();

    public void addSession(String userId, WebSocketSession session) {
        userSessions
            .computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet())
            .add(session);
        sessionUser.put(session.getId(), userId);
    }

    public void removeSession(WebSocketSession session) {
        String userId = sessionUser.remove(session.getId());
        if (userId == null) return;
//        Set<WebSocketSession> set = userSessions.get(userId);
//        if (set != null) {
//            set.remove(session);
//            if (set.isEmpty()) userSessions.remove(userId, Collections.emptySet());
//        }
        // -> Tối ưu
        userSessions.computeIfPresent(userId, (key, sessions) -> {
            sessions.remove(session);
            return sessions.isEmpty() ? null : sessions;
        });
    }

    public Set<WebSocketSession> getUserSessions(String userId) {
        return userSessions.getOrDefault(userId, Collections.emptySet());
    }

    public Set<String> onlineUsers() {
        return userSessions.entrySet().stream()
            .filter(e -> !e.getValue().isEmpty())
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }
}
