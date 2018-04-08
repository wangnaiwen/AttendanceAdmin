package com.wnw.attendanceadmin.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wnw.attendanceadmin.R;
import com.wnw.attendanceadmin.bean.Wifi;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by wnw on 2018/4/7.
 */

public class UpdateWifiActivity extends AppCompatActivity {
    private EditText addressEt;
    private EditText wifiEt;
    private TextView finishTv;

    private Wifi wifi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_wifi);
        Intent intent = getIntent();
        wifi = (Wifi)intent.getSerializableExtra("wifi");
        initView();
    }

    private void initView(){
        addressEt = (EditText)findViewById(R.id.et_address);
        wifiEt = (EditText)findViewById(R.id.et_wifi);
        finishTv = (TextView)findViewById(R.id.tv_finish_insert);
        wifiEt.setText(wifi.getMac());
        addressEt.setText(wifi.getAddress());
        finishTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateWifi();
            }
        });
    }

    private void updateWifi(){
        if(wifiEt.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Wifi不能为空",Toast.LENGTH_SHORT).show();
        }else if (addressEt.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "地址不能为空",Toast.LENGTH_SHORT).show();
        }else{
            wifi.setMac(wifiEt.getText().toString().trim());
            wifi.setAddress(addressEt.getText().toString().trim());
            wifi.update(wifi.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e == null){
                        Toast.makeText(UpdateWifiActivity.this, "更新成功",Toast.LENGTH_SHORT).show();
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