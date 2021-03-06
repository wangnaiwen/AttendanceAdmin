package com.wnw.attendanceadmin.view;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wnw.attendanceadmin.R;
import com.wnw.attendanceadmin.bean.Wifi;
import com.wnw.attendanceadmin.util.NetWorkUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.qqtheme.framework.AppConfig;

/**
 * Created by wnw on 2018/4/7.
 */

public class AddWifiActivity extends AppCompatActivity{

    private EditText addressEt;
    private EditText wifiEt;
    private TextView finishTv;

    private Wifi wifi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wifi);
        initView();
    }

    private void initView(){
        wifi = new Wifi();

        addressEt = (EditText)findViewById(R.id.et_address);
        wifiEt = (EditText)findViewById(R.id.et_wifi);
        finishTv = (TextView)findViewById(R.id.tv_finish_insert);
        finishTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertWifi();
            }
        });

        if (NetWorkUtils.getConnectedType(this) != 1){
            Toast.makeText(this, "请连接到要添加的wifi",Toast.LENGTH_SHORT).show();
        }else{
            WifiManager wifi_service = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo    = wifi_service.getConnectionInfo();
            Log.e("wnw", "wifi" + wifiInfo.getBSSID());
            wifiEt.setText(wifiInfo.getBSSID());
        }
    }



    private void insertWifi(){
        if(wifiEt.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Wifi不能为空",Toast.LENGTH_SHORT).show();
        }else if (addressEt.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "地址不能为空",Toast.LENGTH_SHORT).show();
        }else{
            wifi.setMac(wifiEt.getText().toString().trim());
            wifi.setAddress(addressEt.getText().toString().trim());
            wifi.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e == null){
                        Toast.makeText(AddWifiActivity.this, "添加成功",Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
