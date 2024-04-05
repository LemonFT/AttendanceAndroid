package com.example.hereiam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.example.hereiam.entity.User;
import com.example.hereiam.repository.UserRepository;

@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    @SuppressWarnings("null")
    @Override
    public User register(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(5)));
        return userRepository.save(user);
    }

    @Override
    public User signin(User user) {
        User similarUser = userRepository.findByEmail(user.getEmail());
        boolean userTrue = BCrypt.checkpw(user.getPassword(),
                similarUser.getPassword());
        return userTrue ? similarUser : null;
    }

    @Override
    public User findByEmailAndIdentifierInClass(String email, String identifier, Long classId) {
        return userRepository.findUserByEmailOrIdentifierInClass(email, identifier, classId);
    }

}
