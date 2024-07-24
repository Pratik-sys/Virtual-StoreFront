package com.ecommerce.Auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListAllUserResponse {
    private String email;
    private String password;
    private Date createdDate;
}
