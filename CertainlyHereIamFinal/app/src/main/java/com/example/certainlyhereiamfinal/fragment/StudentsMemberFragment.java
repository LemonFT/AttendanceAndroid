package com.example.certainlyhereiamfinal.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.certainlyhereiamfinal.R;
import com.example.certainlyhereiamfinal.adapter.MemberAdapter;
import com.example.certainlyhereiamfinal.adapter.MemberItemListenner;
import com.example.certainlyhereiamfinal.model.Member;
import com.example.certainlyhereiamfinal.store.DataLocalManager;
import com.example.certainlyhereiamfinal.viewmodel.MemberViewModel;

public class StudentsMemberFragment extends Fragment implements MemberItemListenner {

    RecyclerView recyclerView;
    MemberAdapter memberAdapter;

    MemberViewModel memberViewModel;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.student_member_fragment, container, false);
        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        loadStudentsInClass(root.getContext(), root);
        return root;
    }

    public void loadStudentsInClass(Context context, View view) {
        memberAdapter = new MemberAdapter(context, StudentsMemberFragment.this, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView = view.findViewById(R.id.studentsfr_recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        memberViewModel.getMemberInClass(DataLocalManager.getClassId()).observe((LifecycleOwner) context, data -> {
            memberAdapter.setStudents(data);
            recyclerView.setAdapter(memberAdapter);
        });
    }

    @Override
    public void onDeleteMember(View view, Member member) {

    }
}
