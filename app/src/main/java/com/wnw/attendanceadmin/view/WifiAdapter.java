package com.wnw.attendanceadmin.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public WifiAdapter(Context context, List<Wifi> wifis) {
        this.context = context;
        this.wifiList = wifis;
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
    public void onBindViewHolder(WifiAdapter.VHolder holder, int position) {
        Wifi wifi = wifiList.get(position);
        holder.timeTv.setText("考勤Wifi：" + wifi.getMac());
        holder.addressTv.setText("考勤地点：" + wifi.getAddress());
    }

    @Override
    public int getItemCount() {
        return wifiList.size();
    }

    class VHolder extends RecyclerView.ViewHolder{
        TextView timeTv;
        TextView addressTv;

        public VHolder(View itemView) {
            super(itemView);
            timeTv = (TextView)itemView.findViewById(R.id.mac);
            addressTv = (TextView)itemView.findViewById(R.id.address);
        }
    }
}
