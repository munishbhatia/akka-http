package com.mbhatia.akkahttp.services.contracts;

import com.mbhatia.akkahttp.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getUsers();
    Optional<User> getUserById(long userId);
    User createUser(User user);
}
