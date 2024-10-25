package com.mental.cove.aspect;
import com.mental.cove.annotation.RateLimit;
import com.mental.cove.exception.RateLimitExceededException;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import io.github.bucket4j.Bandwidth;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
@Aspect
@Component
@Slf4j
public class RateLimitAspect {
    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Pointcut("@annotation(com.mental.cove.annotation.RateLimit)")
    public void rateLimitedMethods() {}

    @Around("rateLimitedMethods()")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);
        String key = method.toString();
        Bucket bucket = buckets.computeIfAbsent(key, k -> createBucket(rateLimit));
        if (bucket.tryConsume(1)) {
            return pjp.proceed();
        } else {
            log.info("Rate limit reached for method {}", method.getName());
            throw new RateLimitExceededException(
                    String.format("The method '%s' has been called more than %d times in the configured period.", method.getName(), rateLimit.requests())
            );
        }
    }

    private Bucket createBucket(RateLimit rateLimit) {
        Refill refill = Refill.greedy(rateLimit.requests(), Duration.ofMinutes(rateLimit.duration()));
        Bandwidth limit = Bandwidth.classic(rateLimit.requests(), refill);
        return Bucket4j.builder().addLimit(limit).build();
    }
}
