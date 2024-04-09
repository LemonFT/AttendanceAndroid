package com.example.certainlyhereiamfinal.api;


import com.example.certainlyhereiamfinal.model.Attendance;
import com.example.certainlyhereiamfinal.model.AttendanceRequest;
import com.example.certainlyhereiamfinal.model.Member;
import com.example.certainlyhereiamfinal.model.Response;
import com.example.certainlyhereiamfinal.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IAttendanceService {

    @POST("attendanced")
    Call<List<Member>> findUsersAttendanced(@Body AttendanceRequest request);

    @POST("no-attendance-yet")
    Call<List<Member>> findUsersNoAttendanceYet(@Body AttendanceRequest request);

    @POST("attendance-master")
    Call<Response> insertAttendance(@Body Attendance attendance);

    @POST("attendance/{latitude}/{longitude}")
    Call<Response> insertAttendanceMember(@Body Attendance attendance, @Path("latitude") double latitude, @Path("longitude") double longitude);

    @POST("attendance-qr/{latitude}/{longitude}")
    Call<Response> insertAttendanceMemberQr(@Body Attendance attendance, @Path("latitude") double latitude, @Path("longitude") double longitude);
    @POST("attendance-user")
    Call<Response> checkAttendanceUser(@Body Attendance attendance);


}
