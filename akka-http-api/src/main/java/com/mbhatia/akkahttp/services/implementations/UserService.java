package com.mbhatia.akkahttp.services.implementations;

import com.mbhatia.akkahttp.entities.User;
import com.mbhatia.akkahttp.services.contracts.IUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private final static List<User> users = new ArrayList<>();

    static {
        users.add(new User(1l, "Alice"));
        users.add(new User(2l, "Bob"));
        users.add(new User(3l, "Chris"));
        users.add(new User(4l, "Dick"));
        users.add(new User(5l, "Eve"));
        users.add(new User(6l, "Finn"));
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return users.parallelStream()
                .filter(user -> user.getId() == userId)
                .findFirst();
    }

    @Override
    public User createUser(User user) {
        if(!users.contains(user))
            users.add(user);
        return user;
    }
}
