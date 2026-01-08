package com.ms.user.controllers;

import com.ms.user.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    private CacheService cacheService;


    @PostMapping
    public ResponseEntity<Object> clearCache(@RequestParam("cacheName") String cacheName) {
        cacheService.evictAllCacheValues(cacheName);
        return ResponseEntity.status(HttpStatus.OK).body("Cache limpo com sucesso!");
    }

    @PutMapping
    public void updateCache(){
        cacheService.updateCacheAllUsers();
    }
}
