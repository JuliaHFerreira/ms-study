package com.ms.email.consumers;

import com.ms.email.dtos.EmailRecordDto;
import com.ms.email.models.EmailModel;
import com.ms.email.services.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component //Diz pro Spring: “essa classe é um bean gerenciado"
@AllArgsConstructor
public class EmailConsumer {

    final EmailService emailService;

    @RabbitListener(queues =  "${broker.queue.email.name}") // Ele transforma esse metodo em um “ouvinte de fila”: quando chega mensagem na fila informada, o Spring chama esse metodo automaticamente.
    public void listenEmailQueue(@Payload EmailRecordDto emailRecordDto){ //o @Patload indica que o conteúdo da mensagem - "Pegue o body da mensagem, converta pra String e coloque na variável string"
       var emailModel = new EmailModel();
        BeanUtils.copyProperties(emailRecordDto, emailModel);
        //sendEmail
        emailService.sendEmail(emailModel);
    }
}
