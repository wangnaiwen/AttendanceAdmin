package com.wnw.attendanceadmin.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wnw.attendanceadmin.R;
import com.wnw.attendanceadmin.bean.Leave;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by wnw on 2018/4/7.
 */

public class LeaveActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private TextView nothingView;
    private ProgressDialog progressDialog;

    private List<Leave> greeList = new ArrayList<>();
    private List<Leave> refuseList = new ArrayList<>();
    private List<Leave> neaveList = new ArrayList<>();

    private LeaveAdapter leaveAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        initView();
    }

    private void initView(){
        recyclerView = (RecyclerView)findViewById(R.id.rv);
        nothingView = (TextView)findViewById(R.id.tv_nothing);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中，请稍后...");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        leaveAdapter = new LeaveAdapter(this, neaveList);
        recyclerView.setAdapter(leaveAdapter);

        tabLayout = (TabLayout)findViewById(R.id.tab);
        tabLayout.addTab(tabLayout.newTab().setText("待审批"));
        tabLayout.addTab(tabLayout.newTab().setText("已审批"));
        tabLayout.addTab(tabLayout.newTab().setText("已拒绝"));
        findLeave("待审批");
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                findLeave(tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals("待审批")){
                    updateRv(neaveList);
                }else if (tab.getText().toString().equals("已审批")){
                    updateRv(greeList);
                }else {
                    updateRv(refuseList);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK){
            findLeave("待审批");
        }
    }

    private void findLeave(final String result){
        progressDialog.show();
        BmobQuery<Leave> query = new BmobQuery<>();
        query.addWhereEqualTo("status", result);
        query.findObjects(new FindListener<Leave>() {
            @Override
            public void done(List<Leave> list, BmobException e) {
                progressDialog.dismiss();
                if (e == null && list.size() > 0){
                    if (result.equals("待审批")){
                        neaveList = list;
                        updateRv(neaveList);
                    }else if (result.equals("已审批")){
                        greeList = list;
                        updateRv(greeList);
                    }else {
                        //已经拒绝
                        refuseList = list;
                        updateRv(refuseList);
                    }
                }else {
                    e.printStackTrace();
                    nothingView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void updateRv(List<Leave> leaves){
        nothingView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        leaveAdapter.setList(leaves);
        leaveAdapter.notifyDataSetChanged();
    }
}
