package com.ms.user.services;

import com.ms.user.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

//limpa_todo o cache manualmente
@Service
public class CacheService {

    @Autowired //injetando dependencias
    private CacheManager cacheManager;

    @Autowired
    private UserService userService;

    public void evictAllCacheValues(String cacheName){
        Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
    }

    @CachePut("Users")
    public List<UserModel> updateCacheAllUsers(){
        return userService.allUsers();
    }
}
