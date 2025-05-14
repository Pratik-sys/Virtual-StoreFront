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
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

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
        LOG.info("Received the user response {}", registerResponse);
        LOG.info("Saving user to database");
       User registerUser = User.builder()
               .email(registerResponse.getEmail())
               .password(passwordEncoder.encode(registerResponse.getPassword()))
               .createdDate(new Date())
               .build();
       userRepository.save(registerUser);
        LOG.info("User registered.");
        return modelMapper.map(registerUser, RegisterResponse.class);
    }

    @Override
    public AuthResponse loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    };

    @Override
    public List<ListAllUserResponse> listAllUsers() {
        LOG.info("Fetching all the users");
        List<User> fetchAll = userRepository.findAll();
        return fetchAll.stream().map(value -> modelMapper.map(value, ListAllUserResponse.class)).toList();
    }

    @Override
    public boolean deleteUser(String id) {
        LOG.info("Received the id: %s to delete user {}", id);
        Optional<User> deleteUser = userRepository.findById(id);
        if(deleteUser.isEmpty()){
            LOG.info("No such user with id : {} found", id);
            return false;
        }
        User toDelete = deleteUser.get();
        userRepository.delete(toDelete);
        LOG.info("User deleted successfully");
        return true;
    }
}
