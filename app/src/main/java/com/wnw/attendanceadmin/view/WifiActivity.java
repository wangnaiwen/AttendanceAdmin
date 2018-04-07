package com.wnw.attendanceadmin.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wnw.attendanceadmin.R;
import com.wnw.attendanceadmin.bean.Wifi;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by wnw on 2018/4/7.
 */

public class WifiActivity extends AppCompatActivity{

    private RecyclerView wifiRv;
    private TextView nothingView;

    private List<Wifi> wifiList = new ArrayList<>();
    private WifiAdapter wifiAdapter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        initView();
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
                    nothingView.setVisibility(View.GONE);
                    wifiRv.setVisibility(View.VISIBLE);
                    updateRv();
                }else {
                    e.printStackTrace();
                    nothingView.setVisibility(View.VISIBLE);
                    wifiRv.setVisibility(View.GONE);
                }
            }
        });
    }

    private void updateRv(){
        wifiAdapter.setWifiList(wifiList);
        wifiAdapter.notifyDataSetChanged();
    }
}
