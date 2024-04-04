package com.example.certainlyhereiamfinal.viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.certainlyhereiamfinal.api.IClassroomService;
import com.example.certainlyhereiamfinal.api.IUserService;
import com.example.certainlyhereiamfinal.api.RetrofitClient;
import com.example.certainlyhereiamfinal.model.Classroom;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassroomViewModel extends ViewModel {

    private IClassroomService iClassroomService;

    public ClassroomViewModel() {
        iClassroomService = RetrofitClient.getRetrofitInstance().create(IClassroomService.class);
    }

    public LiveData<List<Classroom>> findAllClassroomByUserId(Long userId){
        MutableLiveData<List<Classroom>> result = new MutableLiveData<>();
        Call<List<Classroom>> call = iClassroomService.findAllClassroomByUserId(userId);
        call.enqueue(new Callback<List<Classroom>>() {
            @Override
            public void onResponse(Call<List<Classroom>> call, Response<List<Classroom>> response) {
                if(response.isSuccessful()){
                    result.setValue(response.body());
                }else{
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Classroom>> call, Throwable t) {
                Log.e("api failed", t.toString() );
            }
        });
        return result;
    }

    public LiveData<Classroom> insertClassroom(Classroom classroom){
        MutableLiveData<Classroom> result = new MutableLiveData<>();
        Call<Classroom> call = iClassroomService.insertClassroom(classroom);
        call.enqueue(new Callback<Classroom>() {
            @Override
            public void onResponse(Call<Classroom> call, Response<Classroom> response) {
                if(response.isSuccessful()){
                    result.setValue(response.body());
                }else{
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Classroom> call, Throwable t) {
                Log.e("api failed", t.toString());
            }
        });
        return  result;
    }

    public LiveData<Classroom> updateClassroom(Classroom classroom){
        MutableLiveData<Classroom> result = new MutableLiveData<>();
        Call<Classroom> call = iClassroomService.updateClassroom(classroom);
        call.enqueue(new Callback<Classroom>() {
            @Override
            public void onResponse(Call<Classroom> call, Response<Classroom> response) {
                if(response.isSuccessful()){
                    result.setValue(response.body());
                }else{
                    result.setValue(null);
                }
            }
            @Override
            public void onFailure(Call<Classroom> call, Throwable t) {
                Log.e("api failed", t.toString());
            }
        });
        return  result;
    }

}
