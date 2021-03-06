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
import com.wnw.attendanceadmin.bean.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by wnw on 2018/4/8.
 */

public class UpdateStudentActivity extends AppCompatActivity{

    private EditText sidEt;
    private EditText nameEt;
    private EditText passwordEt;
    private TextView finishTv;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        initView();
    }

    private void initView(){
        sidEt = (EditText)findViewById(R.id.et_sid);
        nameEt = (EditText)findViewById(R.id.et_name);
        passwordEt = (EditText)findViewById(R.id.et_password);
        sidEt.setText(user.getsId());
        nameEt.setText(user.getName());
        passwordEt.setText(user.getPassword());
        finishTv = (TextView)findViewById(R.id.tv_finish_insert);
        finishTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStudent();
            }
        });

        findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
    }

    private void delete(){
        user.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Toast.makeText(UpdateStudentActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }else {
                    Toast.makeText(UpdateStudentActivity.this, "删除成功，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateStudent(){
        if(sidEt.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "学号不能为空",Toast.LENGTH_SHORT).show();
        }else if (nameEt.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "姓名不能为空",Toast.LENGTH_SHORT).show();
        }else if (passwordEt.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "密码不能为空",Toast.LENGTH_SHORT).show();
        }else{
            user.setsId(sidEt.getText().toString().trim());
            user.setName(nameEt.getText().toString().trim());
            user.setPassword(passwordEt.getText().toString().trim());
            user.update(user.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e == null){
                        Toast.makeText(UpdateStudentActivity.this, "更新成功",Toast.LENGTH_SHORT).show();
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
