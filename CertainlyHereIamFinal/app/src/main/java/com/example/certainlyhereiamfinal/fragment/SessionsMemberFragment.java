package com.example.certainlyhereiamfinal.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.certainlyhereiamfinal.R;
import com.example.certainlyhereiamfinal.adapter.SessionAdapter;
import com.example.certainlyhereiamfinal.adapter.SessionItemListenner;
import com.example.certainlyhereiamfinal.model.Session;
import com.example.certainlyhereiamfinal.store.DataLocalManager;
import com.example.certainlyhereiamfinal.viewmodel.MemberViewModel;
import com.example.certainlyhereiamfinal.viewmodel.SessionViewModel;

public class SessionsMemberFragment extends Fragment implements SessionItemListenner {
    private SessionAdapter sessionAdapter;
    private RecyclerView recyclerView;
    private SessionViewModel sessionViewModel;
    private MemberViewModel memberViewModel;
    private TextView total_session, number_member;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sessions_member_fragment, container, false);
        sessionAdapter = new SessionAdapter(root.getContext(), false, this);
        recyclerView = root.findViewById(R.id.sessions_member_recyclerview);
        sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);
        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        total_session = root.findViewById(R.id.total_session);
        number_member = root.findViewById(R.id.number_member);
        loadSessions(root);
        return root;
    }

    private void loadSessions(View root){
        memberViewModel.getNumberMember(DataLocalManager.getClassId()).observe((LifecycleOwner) root.getContext(), data -> {
            number_member.setText("Members: "+data);
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        sessionViewModel.findAllSessionClass(DataLocalManager.getClassId()).observe((LifecycleOwner) requireContext(), data -> {
            if(data != null){
                sessionAdapter.setSessions(data);
                total_session.setText("Total sessions: "+data.size());
                recyclerView.setAdapter(sessionAdapter);
            }
        });
    }

    @Override
    public void onSeeMoreClicked(View view, Session session) {

    }
}
