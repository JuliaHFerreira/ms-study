package com.ms.user.services;

import com.ms.user.enums.UserStatus;
import com.ms.user.exceptions.CPFRegisteredException;
import com.ms.user.models.UserModel;
import com.ms.user.producers.UserProducer;
import com.ms.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor //construtor com todos os argumentos || noArgsConstructor (construtor vazio)
public class UserService {

    final UserRepository userRepository;
    final UserProducer userProducer;

    @Transactional //reverte a base para caso der um erro na base
    public UserModel save(UserModel userModel){
        if (userModel.getStatus() == null){
            userModel.setStatus(UserStatus.ACTIVE);
        }
        if (userRepository.existsByCPF(userModel.getCPF())) {
            throw new CPFRegisteredException("CPF já cadastrado: " + userModel.getCPF());
        }
        userModel = userRepository.save(userModel);
        userProducer.publishMessangeEmailCreatedUser(userModel);
        return userModel;
    }

    @Transactional
    public UserModel attStatus(UserModel userModel){
        if (userModel.getStatus() == UserStatus.ACTIVE) {
            userModel.setStatus(UserStatus.INACTIVE);
        } else{
            userModel.setStatus(UserStatus.ACTIVE);
        }
        userProducer.publishMessangeEmailUpdateStatus(userModel);
        return userModel;
    }

    @Transactional
    public Optional<UserModel> delete(Optional<UserModel> user0){

        userRepository.delete(user0.get());
        userProducer.publishMessangeEmailDeleteUser(user0.get());
        return user0;
    }

    @Cacheable("users")
    public List<UserModel> allUsersWithCache(){
        return allUsers();
    }

    public List<UserModel> allUsers(){
        return userRepository.findAll();
    }

}
