package com.example.myservice.websocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
//        if (request instanceof ServletServerHttpRequest servletRequest) {
//            HttpServletRequest httpRequest = servletRequest.getServletRequest();
//
//            // Lấy token từ query param (ws://host/ws?token=xxx)
//            String token = httpRequest.getParameter("token");
//
//            // Hoặc lấy từ header Authorization
//            if (token == null) {
//                token = httpRequest.getHeader("Authorization");
//                if (token != null && token.startsWith("Bearer ")) {
//                    token = token.substring(7);
//                }
//            }
//
//            // Kiểm tra token hợp lệ
//            if (token != null && JwtUtils.validateToken(token)) {
//                String username = JwtUtils.getUsernameFromToken(token);
//                attributes.put("username", username); // lưu vào session attribute
//                return true; // Cho phép kết nối
//            }
//        }
//        return false; // từ chối nếu không hợp lệ
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        // Không cần làm gì thêm
    }
}
