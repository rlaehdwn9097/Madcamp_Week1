package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class PaperActivity extends AppCompatActivity {

    PaperAdapter adapter;
    ViewPager viewPager;
    Button btn;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);
        btn = (Button) findViewById(R.id.Button1);

        Intent receivedIntent = getIntent();

        int[] imageID = receivedIntent.getIntArrayExtra("image ID");
        int position = receivedIntent.getExtras().getInt("position");


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




