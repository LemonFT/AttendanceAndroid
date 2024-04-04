package com.example.hereiam.mapping;

import com.example.hereiam.entity.JwtToken;
import com.example.hereiam.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FirstSign {
    private JwtToken jwtToken;
    private User user;
}
