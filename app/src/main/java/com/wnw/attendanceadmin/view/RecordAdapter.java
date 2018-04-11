package com.wnw.attendanceadmin.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.wnw.attendanceadmin.R;
import com.wnw.attendanceadmin.bean.Record;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by wnw on 2018/4/1.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordHolder>{
    private Context context;
    private List<Record> recordList;

    public RecordAdapter(Context context, List<Record> records) {
        this.context = context;
        this.recordList = records;
    }

    public void setRecordList(List<Record> records) {
        this.recordList = records;
    }

    @Override
    public RecordAdapter.RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_record, parent, false);
        return new RecordAdapter.RecordHolder(view);
    }

    @Override
    public void onBindViewHolder(RecordAdapter.RecordHolder holder, int position) {
        Record record = recordList.get(position);
        Date date = new Date(record.getRecordTime());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        holder.sidTv.setText("学号：" + record.getsId());
        holder.statusTv.setText("状态：" + record.getResult());
        holder.timeTv.setText("时间：" + sdf.format(date));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    class RecordHolder extends RecyclerView.ViewHolder{
        TextView sidTv;
        TextView timeTv;
        TextView statusTv;

        public RecordHolder(View itemView) {
            super(itemView);
            sidTv = (TextView)itemView.findViewById(R.id.sid);
            statusTv = (TextView)itemView.findViewById(R.id.status);
            timeTv = (TextView)itemView.findViewById(R.id.time);
        }
    }
}
