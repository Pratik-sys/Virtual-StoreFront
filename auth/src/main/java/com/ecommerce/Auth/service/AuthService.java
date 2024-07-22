package com.ecommerce.Auth.service;

import com.ecommerce.Auth.dto.UserList;
import com.ecommerce.Auth.dto.UserLogin;
import com.ecommerce.Auth.dto.UserRegister;
import java.util.List;

public interface AuthService {
    UserRegister registerUser(UserRegister userRegister);
    Boolean loginUser(UserLogin userLogin);
    boolean deleteUserById(String id);
    List<UserList> listAllUsers();
}
