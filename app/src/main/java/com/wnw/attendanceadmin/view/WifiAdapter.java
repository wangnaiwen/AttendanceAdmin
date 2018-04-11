package com.wnw.attendanceadmin.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wnw.attendanceadmin.R;
import com.wnw.attendanceadmin.bean.Wifi;

import java.util.List;

/**
 * Created by wnw on 2018/4/7.
 */

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.VHolder>{
    private Context context;
    private List<Wifi> wifiList;
    private boolean isAttendance;
    public WifiAdapter(Context context, List<Wifi> wifis) {
        this.context = context;
        this.wifiList = wifis;
    }

    public void isAttendance(boolean isAttendance){
        this.isAttendance = isAttendance;
    }

    public void setWifiList(List<Wifi> wifis) {
        this.wifiList = wifis;
    }

    @Override
    public WifiAdapter.VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wifi, parent, false);
        return new WifiAdapter.VHolder(view);
    }

    @Override
    public void onBindViewHolder(WifiAdapter.VHolder holder, final int position) {
        final Wifi wifi = wifiList.get(position);
        holder.timeTv.setText("考勤Wifi：" + wifi.getMac());
        holder.addressTv.setText("考勤地点：" + wifi.getAddress());

        holder.itemRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAttendance) {
                    Intent intent = new Intent();
                    intent.putExtra("wifi", wifi);
                    ((Activity) context).setResult(Activity.RESULT_OK, intent);
                    ((Activity) context).finish();
                    ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    ((Activity) context).startActivityForResult(new Intent(context, UpdateWifiActivity.class).putExtra(
                            "wifi", wifiList.get(position)), 1001);
                    ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return wifiList.size();
    }

    class VHolder extends RecyclerView.ViewHolder{
        RelativeLayout itemRl;
        TextView timeTv;
        TextView addressTv;

        public VHolder(View itemView) {
            super(itemView);
            timeTv = (TextView)itemView.findViewById(R.id.mac);
            addressTv = (TextView)itemView.findViewById(R.id.address);
            itemRl = (RelativeLayout)itemView.findViewById(R.id.rl_item_wifi);
        }
    }
}
