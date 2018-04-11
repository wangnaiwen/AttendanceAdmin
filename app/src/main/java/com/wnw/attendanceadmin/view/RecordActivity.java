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
import android.widget.Toast;

import com.wnw.attendanceadmin.R;
import com.wnw.attendanceadmin.bean.Attendance;
import com.wnw.attendanceadmin.bean.Record;
import com.wnw.attendanceadmin.bean.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by wnw on 2018/4/7.
 */

public class RecordActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView nothingView;

    private List<Record> recordList = new ArrayList<>();
    private RecordAdapter recordAdapter;

    private ProgressDialog progressDialog;
    private Attendance attendance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Intent intent = getIntent();
        attendance = (Attendance)intent.getSerializableExtra("attendance");
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 菜单选中监听
        int id = item.getItemId();
        if (id == R.id.action_update_attendance) {
            if (recordList.size() > 0){
                Toast.makeText(RecordActivity.this, "已经有人打开，不能更新", Toast.LENGTH_SHORT).show();
                return true;
            }
            startActivityForResult(new Intent(this, UpdateAttendanceActivity.class).putExtra("attendance",attendance), 1001);
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

        recordAdapter = new RecordAdapter(this, recordList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recordAdapter);
        findRecords();
    }

    private void findRecords(){
        progressDialog.show();
        BmobQuery<Record> query = new BmobQuery<Record>();
        query.addWhereEqualTo("attendanceId", attendance.getObjectId());
        query.findObjects(new FindListener<Record>() {
            @Override
            public void done(List<Record> list, BmobException e) {
                progressDialog.dismiss();
                if (e == null && list.size() > 0){
                    Log.e("attendanceTAG", "list size =" + list.size());
                    recordList = list;
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
        recordAdapter.setRecordList(recordList);
        recordAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK){
            setResult(RESULT_OK);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
