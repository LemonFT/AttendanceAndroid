package com.example.certainlyhereiamfinal.api;

import com.example.certainlyhereiamfinal.model.FirstSign;
import com.example.certainlyhereiamfinal.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IUserService {
    @POST("user-register")
    Call<User> register(@Body User user);
    @POST("user-signin")
    Call<FirstSign> signin(@Body User user);
}
