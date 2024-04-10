package com.example.certainlyhereiamfinal.viewmodel;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.certainlyhereiamfinal.api.IAttendanceService;
import com.example.certainlyhereiamfinal.api.RetrofitClient;
import com.example.certainlyhereiamfinal.model.Attendance;
import com.example.certainlyhereiamfinal.model.AttendanceRequest;
import com.example.certainlyhereiamfinal.model.ExcelExport;
import com.example.certainlyhereiamfinal.model.Member;
import com.example.certainlyhereiamfinal.model.Statistics;
import com.example.certainlyhereiamfinal.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceViewModel extends ViewModel {
    private IAttendanceService iAttendanceService;

    public AttendanceViewModel(){
        iAttendanceService = RetrofitClient.getRetrofitInstance().create(IAttendanceService.class);
    }

    public LiveData<List<Member>> findUsersAttendanced(Long classId, String qr){
        MutableLiveData<List<Member>> result = new MutableLiveData<>();
        Call<List<Member>> call = iAttendanceService.findUsersAttendanced(new AttendanceRequest(classId, qr));
        Log.e("ânnna", qr);
        call.enqueue(new Callback<List<Member>>() {
            @Override
            public void onResponse(Call<List<Member>> call, Response<List<Member>> response) {
                if(response.isSuccessful()){
                    result.setValue(response.body());
                }else {
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Member>> call, Throwable t) {
                Log.e("api failed", t.toString());
            }
        });
        return result;
    }

    public LiveData<List<Member>> findUsersNoAttendanceYet(Long classId, String qr){
        MutableLiveData<List<Member>> result = new MutableLiveData<>();
        Call<List<Member>> call = iAttendanceService.findUsersNoAttendanceYet(new AttendanceRequest(classId, qr));
        call.enqueue(new Callback<List<Member>>() {
            @Override
            public void onResponse(Call<List<Member>> call, Response<List<Member>> response) {
                if(response.isSuccessful()){
                    result.setValue(response.body());
                }else {
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Member>> call, Throwable t) {
                Log.e("api failed", t.toString());
            }
        });
        return  result;
    }

    public LiveData<String> insertAttendance(Attendance attendance){
        MutableLiveData<String> result = new MutableLiveData<>();
        Call<com.example.certainlyhereiamfinal.model.Response> call = iAttendanceService.insertAttendance(attendance);
        call.enqueue(new Callback<com.example.certainlyhereiamfinal.model.Response>() {
            @Override
            public void onResponse(Call<com.example.certainlyhereiamfinal.model.Response> call, Response<com.example.certainlyhereiamfinal.model.Response> response) {
                result.setValue(response.body().getData());
            }

            @Override
            public void onFailure(Call<com.example.certainlyhereiamfinal.model.Response> call, Throwable t) {
                Log.e("api failed", t.toString());
                result.setValue("Check internet and try again");
            }
        });
        return result;
    }

    public LiveData<String> checkAttendanceUser(Attendance attendance){
        MutableLiveData<String> result = new MutableLiveData<>();
        Call<com.example.certainlyhereiamfinal.model.Response> call = iAttendanceService.checkAttendanceUser(attendance);
        call.enqueue(new Callback<com.example.certainlyhereiamfinal.model.Response>() {
            @Override
            public void onResponse(Call<com.example.certainlyhereiamfinal.model.Response> call, Response<com.example.certainlyhereiamfinal.model.Response> response) {
                result.setValue(response.isSuccessful() ? "checked" : "nocheckyet");
            }

            @Override
            public void onFailure(Call<com.example.certainlyhereiamfinal.model.Response> call, Throwable t) {
                Log.e("api failed", t.toString());
                result.setValue("nocheckyet");
            }
        });
        return  result;
    }


    public LiveData<String> insertAttendanceMember(Attendance attendance, double latitude, double longitude){
        MutableLiveData<String> result = new MutableLiveData<>();
        Call<com.example.certainlyhereiamfinal.model.Response> call = iAttendanceService.insertAttendanceMember(attendance, latitude, longitude);
        call.enqueue(new Callback<com.example.certainlyhereiamfinal.model.Response>() {
            @Override
            public void onResponse(Call<com.example.certainlyhereiamfinal.model.Response> call, Response<com.example.certainlyhereiamfinal.model.Response> response) {
                result.setValue(response.body().getData());
            }

            @Override
            public void onFailure(Call<com.example.certainlyhereiamfinal.model.Response> call, Throwable t) {
                result.setValue("Check internet and try again");
            }
        });
        return result;
    }

    public LiveData<String> insertAttendanceMemberQr(Attendance attendance, double latitude, double longitude){
        MutableLiveData<String> result = new MutableLiveData<>();
        Call<com.example.certainlyhereiamfinal.model.Response> call = iAttendanceService.insertAttendanceMemberQr(attendance, latitude, longitude);
        call.enqueue(new Callback<com.example.certainlyhereiamfinal.model.Response>() {
            @Override
            public void onResponse(Call<com.example.certainlyhereiamfinal.model.Response> call, Response<com.example.certainlyhereiamfinal.model.Response> response) {
                result.setValue(response.body().getData());
            }

            @Override
            public void onFailure(Call<com.example.certainlyhereiamfinal.model.Response> call, Throwable t) {
                result.setValue("Check internet and try again");
            }
        });
        return result;
    }

    public LiveData<List<Statistics>> findAttendanceByClassroomId(Long classroomId){
        MutableLiveData<List<Statistics>> result = new MutableLiveData<>();
        Call<List<Statistics>> call = iAttendanceService.findAttendanceByClassroomId(classroomId);
        call.enqueue(new Callback<List<Statistics>>() {
            @Override
            public void onResponse(Call<List<Statistics>> call, Response<List<Statistics>> response) {
                if(response.isSuccessful()){
                    result.setValue(response.body());
                }else{
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Statistics>> call, Throwable t) {
                Log.e("api failed", t.toString());
            }
        });
        return result;
    }

    public LiveData<List<ExcelExport>> statisticAttendanceAllSession(Long classId){
        MutableLiveData<List<ExcelExport>> result = new MutableLiveData<>();
        Call<List<ExcelExport>> call = iAttendanceService.statisticAttendanceAllSession(classId);
        call.enqueue(new Callback<List<ExcelExport>>() {
            @Override
            public void onResponse(Call<List<ExcelExport>> call, Response<List<ExcelExport>> response) {
                if(response.isSuccessful()){
                    result.setValue(response.body());
                }else{
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<ExcelExport>> call, Throwable t) {
                Log.e("api failed", t.toString());
            }
        });
        return result;
    }
}
