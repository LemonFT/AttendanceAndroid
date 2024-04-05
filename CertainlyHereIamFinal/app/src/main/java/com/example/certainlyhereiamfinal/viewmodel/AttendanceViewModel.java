package com.example.certainlyhereiamfinal.viewmodel;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.certainlyhereiamfinal.api.IAttendanceService;
import com.example.certainlyhereiamfinal.api.RetrofitClient;
import com.example.certainlyhereiamfinal.model.Attendance;
import com.example.certainlyhereiamfinal.model.AttendanceRequest;
import com.example.certainlyhereiamfinal.model.Member;
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
        return  result;
    }

    public LiveData<Attendance> insertAttendance(Attendance attendance){
        MutableLiveData<Attendance> result = new MutableLiveData<>();
        Call<Attendance> call = iAttendanceService.insertAttendance(attendance);
        call.enqueue(new Callback<Attendance>() {
            @Override
            public void onResponse(Call<Attendance> call, Response<Attendance> response) {
                if(response.isSuccessful()){
                    result.setValue(response.body());
                }else {
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Attendance> call, Throwable t) {
                Log.e("api failed", t.toString());
            }
        });
        return result;
    }
}
