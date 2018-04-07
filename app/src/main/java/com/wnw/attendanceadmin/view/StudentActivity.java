package com.wnw.attendanceadmin.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wnw.attendanceadmin.R;

/**
 * Created by wnw on 2018/4/7.
 */

public class StudentActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        initView();
    }

    private void initView(){

    }
}
