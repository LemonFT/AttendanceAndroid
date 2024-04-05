package com.example.certainlyhereiamfinal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.certainlyhereiamfinal.R;
import com.example.certainlyhereiamfinal.activity.AttendanceActivity;
import com.example.certainlyhereiamfinal.activity.AttendanceMemberActivity;
import com.example.certainlyhereiamfinal.model.Session;
import com.example.certainlyhereiamfinal.store.DataLocalManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.ViewHolder>{

    private Context context;
    private boolean action;
    private List<Session> sessions;

    private SessionItemListenner sessionItemListenner;

    public SessionAdapter(Context context, boolean action, SessionItemListenner sessionItemListenner) {
        this.context = context;
        this.action = action;
        this.sessionItemListenner = sessionItemListenner;
    }

    public void setSessions(List<Session> sessions){
        this.sessions = sessions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sessions_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Session session = sessions.get(position);
        if(session != null){
            if(!action){
                holder.actionBtn.setVisibility(View.GONE);
            }
            holder.date.setText(formatDate(session.getDateInit(), "HH:mm:ss dd-MM-yyyy"));
            holder.timeend.setText(formatDate(session.getTimeEnd(), "HH:mm:ss dd-MM-yyyy"));
            holder.room.setText(session.getRoom());
            holder.location.setText(session.getLocation());
            holder.actionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sessionItemListenner.onSeeMoreClicked(v, session);
                }
            });
            holder.session_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(session.getClassroom().getUser().getId() == DataLocalManager.getUserId()){
                        Intent intent = new Intent(context, AttendanceActivity.class);
                        intent.putExtra("qrCode", session.getQr());
                        intent.putExtra("classId", session.getClassroom().getId());
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }else{
                        Intent intent = new Intent(context, AttendanceMemberActivity.class);
                        intent.putExtra("qrCode", session.getQr());
                        intent.putExtra("classId", session.getClassroom().getId());
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return sessions != null ? sessions.size() : 0;
    }

    public static String formatDate(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout session_item;
        private TextView date, timeend, room, location;
        private ImageView actionBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.session_item_date);
            timeend = itemView.findViewById(R.id.session_item_timeend);
            room = itemView.findViewById(R.id.session_item_room);
            location = itemView.findViewById(R.id.session_item_location);
            session_item = itemView.findViewById(R.id.session_item);
            actionBtn = itemView.findViewById(R.id.action_update_delete);
        }
    }
}
