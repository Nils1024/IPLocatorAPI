package de.nils.iplocatorapi.security;

import de.nils.iplocatorapi.exception.RateLimitException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Aspect
@Component
public class RateLimitAspect {
    private static final Logger log = LoggerFactory.getLogger(RateLimitAspect.class);

    private final Map<String, Collection<Long>> requestCounts = new ConcurrentHashMap<>();
    private final int rateLimit;
    private final int duration;

    public RateLimitAspect(@Value("${IPLOCATORAPI_RATE_LIMIT:#{60}}") int rateLimit, @Value("${IPLOCATORAPI_RATE_DURATION:#{60000}}") int duration) {
        this.rateLimit = rateLimit;
        this.duration = duration;

        log.info("Enabled Rate Limit Protection after <{}> requests in <{}> ms", rateLimit, duration);
    }

    @Before("@annotation(de.nils.iplocatorapi.security.RateLimitProtection)")
    public void rateLimitCheck() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        String key = attributes.getRequest().getRemoteAddr();
        long currentTimeMillis = System.currentTimeMillis();
        requestCounts.putIfAbsent(key, new ConcurrentLinkedQueue<>());
        requestCounts.get(key).add(currentTimeMillis);
        cleanupRequestCounts(currentTimeMillis);
        if(requestCounts.get(key).size() > rateLimit) {
            throw new RateLimitException("Rate limit");
        }
    }

    private void cleanupRequestCounts(long currentTimeMillis) {
        requestCounts.values().forEach(l -> l.removeIf(t -> timeIsTooOld(currentTimeMillis, t)));
    }

    private boolean timeIsTooOld(final long currentTimeMillis, final long timeToCheck) {
        return currentTimeMillis - timeToCheck > duration;
    }
}
