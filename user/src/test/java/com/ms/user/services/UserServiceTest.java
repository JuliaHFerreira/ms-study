package com.ms.user.services;

import com.ms.user.exceptions.CPFRegisteredException;
import com.ms.user.models.UserModel;
import com.ms.user.producers.UserProducer;
import com.ms.user.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserProducer userProducer;

    @Test
    void shouldThrowCPFRegisteredException_whenCPFAlreadyExists_andNotSaveOrPublishEmail() {
        // arrange
        UserModel user = new UserModel();
        user.setCPF("11122233344");
        user.setStatus(null);

        when(userRepository.existsByCPF("11122233344")).thenReturn(true);

        // act
        CPFRegisteredException ex =
                assertThrows(CPFRegisteredException.class, () -> userService.save(user));

        // assert (mensagem)
        assertTrue(ex.getMessage().contains("CPF já cadastrado: 11122233344"));

        // assert (não salvou e não publicou)
        verify(userRepository).existsByCPF("11122233344");
        verify(userRepository, never()).save(any());
        verifyNoInteractions(userProducer);

        // opcional: garante que não teve chamadas extras no repo
        verifyNoMoreInteractions(userRepository);
    }
}
