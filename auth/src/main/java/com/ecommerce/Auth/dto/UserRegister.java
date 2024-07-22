package com.ecommerce.Auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegister {
    private String email;
    private String Password;
    private Date createdDate = new Date();
}
