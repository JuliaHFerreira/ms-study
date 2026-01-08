package com.ms.user.services;

import com.ms.user.models.UserModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CacheServiceTest {

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @Mock
    private UserService userService;

    @InjectMocks
    private CacheService cacheService;

    @Test
    void evictAllCacheValues_shouldClearCache_whenCacheExists() {
        // arrange
        when(cacheManager.getCache("Users")).thenReturn(cache);

        // act
        cacheService.evictAllCacheValues("Users");

        // assert
        verify(cacheManager).getCache("Users");
        verify(cache).clear();
        verifyNoMoreInteractions(cacheManager, cache);
        verifyNoInteractions(userService);
    }

    @Test
    void evictAllCacheValues_shouldThrowException_whenCacheDoesNotExist() {
        // arrange
        when(cacheManager.getCache("Users")).thenReturn(null);

        // act + assert
        assertThrows(NullPointerException.class,
                () -> cacheService.evictAllCacheValues("Users"));

        verify(cacheManager).getCache("Users");
        verifyNoInteractions(cache, userService);
        verifyNoMoreInteractions(cacheManager);
    }

    @Test
    void updateCacheAllUsers_shouldReturnAllUsers() {
        // arrange
        List<UserModel> expected = List.of(new UserModel(), new UserModel());
        when(userService.allUsers()).thenReturn(expected);

        // act
        List<UserModel> result = cacheService.updateCacheAllUsers();

        // assert
        assertSame(expected, result);
        verify(userService).allUsers();
        verifyNoInteractions(cacheManager, cache);
        verifyNoMoreInteractions(userService);
    }
}

