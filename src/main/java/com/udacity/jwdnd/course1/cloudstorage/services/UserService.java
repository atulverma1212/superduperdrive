package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.constants.Consts;
import com.udacity.jwdnd.course1.cloudstorage.entity.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CredentialService credentialService;

    public Optional<User> createUser(User user) {
        User existing = userMapper.findByUsername(user.getUsername());
        user.setSalt(user.getUsername());
        if (existing != null) {
            return Optional.empty();
        }
        Credentials credentials = new Credentials(user, Consts.DEFAULT_CREDS);
        user.setPassword(null);
        userMapper.insert(user);
        credentialService.saveCredentials(credentials);
        return Optional.of(user);
    }

    public Optional<List<User>> getAll() {
        return Optional.of(userMapper.findAll());
    }

    public void delete(User user) {
        userMapper.deleteById(user.getUserId());
    }

    public Optional<User> findById(int id) {
        return Optional.ofNullable(userMapper.findById(id));
    }

    public Optional<User> findByUserName(String username) {
        return Optional.ofNullable(userMapper.findByUsername(username));
    }
}
