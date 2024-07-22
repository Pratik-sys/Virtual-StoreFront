package com.ecommerce.Auth.service;

import com.ecommerce.Auth.dto.UserList;
import com.ecommerce.Auth.dto.UserLogin;
import com.ecommerce.Auth.dto.UserRegister;
import com.ecommerce.Auth.model.Auth;
import com.ecommerce.Auth.repository.AuthRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserRegister registerUser(UserRegister userRegister) {
        Auth auth = Auth.builder()
                .email(userRegister.getEmail())
                .Password(userRegister.getPassword())
                .createdDate(new Date())
                .build();
        authRepository.save(auth);
        return modelMapper.map(auth, UserRegister.class);
    }

    @Override
    public Boolean loginUser(UserLogin userLogin) {
        Optional<Auth> optionalUser = authRepository.findByEmail(userLogin.getEmail());
        if(optionalUser.isEmpty()){
            return false;
        }
        Auth UthUser = optionalUser.get();
        return true;
    }

    @Override
    public boolean deleteUserById(String id) {
        Optional<Auth> optionalAuth = authRepository.findById(id);
        if (optionalAuth.isPresent()){
            Auth deleteauth = optionalAuth.get();
            authRepository.delete(deleteauth);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<UserList> listAllUsers() {
        List<Auth> fetchAllUsers = authRepository.findAll();
        return fetchAllUsers.stream().map(value -> modelMapper.map(value, UserList.class)).toList();
    }
}
