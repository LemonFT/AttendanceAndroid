package com.example.certainlyhereiamfinal.api;

import com.example.certainlyhereiamfinal.model.FirstSign;
import com.example.certainlyhereiamfinal.model.Response;
import com.example.certainlyhereiamfinal.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IUserService {
    @POST("user-register")
    Call<User> register(@Body User user);
    @POST("user-signin")
    Call<FirstSign> signin(@Body User user);
    @PUT("user")
    Call<User> updateProfile(@Body User user);
    @POST("user-verification")
    Call<Response> verifiEmail(@Body User user);

    @POST("user-pwd/{verifiCode}")
    Call<User> updatePwd(@Body User user, @Path("verifiCode") String verifiCode);
}
