package com.wnw.attendanceadmin.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wnw.attendanceadmin.R;
import com.wnw.attendanceadmin.bean.Wifi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * Created by wnw on 2018/4/7.
 */

public class WifiActivity extends AppCompatActivity{

    private RecyclerView wifiRv;
    private TextView nothingView;

    private List<Wifi> wifiList = new ArrayList<>();
    private WifiAdapter wifiAdapter;

    private ProgressDialog progressDialog;

    public boolean isAddAttendance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        Intent intent = getIntent();
        isAddAttendance = intent.getBooleanExtra("addAttendance",false);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wifi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 菜单选中监听
        int id = item.getItemId();
        if (id == R.id.action_add_wifi) {
            startActivityForResult(new Intent(this, AddWifiActivity.class), 1001);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initView(){
        wifiRv = (RecyclerView)findViewById(R.id.rv_wifi);
        nothingView = (TextView)findViewById(R.id.tv_nothing);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中，请稍后...");

        wifiAdapter = new WifiAdapter(this, wifiList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        wifiRv.setLayoutManager(linearLayoutManager);
        wifiRv.setAdapter(wifiAdapter);
        findWifi();
    }

    private void findWifi(){
        progressDialog.show();
        BmobQuery<Wifi> wifiBmobQuery = new BmobQuery<Wifi>();
        wifiBmobQuery.findObjects(new FindListener<Wifi>() {
            @Override
            public void done(List<Wifi> list, BmobException e) {
                progressDialog.dismiss();
                if (e == null && list.size() > 0){
                    Log.e("attendanceTAG", "list size =" + list.size());
                    wifiList = list;
                    nothingView.setVisibility(View.GONE);
                    wifiRv.setVisibility(View.VISIBLE);
                    updateRv();
                }else {
                    Log.e("attendanceTAG", "list size is 0");
                    e.printStackTrace();
                    nothingView.setVisibility(View.VISIBLE);
                    wifiRv.setVisibility(View.GONE);
                }
            }
        });
    }

    private void updateRv(){
        Log.e("attendanceTAG", "update rv");
        wifiAdapter.setWifiList(wifiList);
        wifiAdapter.notifyDataSetChanged();
        wifiAdapter.isAttendance(isAddAttendance);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK){
            findWifi();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
