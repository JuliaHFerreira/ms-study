package com.ms.user.services;

import com.ms.user.enums.UserStatus;
import com.ms.user.exceptions.CPFRegisteredException;
import com.ms.user.models.UserModel;
import com.ms.user.producers.UserProducer;
import com.ms.user.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProducer userProducer;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldThrowCPFRegisteredException_whenCPFAlreadyExists_andNotSaveOrPublishEmail() {

        // arrange
        UserModel user = new UserModel();
        user.setCPF("11122233344");
        user.setStatus(null);

        // act
        when(userRepository.existsByCPF("11122233344")).thenReturn(true);

        CPFRegisteredException ex =
                assertThrows(CPFRegisteredException.class, () -> userService.save(user));

        // assert
        assertTrue(ex.getMessage().contains("CPF já cadastrado: 11122233344"));

        verify(userRepository).existsByCPF("11122233344");
        verify(userRepository, never()).save(any());
        verifyNoInteractions(userProducer);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldSetStatusActive_whenSavingUserWithNullStatus_andPublishCreatedMessage() {
        // arrange
        String cpf = "11122233344";
        UserModel user = new UserModel();
        user.setCPF(cpf);
        user.setStatus(null);

        when(userRepository.existsByCPF(cpf)).thenReturn(false);

        // Simula "persistência" devolvendo o próprio objeto com um ID
        when(userRepository.save(any(UserModel.class)))
                .thenAnswer(invocation -> {
                    UserModel arg = invocation.getArgument(0);
                    arg.setUserId(UUID.randomUUID());
                    return arg;
                });

        ArgumentCaptor<UserModel> captorSave = ArgumentCaptor.forClass(UserModel.class);
        ArgumentCaptor<UserModel> captorProducer = ArgumentCaptor.forClass(UserModel.class);

        // act
        UserModel saved = userService.save(user);

        // assert
        assertNotNull(saved.getUserId());
        assertEquals(UserStatus.ACTIVE, saved.getStatus());

        verify(userRepository).existsByCPF(cpf);
        verify(userRepository).save(captorSave.capture());
        assertEquals(UserStatus.ACTIVE, captorSave.getValue().getStatus()); // garante que salvou ACTIVE

        verify(userProducer).publishMessangeEmailCreatedUser(captorProducer.capture());
        assertEquals(saved.getUserId(), captorProducer.getValue().getUserId());

        verifyNoMoreInteractions(userRepository, userProducer);
    }

    @Test
    void shouldNotOverrideStatus_whenSavingUserWithNonNullStatus() {
        // arrange
        String cpf = "99988877766";
        UserModel user = new UserModel();
        user.setCPF(cpf);
        user.setStatus(UserStatus.INACTIVE);

        when(userRepository.existsByCPF(cpf)).thenReturn(false);
        when(userRepository.save(any(UserModel.class))).thenAnswer(inv -> inv.getArgument(0));

        ArgumentCaptor<UserModel> captorSave = ArgumentCaptor.forClass(UserModel.class);

        // act
        UserModel saved = userService.save(user);

        // assert
        assertEquals(UserStatus.INACTIVE, saved.getStatus());

        verify(userRepository).existsByCPF(cpf);
        verify(userRepository).save(captorSave.capture());
        assertEquals(UserStatus.INACTIVE, captorSave.getValue().getStatus());

        verify(userProducer).publishMessangeEmailCreatedUser(saved);
        verifyNoMoreInteractions(userRepository, userProducer);
    }

    @Test
    void shouldToggleStatusToInactive_whenCurrentStatusIsActive_andPublishUpdateStatus() {
        // arrange
        UserModel user = new UserModel();
        user.setUserId(UUID.randomUUID());
        user.setStatus(UserStatus.ACTIVE);

        // act
        UserModel updated = userService.attStatus(user);

        // assert
        assertEquals(UserStatus.INACTIVE, updated.getStatus());
        verify(userProducer).publishMessangeEmailUpdateStatus(user);
        verifyNoInteractions(userRepository);
        verifyNoMoreInteractions(userProducer);
    }

    @Test
    void shouldToggleStatusToActive_whenCurrentStatusIsInactive_andPublishUpdateStatus() {
        // arrange
        UserModel user = new UserModel();
        user.setUserId(UUID.randomUUID());
        user.setStatus(UserStatus.INACTIVE);

        // act
        UserModel updated = userService.attStatus(user);

        // assert
        assertEquals(UserStatus.ACTIVE, updated.getStatus());
        verify(userProducer).publishMessangeEmailUpdateStatus(user);
        verifyNoInteractions(userRepository);
        verifyNoMoreInteractions(userProducer);
    }

    @Test
    void shouldSetStatusActive_whenCurrentStatusIsNull_andPublishUpdateStatus() {
        // arrange
        UserModel user = new UserModel();
        user.setUserId(UUID.randomUUID());
        user.setStatus(null);

        // act
        UserModel updated = userService.attStatus(user);

        // assert
        assertEquals(UserStatus.ACTIVE, updated.getStatus());
        verify(userProducer).publishMessangeEmailUpdateStatus(user);
        verifyNoInteractions(userRepository);
        verifyNoMoreInteractions(userProducer);
    }

    // -------- delete() --------

    @Test
    void shouldDeleteUser_whenOptionalHasValue_andPublishDeleteMessage() {
        // arrange
        UserModel user = new UserModel();
        user.setUserId(UUID.randomUUID());

        Optional<UserModel> opt = Optional.of(user);

        // act
        Optional<UserModel> returned = userService.delete(opt);

        // assert
        assertSame(opt, returned);

        verify(userRepository).delete(user);
        verify(userProducer).publishMessangeEmailDeleteUser(user);
        verifyNoMoreInteractions(userRepository, userProducer);
    }

    @Test
    void shouldThrowNoSuchElementException_whenDeletingEmptyOptional() {
        // arrange
        Optional<UserModel> opt = Optional.empty();

        // act + assert
        assertThrows(NoSuchElementException.class, () -> userService.delete(opt));

        // Como estoura antes de chamar delete/publish
        verifyNoInteractions(userRepository, userProducer);
    }

    @Test
    void shouldReturnAllUsers() {
        // arrange
        List<UserModel> users = List.of(new UserModel(), new UserModel());
        when(userRepository.findAll()).thenReturn(users);

        // act
        List<UserModel> result = userService.allUsers();

        // assert
        assertEquals(2, result.size());
        verify(userRepository).findAll();
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userProducer);
    }

    @Test
    void allUsersWithCache_shouldReturnAllUsers() {
        // arrange
        List<UserModel> users = List.of(new UserModel());
        when(userRepository.findAll()).thenReturn(users);

        // act
        List<UserModel> result = userService.allUsersWithCache();

        // assert
        assertEquals(1, result.size());
        verify(userRepository).findAll();
        verifyNoInteractions(userProducer);
        verifyNoMoreInteractions(userRepository);
    }
}
