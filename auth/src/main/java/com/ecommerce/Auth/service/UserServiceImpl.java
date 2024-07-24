package com.ecommerce.Auth.service;

import com.ecommerce.Auth.dto.ListAllUserResponse;
import com.ecommerce.Auth.dto.LoginRequest;
import com.ecommerce.Auth.dto.RegisterResponse;
import com.ecommerce.Auth.model.User;
import com.ecommerce.Auth.repository.UserRepository;
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
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RegisterResponse registerUser(RegisterResponse registerResponse) {
        log.info(String.format("Received the user response %s", registerResponse));
        log.info("Saving user to database");
       User registerUser = User.builder()
               .email(registerResponse.getEmail())
               .password(registerResponse.getPassword())
               .createdDate(new Date())
               .build();
       userRepository.save(registerUser);
       log.info("User registered.");
        return modelMapper.map(registerUser, RegisterResponse.class);
    }

    @Override
    public boolean loginUser(LoginRequest loginRequest) {
        Optional<User> fetchUserByEmail = userRepository.findByEmail(loginRequest.getEmail());
        if (fetchUserByEmail.isEmpty()){
            log.error(String.format("No uer found with email id %s", loginRequest.getEmail()));
            return false;
        }
        log.info(String.format("User found %s", fetchUserByEmail));
        return true;
    }

    @Override
    public List<ListAllUserResponse> listAllUsers() {
        log.info("Fetching all the users");
        List<User> fetchAll = userRepository.findAll();
        return fetchAll.stream().map(value -> modelMapper.map(value, ListAllUserResponse.class)).toList();
    }

    @Override
    public boolean deleteUser(String id) {
        log.info(String.format("Received the id: %s to delete user", id));
        Optional<User> deleteUser = userRepository.findById(id);
        if(deleteUser.isEmpty()){
            log.info(String.format("No such user with id : %s found", id));
            return false;
        }
        User toDelete = deleteUser.get();
        userRepository.delete(toDelete);
        log.info("User deleted successfully");
        return true;
    }
}
