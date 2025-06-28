package com.loca_mais.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserEntity {
    private int id;
    private String name;
    private String lastName;
    private String cpf;
    private String phone;
    private String email;
    private String password;
    private Date createdAt;
    private Date updatedAt;
    private boolean active;

    public UserEntity( String name, String lastName,
                       String cpf, String phone, String email, String password
                       ) {
        this.name = name;
        this.lastName = lastName;
        this.cpf = cpf;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.active = true;
    }
}