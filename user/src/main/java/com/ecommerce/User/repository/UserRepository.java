package com.ecommerce.User.repository;

import com.ecommerce.User.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<String, User> {
}
