package com.example.hereiam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.hereiam.config.JwtTokenProvider;
import com.example.hereiam.entity.User;
import com.example.hereiam.mapping.FirstSign;
import com.example.hereiam.mapping.JwtToken;
import com.example.hereiam.service.IUserService;

@Controller
@RequestMapping("")
public class UserApi {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/user-register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User userReturn = iUserService.register(user);
        return userReturn != null ? ResponseEntity.ok().body(userReturn) : ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/user-signin")
    public ResponseEntity<?> postMethodName(@RequestBody User user) {
        User userReturn = iUserService.signin(user);
        if (userReturn != null) {
            JwtToken jwtTokens = jwtTokenProvider.getTokens(userReturn.getId(), userReturn.getIdentifier(),
                    userReturn.getEmail());
            userReturn.setPassword("");
            FirstSign data = new FirstSign(jwtTokens, userReturn);
            return ResponseEntity.ok().body(data);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateProfile(@RequestBody User user) {
        User userReturn = iUserService.updateProfile(user);
        return userReturn != null ? ResponseEntity.ok().body(userReturn)
                : ResponseEntity.notFound().build();
    }
}
