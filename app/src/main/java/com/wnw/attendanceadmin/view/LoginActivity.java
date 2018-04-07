package com.wnw.attendanceadmin.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wnw.attendanceadmin.R;
import com.wnw.attendanceadmin.bean.Admin;
import com.wnw.attendanceadmin.config.KeyInfo;
import com.wnw.attendanceadmin.util.ActivityCollector;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by wnw on 2017/4/21.
 */

public class LoginActivity extends Activity implements
        View.OnClickListener{

    private EditText username;      //用户名输入框
    private EditText password;   //密码输入框
    private Button login;        //登录按钮
    private Admin admin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this, KeyInfo.applicationID);
        //如果已经登录过，并且没有退出账号，默认登录，直接跳转到MainActivity
        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        if(sharedPreferences != null){
            String id = sharedPreferences.getString("id", "");
            if(!id.equals("")){  //说明已经登录
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
        initView();
        ActivityCollector.addActivity(this);
    }

    //初始化View
    private void initView(){
        username = (EditText)findViewById(R.id.store_login_username);
        password = (EditText)findViewById(R.id.store_login_password);
        login = (Button)findViewById(R.id.btn_store_login);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_store_login:
                if(validateEditText()){
                    Toast.makeText(this, "手机和密码都不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "正在拼命登录中...",Toast.LENGTH_SHORT).show();
                    //验证密码
                    admin = new Admin();
                    admin.setObjectId(username.getText().toString().trim());
                    admin.setPassword(password.getText().toString().trim());
                    //开始获得数据
                    findAdmin();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 验证两个EditText是否都已经不为空了
     * */
    private boolean validateEditText(){
        return username.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty();
    }

    //登录云端，查找账号，返回长度为0代表不成功，大于零则成功
    private void findAdmin(){
        BmobQuery<Admin> query = new BmobQuery<Admin>();
        query.addWhereEqualTo("userName",admin.getUserName());
        query.addWhereEqualTo("password", admin.getPassword());
        query.findObjects(new FindListener<Admin>() {
            @Override
            public void done(List<Admin> object, BmobException e) {
                if(e==null){
                    if(object != null){
                        int length = object.size();
                        if(length > 0){
                            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                            openMainAty();
                        }else {
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    //登录成功后，打开MainActivity
    private void openMainAty(){
        saveAccount();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("admin",admin);
        startActivity(intent);
        ActivityCollector.finishAllActivity();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    //登录成功，保存登录账号信息到本地
    private void saveAccount(){
        SharedPreferences.Editor editor = getSharedPreferences("account",
                MODE_PRIVATE).edit();
        editor.clear();
        editor.putString("userName", admin.getUserName());
        editor.apply();
    }
}
