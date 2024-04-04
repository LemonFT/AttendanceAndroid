package com.example.certainlyhereiamfinal.api;

import com.example.certainlyhereiamfinal.model.Member;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IMemberService {

    @GET("members-am/{classroomId}")
    Call<Integer> getNumberMember(@Path("classroomId") Long classroomId);

    @GET("members/{classroomId}")
    Call<List<Member>> getMember(@Path("classroomId") Long classroomId);
}
