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
import com.wnw.attendanceadmin.bean.User;
import com.wnw.attendanceadmin.bean.Wifi;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by wnw on 2018/4/7.
 */

public class StudentActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private TextView nothingView;

    private List<User> userList = new ArrayList<>();
    private StudentAdapter studentAdapter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 菜单选中监听
        int id = item.getItemId();
        if (id == R.id.action_add) {
            startActivityForResult(new Intent(this, AddStudentActivity.class), 1001);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initView(){
        recyclerView = (RecyclerView)findViewById(R.id.rv);
        nothingView = (TextView)findViewById(R.id.tv_nothing);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中，请稍后...");

        studentAdapter = new StudentAdapter(this, userList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(studentAdapter);
        findStudents();
    }

    private void findStudents(){
        progressDialog.show();
        BmobQuery<User> query = new BmobQuery<User>();
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                progressDialog.dismiss();
                if (e == null && list.size() > 0){
                    Log.e("attendanceTAG", "list size =" + list.size());
                    userList = list;
                    nothingView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    updateRv();
                }else {
                    Log.e("attendanceTAG", "list size is 0");
                    e.printStackTrace();
                    nothingView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void updateRv(){
        Log.e("attendanceTAG", "update rv");
        studentAdapter.setList(userList);
        studentAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK){
            findStudents();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
