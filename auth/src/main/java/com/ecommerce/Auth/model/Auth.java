package com.ecommerce.Auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auth {
    @Id
    private String id;
    private String email;
    private String Password;
    private Date createdDate = new Date();
}
