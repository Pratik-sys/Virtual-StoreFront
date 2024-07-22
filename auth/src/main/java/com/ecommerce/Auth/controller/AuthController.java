package com.ecommerce.Auth.controller;

import com.ecommerce.Auth.dto.UserList;
import com.ecommerce.Auth.dto.UserLogin;
import com.ecommerce.Auth.dto.UserRegister;
import com.ecommerce.Auth.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@NoArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserRegister> registerUser(@RequestBody UserRegister userRegister) {
        return ResponseEntity.ok().body(authService.registerUser(userRegister));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLogin> loginUser(@RequestBody UserLogin userLogin) {
        return ResponseEntity.ok().body(authService.loginUser(userLogin));
    }

    @GetMapping("/fetch-all-users")
    public ResponseEntity<List<UserList>> fetchAllUsers(){
        return ResponseEntity.ok().body(authService.listAllUsers());
    }
    @DeleteMapping("/del/{id}")
    public ResponseEntity<String> deleteProductById (@PathVariable("id") String id ) {
        boolean removeUser = authService.deleteUserById(id);
        if (removeUser) {
            return ResponseEntity.ok().body(String.format("Removed User with id: %s", id));
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request failed: Invalid id provided");
    }
}