package com.example.certainlyhereiamfinal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.certainlyhereiamfinal.R;
import com.example.certainlyhereiamfinal.entityui.UserAttendance;
import com.example.certainlyhereiamfinal.model.Member;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder>{

    private Context context;
    private boolean delete;
    private List<Member> members;

    public StudentAdapter(Context context, boolean delete) {
        this.context = context;
        this.delete = delete;
    }

    public void setStudents(List<Member> members){
        this.members = members;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Member member = members.get(position);
        if(member != null){
            if(member.getUser().getAvatar() == null){
                Picasso.get().load("https://th.bing.com/th/id/OIP.pG2K1J6nAStrFbo1rsU-VwAAAA?rs=1&pid=ImgDetMain").into(holder.avatar);
            }else {
                Picasso.get().load(member.getUser().getAvatar()).into(holder.avatar);
            }
            holder.name.setText(member.getUser().getEmail());
            holder.identifier.setText(member.getUser().getIdentifier());
            if(!delete){
                holder.icon_trash.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return members.size();
    }


    public static String formatDate(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView avatar;
        private TextView name, identifier, time_attendance;
        private ImageView icon_trash;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
            identifier = itemView.findViewById(R.id.identifier);
            time_attendance = itemView.findViewById(R.id.time_attendance);
            icon_trash = itemView.findViewById(R.id.icon_trash);

        }
    }
}