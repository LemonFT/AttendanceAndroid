package com.example.hereiam.service;

import com.example.hereiam.entity.User;

public interface IUserService {
    public abstract User register(User user);

    public abstract User signin(User user);

    public abstract User findByEmailAndIdentifierInClass(String email, String identifier, Long classId);

    public abstract User updateProfile(User user);
}
