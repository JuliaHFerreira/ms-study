package com.ms.user.controllers;

import com.ms.user.dtos.UserRecordDto;
import com.ms.user.enums.UserStatus;
import com.ms.user.models.UserModel;
import com.ms.user.repositories.UserRepository;
import com.ms.user.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class UserController {

    final UserService userService;

    final UserRepository userRepository;

    @PostMapping("/users")
    @Operation(
            summary = "Cria um novo usuário",
            description = "Recebe os dados do usuário, salva no banco e retorna o usuário criado. " +
                    "Conversa com o MS Email enviando a informação de novo usuário para disparar os e-mails de boas-vindas."
    )
    public ResponseEntity<UserModel> saveUser(@RequestBody @Valid UserRecordDto userRecordDto) {
        var userModel = new UserModel();

        BeanUtils.copyProperties(userRecordDto, userModel); //convertendo DTO em model
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userModel));
    }

    @GetMapping("/users")
    @Operation(
            summary = "Busca todos os usuários",
            description = "Retorna todos os usuários cadastrado no banco"
    )
    public ResponseEntity<List<UserModel>> getAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.allUsersWithCache());
    }

    @GetMapping("/users/{id}")
    @Operation(
            summary = "Busca usuário por ID",
            description = "Retorna os dados do usuário correspondente ao ID informado caso encontrado."
    )
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "id") UUID id){
        Optional<UserModel> user0 = userRepository.findById(id);
        if (user0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(user0.get());
    }

    @PutMapping("/users/{id}")
    @Operation(
            summary = "Atualizado o usuário pelo ID",
            description = "Atualiza as informações do usuário pelo ID caso encontrado e valido"
    )
    public ResponseEntity<Object> updateUser(@PathVariable(value = "id") UUID id,
                                             @RequestBody @Valid UserRecordDto userRecordDto){ //recebe o corpo da requisição
        Optional<UserModel> user0 = userRepository.findById(id);
        if (user0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        var userModel = user0.get();
        BeanUtils.copyProperties(userRecordDto, userModel); //convertendo DTO em model
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(userModel));
    }

    @PutMapping("/users/status/{id}")
    @Operation(
            summary = "Inativa ou Ativa usuário pelo ID",
            description = "Inativa ou Ativa o usuário pelo ID caso encontrado e valido"
    )
    public ResponseEntity<Object> inativeUser(@PathVariable(value = "id") UUID id){ //recebe o corpo da requisição
        Optional<UserModel> user0 = userRepository.findById(id);
        if (user0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        var userModel = user0.get();
        userService.attStatus(userModel);
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(userModel));
    }

    @DeleteMapping("/users/{id}")
    @Operation(
            summary = "Exclui o usuário pelo ID",
            description = "Exclui ao usuário pelo ID caso encontrado"
    )
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") UUID id){
        Optional<UserModel> user0 = userRepository.findById(id);

        if (user0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        var userModel = user0.get();
        if (userModel.getStatus() == UserStatus.ACTIVE){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The user needs to be inactive to delete.");
        }

        userService.delete(user0);

        return ResponseEntity.status(HttpStatus.OK).body("User deleted sucessfully.");
    }


}
