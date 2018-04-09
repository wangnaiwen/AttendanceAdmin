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
import com.wnw.attendanceadmin.bean.Attendance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by wnw on 2018/4/1.
 */

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceHolder>{

    private Context context;
    private List<Attendance> attendanceList;

    public AttendanceAdapter(Context context, List<Attendance> attendances) {
        this.context = context;
        this.attendanceList = attendances;
    }

    public void setAttendanceList(List<Attendance> attendanceList) {
        this.attendanceList = attendanceList;
    }

    @Override
    public AttendanceAdapter.AttendanceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attendance, parent, false);
        return new AttendanceHolder(view);
    }

    @Override
    public void onBindViewHolder(AttendanceHolder holder, final int position) {
        final Attendance attendance = attendanceList.get(position);
        Date startDate = new Date(attendance.getStartTime());
        Date endDate = new Date(attendance.getEndTime());
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
        holder.startTimeTv.setText("考勤开始：" + sdf.format(startDate));
        holder.endTimeTv.setText("考勤结束：" + sdf.format(endDate));
        holder.addressTv.setText("考勤地点：" + attendance.getAddress());
        holder.attendacenRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                ((Activity)context).startActivityForResult(new Intent(context, RecordActivity.class).putExtra(
                        "attendance", attendance), 1001);
                ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    class AttendanceHolder extends RecyclerView.ViewHolder{
        TextView startTimeTv;
        TextView endTimeTv;
        TextView addressTv;
        RelativeLayout attendacenRl;

        public AttendanceHolder(View itemView) {
            super(itemView);
            attendacenRl = (RelativeLayout)itemView.findViewById(R.id.rl_attendance);
            startTimeTv = (TextView)itemView.findViewById(R.id.start_time);
            endTimeTv = (TextView)itemView.findViewById(R.id.end_time);
            addressTv = (TextView)itemView.findViewById(R.id.address);
        }
    }
}
