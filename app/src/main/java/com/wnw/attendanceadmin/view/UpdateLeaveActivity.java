package com.wnw.attendanceadmin.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wnw.attendanceadmin.R;
import com.wnw.attendanceadmin.bean.Attendance;
import com.wnw.attendanceadmin.bean.Leave;
import com.wnw.attendanceadmin.bean.Record;
import com.wnw.attendanceadmin.bean.User;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by wnw on 2018/4/9.
 */

public class UpdateLeaveActivity extends AppCompatActivity{

    private ProgressDialog progressDialog;
    private TextView sidTv;
    private TextView nameTv;
    private TextView timeTv;
    private TextView greenTv;
    private TextView refustTv;

    private Leave leave;

    private Attendance mAttendance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_leave);
        Intent intent = getIntent();
        leave = (Leave) intent.getSerializableExtra("leave");
        initView();
    }

    private void initView(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中，请稍后...");

        sidTv = (TextView)findViewById(R.id.sid);
        nameTv = (TextView)findViewById(R.id.name);
        timeTv = (TextView)findViewById(R.id.time);
        greenTv = (TextView)findViewById(R.id.green);
        refustTv = (TextView)findViewById(R.id.refust);
        sidTv.setText(leave.getsId());
        nameTv.setText(leave.getName());

        greenTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLeave("已审批");
            }
        });

        refustTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLeave("已拒绝");
            }
        });
        findAttendance();
    }

    private void findAttendance(){
        progressDialog.show();
        BmobQuery<Attendance> attendanceBmobQuery = new BmobQuery<>();
        attendanceBmobQuery.getObject(leave.getAttendanceId(), new QueryListener<Attendance>() {
            @Override
            public void done(final Attendance attendance, BmobException e) {
                progressDialog.dismiss();
                if(e == null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAttendance = attendance;
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm");
                            String startTime = simpleDateFormat.format(new Date(attendance.getStartTime()));
                            String endTime = simpleDateFormat.format(new Date(attendance.getEndTime()));
                            timeTv.setText(startTime+" 至 " + endTime);
                        }
                    });
                }else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateLeave(String status){
        progressDialog.show();
        leave.setStatus(status);
        leave.update(leave.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                progressDialog.dismiss();
                if(e == null){
                    insertRecord();
                }else {
                    Toast.makeText(UpdateLeaveActivity.this, "更新失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void insertRecord(){
        Record record =  new Record();
        record.setAddress(mAttendance.getAddress());
        record.setsId(leave.getsId());
        record.setResult("请假");
        record.setRecordTime(mAttendance.getStartTime());
        record.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    Toast.makeText(UpdateLeaveActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }else {
                    Toast.makeText(UpdateLeaveActivity.this, "更新失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
