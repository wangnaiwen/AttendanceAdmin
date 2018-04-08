package com.wnw.attendanceadmin.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wnw.attendanceadmin.R;
import com.wnw.attendanceadmin.bean.User;

import java.util.List;

/**
 * Created by wnw on 2018/4/7.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.VHolder>{
    private Context context;
    private List<User> list;

    public StudentAdapter(Context context, List<User> users) {
        this.context = context;
        this.list = users;
    }

    public void setList(List<User> users) {
        this.list = users;
    }

    @Override
    public StudentAdapter.VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return new StudentAdapter.VHolder(view);
    }

    @Override
    public void onBindViewHolder(StudentAdapter.VHolder holder, final int position) {
        User user = list.get(position);
        holder.sidTv.setText("学号：" + user.getsId());
        holder.nameTv.setText("姓名：" + user.getName());
        holder.passwordTv.setText("密码：" + user.getPassword());
        holder.itemRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)context).startActivityForResult(new Intent(context, UpdateStudentActivity.class).putExtra(
                        "user", list.get(position)), 1001);
                ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class VHolder extends RecyclerView.ViewHolder{
        RelativeLayout itemRl;
        TextView sidTv;
        TextView nameTv;
        TextView passwordTv;

        public VHolder(View itemView) {
            super(itemView);
            sidTv = (TextView)itemView.findViewById(R.id.sid);
            nameTv = (TextView)itemView.findViewById(R.id.name);
            passwordTv = (TextView)itemView.findViewById(R.id.password);
            itemRl = (RelativeLayout)itemView.findViewById(R.id.rl);
        }
    }
}
