package com.example.certainlyhereiamfinal.api;

import com.example.certainlyhereiamfinal.model.Classroom;
import com.example.certainlyhereiamfinal.model.Response;
import com.example.certainlyhereiamfinal.model.User;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IClassroomService {
    @GET("classrooms/{userId}")
    Call<List<Classroom>> findAllClassroomByUserId(@Path("userId") Long userId);
    @POST("classroom")
    Call<Classroom> insertClassroom(@Body Classroom classroom);
    @PUT("classroom")
    Call<Classroom> updateClassroom(@Body Classroom classroom);
    @DELETE("classroom/{classId}")
    Call<Response> deleteClass(@Path("classId") Long classId);
}
