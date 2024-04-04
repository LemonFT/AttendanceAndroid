package com.example.hereiam.service;

import com.example.hereiam.entity.User;

public interface IUserService {
    User register(User user);

    User signin(User user);
}
