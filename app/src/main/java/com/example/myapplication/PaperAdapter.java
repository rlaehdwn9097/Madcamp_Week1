package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class PaperAdapter extends PagerAdapter {

    //private int[] images;
    private ArrayList<Uri> images;
    private LayoutInflater inflater;
    private Context context;
    private int preposition;

    public PaperAdapter(Context context, /*int[] imageIDs*/ ArrayList<Uri> imageIDs, int preposition){
        this.context = context;
        this.images = imageIDs;
        this.preposition = preposition;
    }

    @Override
    public int getCount() {
        // return images.length;
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout)object);
    }


    public Object instantiateItem(ViewGroup container, int position){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.slider, container,false);

        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        TextView textView = (TextView) v.findViewById(R.id.textView);
        //Log.d("preposition", String.valueOf(preposition));
        //Log.d("position", String.valueOf(position));
        //imageView.setImageResource(images[position]);
        imageView.setImageURI(images.get(position));
        //imageView.setImageBitmap(images.get(position));
        textView.setText((position+1) + "image");

        v.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //this will log the page number that was click
                //Log.i("TAG", "This page was clicked: " + position);

                //getSupportFragmentManager().beginTransaction().replace(R.id.contents,paperFragment).commit();

            }
        });

        container.addView(v);
        return v;
    }

    public void destroyItem(ViewGroup container, int position, Object object){
        container.invalidate();
    }
}
