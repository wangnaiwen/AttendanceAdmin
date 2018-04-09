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
import com.wnw.attendanceadmin.bean.Leave;

import java.util.List;

/**
 * Created by wnw on 2018/4/7.
 */

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.VHolder>{
    private Context context;
    private List<Leave> list;

    public LeaveAdapter(Context context, List<Leave> users) {
        this.context = context;
        this.list = users;
    }

    public void setList(List<Leave> users) {
        this.list = users;
    }

    @Override
    public LeaveAdapter.VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_leave, parent, false);
        return new LeaveAdapter.VHolder(view);
    }

    @Override
    public void onBindViewHolder(LeaveAdapter.VHolder holder, final int position) {
        Leave leave = list.get(position);
        holder.sidTv.setText("学号：" + leave.getsId());
        holder.nameTv.setText("姓名：" + leave.getName());
        holder.resultTv.setText("状态：" + leave.getStatus());
        holder.itemRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).getStatus().equals("待审批")){
                    ((Activity)context).startActivityForResult(new Intent(context, UpdateLeaveActivity.class).putExtra(
                            "leave", list.get(position)), 1001);
                    ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
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
        TextView resultTv;

        public VHolder(View itemView) {
            super(itemView);
            sidTv = (TextView)itemView.findViewById(R.id.sid);
            nameTv = (TextView)itemView.findViewById(R.id.name);
            resultTv = (TextView)itemView.findViewById(R.id.result);
            itemRl = (RelativeLayout)itemView.findViewById(R.id.rl);
        }
    }
}

