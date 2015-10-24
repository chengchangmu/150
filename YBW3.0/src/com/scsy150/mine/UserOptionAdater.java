package com.scsy150.mine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scsy150.R;
 
public class UserOptionAdater extends BaseAdapter {
    private LayoutInflater inflater;
    String[] name;
    int[] iconarray;
 
    public UserOptionAdater(Context context, String[] name, int[] iconarray) {
        this.inflater = LayoutInflater.from(context);
        this.name = name;
        this.iconarray = iconarray;
    }
 
    @Override
    public int getCount() {
        return name.length;
    }
 
    @Override
    public Object getItem(int position) {
        return position;
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null) {
            holder=new ViewHolder();
            convertView=this.inflater.inflate(R.layout.item_user_layout, null);
            holder.iv=(ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv=(TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        }
        else {
           holder=(ViewHolder) convertView.getTag();
        }
        holder.iv.setImageResource(iconarray[position]);
        holder.tv.setText(name[position]);
        return convertView;
    }
    private class ViewHolder{
        ImageView iv;
        TextView tv;
    }
 
}