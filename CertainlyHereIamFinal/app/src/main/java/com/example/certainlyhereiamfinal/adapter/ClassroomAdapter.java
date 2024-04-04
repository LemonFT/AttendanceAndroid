package com.example.certainlyhereiamfinal.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.certainlyhereiamfinal.R;
import com.example.certainlyhereiamfinal.activity.SessionsActivity;
import com.example.certainlyhereiamfinal.activity.SessionsMemberActivity;
import com.example.certainlyhereiamfinal.entityui.ClassroomUI;
import com.example.certainlyhereiamfinal.model.Classroom;
import com.example.certainlyhereiamfinal.store.DataLocalManager;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClassroomAdapter extends RecyclerView.Adapter<ClassroomAdapter.MyHolder>{

    private Context context;
    private List<Classroom> classrooms;

    private ClassroomItemListenner classroomItemListenner;

    public ClassroomAdapter(Context context, ClassroomItemListenner classroomItemListenner) {
        this.context = context;
        this.classroomItemListenner = classroomItemListenner;
    }

    public void setClassrooms(List<Classroom> classrooms){
        this.classrooms = classrooms;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Classroom classroom = classrooms.get(position);
        if(classroom != null){
            holder.item_class.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataLocalManager.setClassname(classroom.getName());
                    DataLocalManager.setClassId(classroom.getId());
                    if(classroom.getUser().getId() == DataLocalManager.getUserId()){
                        Intent intent = new Intent(context, SessionsActivity.class);
                        context.startActivity(intent);
                    }else{
                        Intent intent = new Intent(context, SessionsMemberActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
            holder.master_room.setText(classroom.getUser().getEmail());
            holder.class_name.setText(classroom.getName());
            holder.see_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    classroomItemListenner.onSeeMoreClicked(v, classroom);
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return classrooms.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{

        private TextView class_name, master_room;
        private CircleImageView see_more;

        private RelativeLayout item_class;



        public MyHolder(@NonNull View itemView) {
            super(itemView);
            class_name = itemView.findViewById(R.id.class_name);
            see_more = itemView.findViewById(R.id.see_more);
            item_class = itemView.findViewById(R.id.item_class);
            master_room = itemView.findViewById(R.id.master_room);
        }
    }
}
