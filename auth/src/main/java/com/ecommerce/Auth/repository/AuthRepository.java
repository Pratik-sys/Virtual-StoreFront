package com.ecommerce.Auth.repository;

import com.ecommerce.Auth.model.Auth;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends MongoRepository<Auth, String> {
    Optional<Auth> findByEmail(String email);
}
