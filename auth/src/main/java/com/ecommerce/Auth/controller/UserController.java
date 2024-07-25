package com.ecommerce.Auth.controller;

import com.ecommerce.Auth.dto.AuthResponse;
import com.ecommerce.Auth.dto.ListAllUserResponse;
import com.ecommerce.Auth.dto.LoginRequest;
import com.ecommerce.Auth.dto.RegisterResponse;
import com.ecommerce.Auth.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@NoArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/fetch-all")
    public ResponseEntity<List<ListAllUserResponse>> fetchAllUsers(){
        return ResponseEntity.ok().body(userService.listAllUsers());
    }
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable String id){
        boolean user = userService.deleteUser(id);
        if(user){
            return ResponseEntity.ok().body(String.format("User with id -> %s deleted", id));
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body("Invalid provided");
    }
    @PostMapping("/signup")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterResponse registerResponse){
        return ResponseEntity.ok().body(userService.registerUser(registerResponse));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok().body(userService.loginUser(loginRequest));
    }
}
