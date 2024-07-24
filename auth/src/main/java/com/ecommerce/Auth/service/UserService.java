package com.ecommerce.Auth.service;

import com.ecommerce.Auth.dto.ListAllUserResponse;
import com.ecommerce.Auth.dto.LoginRequest;
import com.ecommerce.Auth.dto.RegisterResponse;
import java.util.List;

public interface UserService {
    RegisterResponse registerUser(RegisterResponse registerResponse);
    boolean loginUser(LoginRequest loginRequest);
    List<ListAllUserResponse> listAllUsers();
    boolean deleteUser(String id);
}
