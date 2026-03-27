package com.blog.interceptor;

import com.blog.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class CommentRateLimitInterceptor implements HandlerInterceptor {
    private static final int LIMIT = 3;
    private static final long WINDOW_SECONDS = 60;
    private final Map<String, Deque<Long>> requestTracker = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String ip = request.getRemoteAddr();
        long now = Instant.now().getEpochSecond();
        Deque<Long> deque = requestTracker.computeIfAbsent(ip, k -> new ArrayDeque<>());

        synchronized (deque) {
            while (!deque.isEmpty() && now - deque.peekFirst() >= WINDOW_SECONDS) {
                deque.pollFirst();
            }
            if (deque.size() >= LIMIT) {
                response.setStatus(429);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(mapper.writeValueAsString(ApiResponse.error(429, "Too many comments from this IP")));
                return false;
            }
            deque.addLast(now);
        }
        return true;
    }
}
