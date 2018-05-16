package com.wnw.attendanceadmin.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.TimePicker;

/**
 * Created by wnw on 2018/4/9.
 */

public class AddAttendanceActivity extends AppCompatActivity{
    //开始时间，结束时间，地点，插入按钮等
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
        setContentView(R.layout.activity_add_attendance);
        initView();
    }

    //初始化界面
    private void initView(){
        attendance = new Attendance();

        startTime = (EditText)findViewById(R.id.start_time);
        endTime = (EditText)findViewById(R.id.end_time);
        address = (EditText)findViewById(R.id.address);
        finishTv = (TextView)findViewById(R.id.tv_finish_insert);
        //点击开始选择日期
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePicker datePicker = new DatePicker(AddAttendanceActivity.this);
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
                            //选好日期后，选择时间
                            TimePicker timePicker = new TimePicker(AddAttendanceActivity.this);
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

        //选择结束时间，但是选择之前必须先选择开始时间
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(startYear)){
                    Toast.makeText(AddAttendanceActivity.this, "请先选择开始时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                endYear = startYear;
                endMonth = startMonth;
                endDay = startDay;
                attendance.setEndYear(Integer.parseInt(endYear));
                attendance.setEadMonth(Integer.parseInt(endMonth));
                attendance.setEndDay(Integer.parseInt(endDay));
                TimePicker timePicker = new TimePicker(AddAttendanceActivity.this);
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
        //完成，然后插入数据
        finishTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertAttendance();
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAttendanceActivity.this, WifiActivity.class).putExtra("addAttendance", true);
                startActivityForResult(intent, 1001);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    //把字符串转为日期
    public static Date converToDate(String strDate) throws Exception
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.parse(strDate);
    }

    //插入打卡计划
    private void insertAttendance(){
        if(startTime.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "开始时间不能为空",Toast.LENGTH_SHORT).show();
        }else if (endTime.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "结束时间不能为空",Toast.LENGTH_SHORT).show();
        }else if (address.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "考勤地址不能为空",Toast.LENGTH_SHORT).show();
        }else{
            attendance.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e == null){
                        Toast.makeText(AddAttendanceActivity.this, "添加成功",Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }else {
                        e.printStackTrace();
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

    /**
     * 用户按下返回键
     * */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
