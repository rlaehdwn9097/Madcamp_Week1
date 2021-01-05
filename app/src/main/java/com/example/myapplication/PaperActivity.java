package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class PaperActivity extends AppCompatActivity {

    PaperAdapter adapter;
    ViewPager viewPager;
    Button btn;
    ArrayList<Uri> imageID;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);

        Intent receivedIntent = getIntent();
        imageID = new ArrayList<Uri>();
        imageID = receivedIntent.getParcelableArrayListExtra("image ID");
        //ArrayList<Bitmap> imageID = receivedIntent.getParcelableArrayListExtra("image ID");
        int position = receivedIntent.getExtras().getInt("position");
        int length = receivedIntent.getExtras().getInt("length");



        viewPager = (ViewPager) findViewById(R.id.view);
        adapter = new PaperAdapter(this, imageID,position);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
/*
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paperFragment = new PaperFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.contents,paperFragment).commit();
            }
        });*/


    }


}




