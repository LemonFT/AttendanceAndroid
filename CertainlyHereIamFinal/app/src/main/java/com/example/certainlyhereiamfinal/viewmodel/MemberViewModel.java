package com.example.certainlyhereiamfinal.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.certainlyhereiamfinal.api.IClassroomService;
import com.example.certainlyhereiamfinal.api.IMemberService;
import com.example.certainlyhereiamfinal.api.RetrofitClient;
import com.example.certainlyhereiamfinal.model.Member;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberViewModel extends ViewModel {
    private IMemberService iMemberService;

    public MemberViewModel() {
        iMemberService = RetrofitClient.getRetrofitInstance().create(IMemberService.class);
    }

    public LiveData<Integer> getNumberMember(Long classroomId){
        MutableLiveData<Integer> result = new MutableLiveData<>();
        Call<Integer> call = iMemberService.getNumberMember(classroomId);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    result.setValue(response.body());
                }else {
                    result.setValue(0);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("api failed", t.toString());
            }
        });
        return result;
    }

    public LiveData<List<Member>> getMemberInClass(Long classroomId){
        MutableLiveData<List<Member>> result = new MutableLiveData<>();
        Call<List<Member>> call = iMemberService.getMember(classroomId);
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

}
