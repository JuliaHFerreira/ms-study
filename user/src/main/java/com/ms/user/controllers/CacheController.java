package com.ms.user.controllers;

import com.ms.user.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    private CacheService cacheService;


    @PostMapping
    public void clearCache(@RequestParam("cacheName") String cacheName) {
        cacheService.evictAllCacheValues(cacheName);
    }

    @PutMapping
    public void updateCache(){
        cacheService.updateCacheAllUsers();
    }
}
