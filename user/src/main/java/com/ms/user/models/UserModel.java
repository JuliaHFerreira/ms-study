package com.ms.user.models;

import com.ms.user.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "TB_USERS") //criando a tabela
@Getter //cria os getter e setter em tempo de execução
@Setter
public class UserModel implements Serializable {
    private static final long serialVersionUID = 1L; //controle de versões

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId; //id unico não sequenciais
    private String name;
    private String email;
    @Column(length = 11, unique = true)
    private String CPF;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
}
