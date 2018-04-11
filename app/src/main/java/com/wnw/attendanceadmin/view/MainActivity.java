package com.wnw.attendanceadmin.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wnw.attendanceadmin.R;
import com.wnw.attendanceadmin.bean.Attendance;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.qqtheme.framework.picker.DatePicker;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private TextView nothingTv;

    RecyclerView mRecyclerView;

    ProgressDialog progressDialog ;

    AttendanceAdapter attendanceAdapter ;

    private List<Attendance> attendanceList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    /**
     * 初始化控件
     * */
    private void initView(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中，请稍等...");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, AddAttendanceActivity.class), 1001);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Date date = new Date();
        long l = 24*60*60*1000; //每天的毫秒数
        //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
        //减8个小时的毫秒值是为了解决时区的问题。

        final long startTime = (date.getTime() - (date.getTime()%l) - 8* 60 * 60 *1000);
        final long endTime = startTime + 24 * 60 * 60 * 1000;

        Log.e("AttendanceWnw", startTime + " " +endTime);
        findAttendance(startTime, endTime);

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler);
        nothingTv = (TextView)findViewById(R.id.tv_nothing);
        nothingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findAttendance(startTime, endTime);
            }
        });

        attendanceAdapter = new AttendanceAdapter(this, attendanceList);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(attendanceAdapter);
    }

    private void findAttendance(long startTime, long endTime){
        progressDialog.show();
        BmobQuery<Attendance> query = new BmobQuery<Attendance>();
        query.addWhereLessThan("startTime", endTime);
        query.addWhereGreaterThan("startTime", startTime);
        query.findObjects(new FindListener<Attendance>() {
            @Override
            public void done(List<Attendance> list, BmobException e) {
                if (e == null && list.size() > 0){
                    //刷新视图
                    progressDialog.dismiss();
                    Log.e("AttendanceTAG", " " + list.size());
                    attendanceList = list;
                    updateRv();
                }else{
                    progressDialog.dismiss();
                    e.printStackTrace();
                    mRecyclerView.setVisibility(View.GONE);                    nothingTv.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001 && resultCode == RESULT_OK){

            Date date = new Date();
            long l = 24*60*60*1000; //每天的毫秒数
            //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
            //减8个小时的毫秒值是为了解决时区的问题。

            final long startTime = (date.getTime() - (date.getTime()%l) - 8* 60 * 60 *1000);
            final long endTime = startTime + 24 * 60 * 60 * 1000;

            findAttendance(startTime, endTime);
        }
    }

    private void updateRv(){
        mRecyclerView.setVisibility(View.VISIBLE);
        nothingTv.setVisibility(View.GONE);
        attendanceAdapter.setAttendanceList(attendanceList);
        attendanceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 菜单选中监听
        int id = item.getItemId();
        if (id == R.id.action_pick_date) {
            DatePicker datePicker = new DatePicker(this);
            datePicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                @Override
                public void onDatePicked(String year, String month, String day) {
                    try {
                        Log.e("AttendanceWnw", year+"-"+month+"-"+day );
                        Date date = converToDate(year+"-"+month+"-"+day);
                        long l = 24*60*60*1000; //每天的毫秒数
                        //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
                        //减8个小时的毫秒值是为了解决时区的问题。
                        Log.e("AttendanceWnw", date.getTime()+"");
                        final long startTime = date.getTime();
                        //final long startTime = (date.getTime() - (date.getTime()%l) - 8* 60 * 60 *1000);
                        final long endTime = startTime + 24 * 60 * 60 * 1000;
                        Log.e("AttendanceWnw", startTime + " " +endTime);
                        findAttendance(startTime, endTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            datePicker.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //把字符串转为日期
    public static Date converToDate(String strDate) throws Exception
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(strDate);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_setting) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if (id == R.id.nav_leave){
            Intent intent = new Intent(MainActivity.this, LeaveActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if (id == R.id.nav_student){
            Intent intent = new Intent(MainActivity.this, StudentActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if (id == R.id.nav_wifi){
            Intent intent = new Intent(MainActivity.this, WifiActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        return true;
    }
}

