package com.ecommerce.User.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class User {
    @Id
    private String id;
    private String userName;
    private String Password;
    private String email;
    private Date createdDate = new Date();
}
