package com.ecommerce.User.service;

import com.ecommerce.User.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void userRegister() {

    }

    @Override
    public void userLogin() {

    }

    @Override
    public boolean userDelete(String id) {
        return false;
    }
}
