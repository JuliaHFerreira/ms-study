package com.ms.email.dtos;

import java.util.UUID;

public record EmailRecordDto(UUID userId,
                             String emailTo, //email do usuario
                             String subject, //titulo do e-mail
                             String text) { //corpo do e-mail
}
