package com.example.certainlyhereiamfinal.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.certainlyhereiamfinal.api.IClassroomService;
import com.example.certainlyhereiamfinal.api.ISessionService;
import com.example.certainlyhereiamfinal.api.RetrofitClient;
import com.example.certainlyhereiamfinal.model.Session;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionViewModel extends ViewModel {
    private ISessionService iSessionService;

    public SessionViewModel() {
        iSessionService = RetrofitClient.getRetrofitInstance().create(ISessionService.class);
    }

    public LiveData<List<Session>> findAllSessionClass(Long classId){
        MutableLiveData<List<Session>> result = new MutableLiveData<>();
        Call<List<Session>> call = iSessionService.findAllSessionClass(classId);
        call.enqueue(new Callback<List<Session>>() {
            @Override
            public void onResponse(Call<List<Session>> call, Response<List<Session>> response) {
                if(response.isSuccessful()){
                    result.setValue(response.body());
                }else {
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Session>> call, Throwable t) {
                Log.e("api failed", t.toString());
            }
        });
        return result;
    }

    public LiveData<Session> insertSession(Session session){
        MutableLiveData<Session> result = new MutableLiveData<>();
        Call<Session> call = iSessionService.insertSession(session);
        call.enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                if(response.isSuccessful()){
                    result.setValue(response.body());
                }else {
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                Log.e("api failed", t.toString());
            }
        });
        return result;
    }

    public LiveData<Session> updateSession(Session session){
        MutableLiveData<Session> result = new MutableLiveData<>();
        Call<Session> call = iSessionService.updateSession(session);
        call.enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                if(response.isSuccessful()){
                    result.setValue(response.body());
                }else {
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                Log.e("api failed", t.toString());
            }
        });
        return result;
    }

    public LiveData<String> deleteSession(Long sessionId){
        MutableLiveData<String> result = new MutableLiveData<>();
        Call<String> call = iSessionService.deleteSession(sessionId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    result.setValue(response.body());
                }else {
                    result.setValue("");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("api failed", t.toString());
            }
        });
        return result;
    }
}
