package com.example.certainlyhereiamfinal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.certainlyhereiamfinal.R;
import com.example.certainlyhereiamfinal.model.Member;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private Context context;
    private List<Member> members;

    public AttendanceAdapter(Context context) {
        this.context = context;
    }

    public void setStudents(List<Member> members) {
        this.members = members;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Member member = members.get(position);
        if (member != null) {
            if (member.getUser().getAvatar() == null || member.getUser().getAvatar().equals("")) {
                Picasso.get().load("https://th.bing.com/th/id/OIP.pG2K1J6nAStrFbo1rsU-VwAAAA?rs=1&pid=ImgDetMain").into(holder.avatar);
            } else {
                Picasso.get().load(member.getUser().getAvatar()).into(holder.avatar);
            }
            String nameStr = (member.getUser().getFullname().equals("") || member.getUser().getFullname() == null) ? "Not updated" : member.getUser().getFullname();
            String idStr = (member.getUser().getIdentifier().equals("") || member.getUser().getIdentifier() == null) ? "Not updated" : member.getUser().getIdentifier();
            holder.name.setText("Email: "+member.getUser().getEmail());
            holder.identifier.setText("ID: "+idStr);
            holder.fullname.setText(nameStr);
            holder.icon_trash.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return members != null ? members.size() : 0;
    }


    public static String formatDate(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView avatar;

        private TextView name, identifier, fullname;
        private ImageView icon_trash;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
            identifier = itemView.findViewById(R.id.identifier);
            icon_trash = itemView.findViewById(R.id.icon_trash);
            fullname = itemView.findViewById(R.id.fullname);
        }
    }
}
