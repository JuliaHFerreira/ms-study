package com.ms.user.producers;

import com.ms.user.dtos.EmailDto;
import com.ms.user.models.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor //sobe somente com os obrigatórios
public class UserProducer {

    final RabbitTemplate rabbitTemplate;

    @Value(value = "${broker.queue.email.name}")
    private String routingKey;

    public void publishMessangeEmailCreatedUser(UserModel userModel){
        var emailDto = new EmailDto();
        emailDto.setUserId(userModel.getUserId());
        emailDto.setEmailTo(userModel.getEmail());
        emailDto.setSubject("Cadastro realizado com sucesso!");
        emailDto.setText(userModel.getName() + ", seja bem vindo(a)! \n" +
                "Agradecemos o seu cadastro, aproveite agora todos os recursos da nossa plataforma!");

        rabbitTemplate.convertAndSend("", routingKey, emailDto); //passa o exchange vazio pois é uma default
    }

    public void publishMessangeEmailUpdateStatus(UserModel userModel){ //publica a mensagem quando altera cadastro
        var emailDto = new EmailDto();
        emailDto.setUserId(userModel.getUserId());
        emailDto.setEmailTo(userModel.getEmail());
        emailDto.setSubject("Cadastro atualizado com sucesso!");
        emailDto.setText(userModel.getName() + ", seu cadastro foi alterado para "+ userModel.getStatus() + "!");

        rabbitTemplate.convertAndSend("", routingKey, emailDto); //passa o exchange vazio pois é uma default
    }

    public void publishMessangeEmailDeleteUser(UserModel userModel){ //publica a mensagem quando altera cadastro
        var emailDto = new EmailDto();
        emailDto.setUserId(userModel.getUserId());
        emailDto.setEmailTo(userModel.getEmail());
        emailDto.setSubject("Cadastro Deletado com sucesso!");
        emailDto.setText("Obrigado pela parceria "+ userModel.getName() + ", estaremos sempre a disposição!");

        rabbitTemplate.convertAndSend("", routingKey, emailDto); //passa o exchange vazio pois é uma default
    }




}
