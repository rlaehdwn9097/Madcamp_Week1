package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    public ImageView imageView;

    //int[] imageArray = null;
    ArrayList<Uri> imageArray;

    @Override
    public int getCount() {
        //return imageArray.length;
        return imageArray.size();
    }

    public ImageAdapter(Context context, /*int[] imageIDs*/ArrayList<Uri> imageIDs) {
        this.context = context;
        this.imageArray = imageIDs;
    }

    @Override
    public Object getItem(int position) {
        //return imageArray[position];
        return imageArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("image position", String.valueOf(position));
        context = parent.getContext();
        if (null != convertView)
            imageView = (ImageView) convertView;
        else {
            //---------------------------------------------------------------
            // GridView 뷰를 구성할 ImageView 뷰의 비트맵을 정의합니다.
            // 그리고 그것의 크기를 320*240으로 줄입니다.
            // 크기를 줄이는 이유는 메모리 부족 문제를 막을 수 있기 때문입니다.
            /*
            Bitmap bmp
                    = BitmapFactory.decodeResource(context.getResources(), imageArray[position]);*/
            //Bitmap bmp = imageArray.get(position);
            //bmp = Bitmap.createScaledBitmap(bmp, 100, 100, false);


            //---------------------------------------------------------------
            // GridView 뷰를 구성할 ImageView 뷰들을 정의합니다.
            // 뷰에 지정할 이미지는 앞에서 정의한 비트맵 객체입니다.

            imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true);
            //Glide로 대체
            //imageView.setImageURI(imageArray.get(position));
/*
            Glide.with(context)
                    .load(imageArray.get(position))
                    .apply(new RequestOptions().override(150, 150))
                    .into(imageView);

 */


            //---------------------------------------------------------------
            // 사진 항목들의 클릭을 처리하는 ImageClickListener 객체를 정의합니다.
            // 그리고 그것을 ImageView의 클릭 리스너로 설정합니다.

            ImageClickListener imageViewClickListener
                    = new ImageClickListener(context, imageArray, position);
            imageView.setOnClickListener(imageViewClickListener);

        }
        return imageView;
    }
}

