package com.example.certainlyhereiamfinal.viewmodel;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.certainlyhereiamfinal.api.IUserService;
import com.example.certainlyhereiamfinal.api.RetrofitClient;
import com.example.certainlyhereiamfinal.model.FirstSign;
import com.example.certainlyhereiamfinal.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninViewModel extends ViewModel {
    private IUserService userService;

    public SigninViewModel() {
        userService = RetrofitClient.getRetrofitInstance().create(IUserService.class);
    }

    public LiveData<User> register(String email, String password){
        MutableLiveData<User> result = new MutableLiveData<>();
        Call<User> call = userService.register(new User(email, password));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    result.setValue(response.body());
                }else{
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
        return result;
    }
    public LiveData<FirstSign> signIn(String email, String password) {
        MutableLiveData<FirstSign> result = new MutableLiveData<>();
        Call<FirstSign> call = userService.signin(new User(email, password));
        call.enqueue(new Callback<FirstSign>() {
            @Override
            public void onResponse(Call<FirstSign> call, Response<FirstSign> response) {
                if (response.isSuccessful()) {
                    result.setValue(response.body());
                } else {
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<FirstSign> call, Throwable t) {
                Log.e("api failed", t.toString());
            }
        });
        return result;
    }

    public LiveData<User> updateProfile(User user){
        MutableLiveData<User> result = new MutableLiveData<>();
        Call<User> call = userService.updateProfile(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    result.setValue(response.body());
                }else {
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("api failed", t.toString());
            }
        });
        return result;
    }

    public LiveData<String> verifiEmail(User user){
        MutableLiveData<String> result = new MutableLiveData<>();
        Call<com.example.certainlyhereiamfinal.model.Response> call = userService.verifiEmail(user);
        call.enqueue(new Callback<com.example.certainlyhereiamfinal.model.Response>() {
            @Override
            public void onResponse(Call<com.example.certainlyhereiamfinal.model.Response> call, Response<com.example.certainlyhereiamfinal.model.Response> response) {
                if(response.isSuccessful()){
                    result.setValue(response.body().getData());
                }else {
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<com.example.certainlyhereiamfinal.model.Response> call, Throwable t) {
                Log.e("api failed", t.toString());
            }
        });
        return result;
    }

    public LiveData<User> updatePwd(User user, String code){
        MutableLiveData<User> result = new MutableLiveData<>();
        Call<User> call = userService.updatePwd(user, code);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    result.setValue(response.body());
                }else{
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("api failed", t.toString());
            }
        });
        return result;
    }
}
