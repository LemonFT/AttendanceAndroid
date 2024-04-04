package com.example.certainlyhereiamfinal.api;

import com.example.certainlyhereiamfinal.model.Session;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ISessionService {
    @GET("sessions/{classId}")
    Call<List<Session>> findAllSessionClass(@Path("classId") Long classId);
    @POST("session")
    Call<Session> insertSession(@Body Session session);
    @DELETE("session/{sessionId}")
    Call<String> deleteSession(@Path("sessionId") Long sessionId);
    @PUT("session")
    Call<Session> updateSession(@Body Session session);
}
