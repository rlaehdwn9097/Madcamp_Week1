package com.example.myapplication;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListItemAdapter extends BaseAdapter {

    ArrayList<ListItem> items = new ArrayList<ListItem>();
    Context context;


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        context = parent.getContext();
        ListItem listItem = items.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        TextView adapter_name = (TextView) convertView.findViewById(R.id.listView_name);
        TextView adapter_phone = (TextView) convertView.findViewById(R.id.listView_phone);
        TextView adapter_email = (TextView) convertView.findViewById(R.id.listView_email);

        adapter_name.setText(listItem.getName());
        adapter_phone.setText(listItem.getPhone());
        adapter_email.setText(listItem.getEmail());

        return convertView;

    }

    public void addItem(String name, String phone, String email){
        Log.d("addItem IN >> ", name + "가지고 들어옴");
        ListItem item = new ListItem();
        item.setName(name);
        item.setPhone(phone);
        item.setEmail(email);
        items.add(item);
    }

}