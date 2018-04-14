package com.wnw.attendanceadmin.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wnw.attendanceadmin.R;
import com.wnw.attendanceadmin.bean.Attendance;
import com.wnw.attendanceadmin.bean.User;
import com.wnw.attendanceadmin.bean.Wifi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.TimePicker;

/**
 * Created by wnw on 2018/4/11.
 */

public class UpdateAttendanceActivity extends AppCompatActivity{
    private EditText startTime;
    private EditText endTime;
    private EditText address;
    private TextView finishTv;

    private User user;

    private Attendance attendance;
    private String startYear;
    private String startMonth;
    private String startDay;
    private String endYear;
    private String endMonth;
    private String endDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_attendance);
        Intent intent = getIntent();
        attendance = (Attendance) intent.getSerializableExtra("attendance");
        initView();
    }

    private void initView(){
        startTime = (EditText)findViewById(R.id.start_time);
        endTime = (EditText)findViewById(R.id.end_time);
        address = (EditText)findViewById(R.id.address);
        finishTv = (TextView)findViewById(R.id.tv_finish_insert);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        startTime.setText(df.format(new Date(attendance.getStartTime())));
        endTime.setText(df.format(new Date(attendance.getEndTime())));
        address.setText(attendance.getAddress());

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePicker datePicker = new DatePicker(UpdateAttendanceActivity.this);
                datePicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        try {
                            attendance.setStartYear(Integer.parseInt(year));
                            attendance.setStartMonth(Integer.parseInt(month));
                            attendance.setStartDay(Integer.parseInt(day));
                            startYear = year;
                            startMonth = month;
                            startDay = day;
                            TimePicker timePicker = new TimePicker(UpdateAttendanceActivity.this);
                            timePicker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                                @Override
                                public void onTimePicked(String hour, String minute) {
                                    try {
                                        Date date = converToDate(startYear+"-"+startMonth+"-"+startDay+" "+ hour+":" +minute);
                                        attendance.setStartTime(date.getTime());
                                        startTime.setText(startYear+"-"+startMonth+"-"+startDay+" "+ hour+":" +minute);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            timePicker.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                datePicker.show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(startYear)){
                    Toast.makeText(UpdateAttendanceActivity.this, "请先选择开始时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                endYear = startYear;
                endMonth = startMonth;
                endDay = startDay;
                attendance.setEndYear(Integer.parseInt(endYear));
                attendance.setEadMonth(Integer.parseInt(endMonth));
                attendance.setEndDay(Integer.parseInt(endDay));
                TimePicker timePicker = new TimePicker(UpdateAttendanceActivity.this);
                timePicker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                    @Override
                    public void onTimePicked(String hour, String minute) {
                        try {
                            Date date = converToDate(startYear+"-"+startMonth+"-"+startDay+" "+ hour+":" +minute);
                            attendance.setEndTime(date.getTime());
                            endTime.setText(startYear+"-"+startMonth+"-"+startDay+" "+ hour+":" +minute);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                timePicker.show();
            }
        });

        finishTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAttendance();
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateAttendanceActivity.this, WifiActivity.class).putExtra("addAttendance", true);
                startActivityForResult(intent, 1001);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
    }

    //把字符串转为日期
    public static Date converToDate(String strDate) throws Exception
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.parse(strDate);
    }
    private void updateAttendance(){
        if(startTime.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "开始时间不能为空",Toast.LENGTH_SHORT).show();
        }else if (endTime.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "结束时间不能为空",Toast.LENGTH_SHORT).show();
        }else if (address.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "考勤地址不能为空",Toast.LENGTH_SHORT).show();
        }else{
            attendance.update(attendance.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e == null){
                        Toast.makeText(UpdateAttendanceActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }else {
                        Toast.makeText(UpdateAttendanceActivity.this, "更新失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK){
            Wifi wifi = (Wifi)data.getSerializableExtra("wifi");
            attendance.setWifiId(wifi.getObjectId());
            attendance.setAddress(wifi.getAddress());
            address.setText(wifi.getAddress());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void delete(){
        attendance.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Toast.makeText(UpdateAttendanceActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }else {
                    Toast.makeText(UpdateAttendanceActivity.this, "删除成功，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
