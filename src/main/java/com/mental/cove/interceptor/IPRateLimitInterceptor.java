package com.mental.cove.interceptor;

import com.mental.cove.common.HttpStatusCodes;
import com.mental.cove.properties.RateLimitProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
@Slf4j
@Component
public class IPRateLimitInterceptor implements HandlerInterceptor {

    private final ConcurrentHashMap<String, RequestInfo> requestCounts = new ConcurrentHashMap<>();

    @Autowired
    private RateLimitProperties rateLimitProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String clientIp = request.getRemoteAddr();
        boolean isAllowed = exceedMaxRequestsPerIP(clientIp);
        if (!isAllowed) {
            log.info("Rate limit exceeded. IP: {}", clientIp);
            response.setStatus(HttpStatusCodes.SC_TOO_MANY_REQUESTS);
            return false;
        }
        return true;
    }

    public boolean exceedMaxRequestsPerIP(String ip) {
        RequestInfo info = requestCounts.computeIfAbsent(ip, k -> new RequestInfo());
        synchronized (info) {
            if (LocalDateTime.now().isAfter(info.resetTime)) {
                info.count.set(1);
                info.resetTime = LocalDateTime.now().plusMinutes(rateLimitProperties.getDuration());
                return true;
            } else if (info.count.get() < rateLimitProperties.getMaxRequests()) {
                info.count.incrementAndGet();
                return true;
            } else {
                return false;
            }
        }
    }

    private static class RequestInfo {
        AtomicInteger count = new AtomicInteger(0);
        LocalDateTime resetTime = LocalDateTime.now().plusMinutes(60);
    }
}
