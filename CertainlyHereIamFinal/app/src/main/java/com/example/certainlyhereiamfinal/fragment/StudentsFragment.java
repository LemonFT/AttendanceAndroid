package com.example.certainlyhereiamfinal.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.certainlyhereiamfinal.Global;
import com.example.certainlyhereiamfinal.R;
import com.example.certainlyhereiamfinal.activity.MainActivity;
import com.example.certainlyhereiamfinal.adapter.MemberAdapter;
import com.example.certainlyhereiamfinal.adapter.MemberItemListenner;
import com.example.certainlyhereiamfinal.model.Member;
import com.example.certainlyhereiamfinal.store.DataLocalManager;
import com.example.certainlyhereiamfinal.viewmodel.MemberViewModel;

public class StudentsFragment extends Fragment implements MemberItemListenner {

    RecyclerView recyclerView;
    MemberAdapter studentAdapter;

    MemberViewModel memberViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.students_fragment, container, false);
        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        loadStudentsInClass(root.getContext(), root);
        return root;
    }

    public void loadStudentsInClass(Context context, View view) {
        studentAdapter = new MemberAdapter(context, StudentsFragment.this, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView = view.findViewById(R.id.studentsfr_recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        memberViewModel.getMemberInClass(DataLocalManager.getClassId()).observe((LifecycleOwner) context, data -> {
            studentAdapter.setStudents(data);
            recyclerView.setAdapter(studentAdapter);
        });
    }

    @Override
    public void onDeleteMember(View view, Member member) {
        new AlertDialog.Builder(StudentsFragment.this.getContext())
                .setTitle("Delete member")
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        memberViewModel.outClass(DataLocalManager.getClassId(), member.getUser().getId())
                                .observe((LifecycleOwner) StudentsFragment.this, data -> {
                                    if (data.equals("You have left the class")) {
                                        Global.showAlertSuccess("Deleted member successful", StudentsFragment.this.getContext());
                                        loadStudentsInClass(StudentsFragment.this.getContext(), StudentsFragment.this.getView());
                                    } else {
                                        Global.showAlert(data, StudentsFragment.this.getContext());
                                        loadStudentsInClass(StudentsFragment.this.getContext(), StudentsFragment.this.getView());
                                    }
                                });
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();

    }
}
