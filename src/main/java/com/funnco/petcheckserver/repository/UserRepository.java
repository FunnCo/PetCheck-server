package com.funnco.petcheckserver.repository;

import com.funnco.petcheckserver.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    boolean existsByEmail(String email);

    List<User> findUsersByFirstNameContainingIgnoreCase(String pattern);
    List<User> findUsersByLastNameContainingIgnoreCase(String pattern);
    List<User> findUsersByEmailContainingIgnoreCase(String pattern);

    User findByEmailAndPassword(String email, String password);
}