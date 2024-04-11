package com.example.certainlyhereiamfinal.adapter;

import android.content.Context;
import android.content.Intent;
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

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private Context context;

    private List<Member> members;
    private MemberItemListenner memberItemListenner;
    private  boolean noDelete;

    public MemberAdapter(Context context, MemberItemListenner memberItemListenner, boolean noDelete) {
        this.context = context;
        this.memberItemListenner = memberItemListenner;
        this.noDelete = noDelete;
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
            String nameStr = member.getUser().getFullname() == null || member.getUser().getFullname().equals("")  ? "Not updated" : member.getUser().getFullname();
            String idStr = member.getUser().getIdentifier() == null || member.getUser().getIdentifier().equals("")  ? "Not updated" : member.getUser().getIdentifier();
            holder.name.setText("Email: "+member.getUser().getEmail());
            holder.identifier.setText("ID: "+idStr);
            holder.fullname.setText(nameStr);

            if(noDelete){
                holder.icon_trash.setVisibility(View.GONE);
            }
            holder.icon_trash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    memberItemListenner.onDeleteMember(v, member);
                }
            });
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    private void seeProfile(){
        Intent intent = new Intent();
    }

    @Override
    public int getItemCount() {
        return members.isEmpty() ? 0 : members.size();
    }


    public static String formatDate(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView avatar;

        private TextView name, identifier, fullname;
        private ImageView icon_trash;

        private LinearLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
            identifier = itemView.findViewById(R.id.identifier);
            fullname = itemView.findViewById(R.id.fullname);
            icon_trash = itemView.findViewById(R.id.icon_trash);
            container = itemView.findViewById(R.id.container);
        }
    }
}
