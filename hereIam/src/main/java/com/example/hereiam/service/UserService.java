package com.example.hereiam.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.example.hereiam.config.EmailProperty;
import com.example.hereiam.entity.User;
import com.example.hereiam.repository.UserRepository;

@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    IEmailService iEmailService;

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

    @Override
    public User updateProfile(User user) {
        User similarUser = userRepository.findByEmail(user.getEmail());
        if (!Objects.equals(similarUser.getId(), user.getId())) {
            return null;
        }
        user.setPassword(similarUser.getPassword());
        User userReturn = userRepository.save(user);
        return userReturn;
    }

    @Override
    public boolean checkAndSendVerifiCode(User user) {
        User similarUser = userRepository.findByEmail(user.getEmail());
        if (similarUser == null) {
            return false;
        }

        String to = user.getEmail();
        String uniqN = EmailProperty.uniqueNumber();
        String subject = EmailProperty.TITLE_MAIL;
        String message = EmailProperty.CONTENT_MAIL + uniqN;
        similarUser.setResetPwd(uniqN);
        User saveVerificationCode = userRepository.save(similarUser);
        return saveVerificationCode != null && (iEmailService.send(to, subject, message));
    }

    @Override
    public User updatePwd(User user, String code) {
        User similarUser = userRepository.findByEmail(user.getEmail());
        if (similarUser == null) {
            return null;
        }
        if (!code.equals(similarUser.getResetPwd())) {
            return null;
        }
        similarUser.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(5)));
        return userRepository.save(similarUser);
    }
}
