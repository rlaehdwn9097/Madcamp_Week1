package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ContactsAdapter extends BaseAdapter {

    private ArrayList<Contact> contactList;
    private Context context;


    public ContactsAdapter(Context context, ArrayList<Contact> arrayList) {
        this.contactList = arrayList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        context = parent.getContext();
        Contact contact = contactList.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        TextView adapter_name = (TextView) convertView.findViewById(R.id.listView_name);
        TextView adapter_phone = (TextView) convertView.findViewById(R.id.listView_phone);
        TextView adapter_email = (TextView) convertView.findViewById(R.id.listView_email);
        ImageView adapter_image = (ImageView) convertView.findViewById(R.id.listView_image);

        adapter_name.setText(contact.getName());
        adapter_phone.setText(contact.getPhone());
        adapter_email.setText(contact.getEmail());
        Glide.with(context)
                .load(contactList.get(position).getImage())
                .apply(new RequestOptions().override(60, 68))
                .into(adapter_image);

        return convertView;

    }
}
