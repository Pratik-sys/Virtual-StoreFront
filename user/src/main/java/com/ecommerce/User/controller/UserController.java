package com.ecommerce.User.controller;

import com.ecommerce.User.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@NoArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

}
