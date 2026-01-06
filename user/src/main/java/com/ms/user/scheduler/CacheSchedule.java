package com.ms.user.scheduler;

import lombok.extern.java.Log;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
@Log
public class CacheSchedule {

    @Scheduled(fixedDelay = 12, timeUnit = TimeUnit.HOURS)
    @CacheEvict("users")
    public void ClearCacheAllUsers() {
        log.info("Cache Clear: " + LocalDateTime.now());
    }
}
