package com.example.hereiam.service;

public interface IEmailService {
    boolean send(String to, String subject, String message);
}