package com.ecommerce.User.service;


public interface UserService {
    void userRegister();
    void userLogin();
    boolean userDelete(String id);

}
