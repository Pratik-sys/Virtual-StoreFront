package com.ecommerce.Auth.service;

import com.ecommerce.Auth.config.JWTService;
import com.ecommerce.Auth.dto.AuthResponse;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTService jwtService;

    @Override
    public RegisterResponse registerUser(RegisterResponse registerResponse) {
        log.info(String.format("Received the user response %s", registerResponse));
        log.info("Saving user to database");
       User registerUser = User.builder()
               .email(registerResponse.getEmail())
               .password(passwordEncoder.encode(registerResponse.getPassword()))
               .createdDate(new Date())
               .build();
       userRepository.save(registerUser);
       log.info("User registered.");
        return modelMapper.map(registerUser, RegisterResponse.class);
    }

    @Override
    public AuthResponse loginUser(LoginRequest loginRequest) {
        System.out.println(loginRequest.getEmail());
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
        if(user != null){
            String token = jwtService.generateToken(user);
            return AuthResponse.builder()
                    .token(token)
                    .build();
        }
        else return null;
    };

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
