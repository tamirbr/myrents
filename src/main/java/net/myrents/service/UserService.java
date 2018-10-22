package net.myrents.service;

import net.myrents.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Long id);
    User findByIdActiveItems(Long id);
    User findByEmail(String email);
    void save(User user);
    void delete(User user);
}
