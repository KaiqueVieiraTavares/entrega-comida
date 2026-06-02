package com.ms.notificationservice.config;

import com.ms.notificationservice.security.StompPrincipal;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class CustomHandShakeHandler extends DefaultHandshakeHandler {
    private static final String USER_ID_HEADER = "X-User-Id";
    @Nullable
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if(request instanceof ServletServerHttpRequest){
            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
            String userId = servletServerHttpRequest.getServletRequest().getHeader(USER_ID_HEADER);
            if(userId!=null && !userId.isEmpty()){
                return new StompPrincipal(userId);
            }
        }
        return super.determineUser(request, wsHandler, attributes);
    }
}
