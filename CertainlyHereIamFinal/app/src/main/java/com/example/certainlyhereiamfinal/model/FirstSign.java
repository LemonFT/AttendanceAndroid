package com.example.certainlyhereiamfinal.model;

public class FirstSign {
    private Token tokens;
    private User user;

    public FirstSign(Token tokens, User user) {
        this.tokens = tokens;
        this.user = user;
    }

    public Token getTokens() {
        return tokens;
    }

    public void setTokens(Token tokens) {
        this.tokens = tokens;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
