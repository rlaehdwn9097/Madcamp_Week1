package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    public ImageView imageView;

    //int[] imageArray = null;
    ArrayList<Uri> imageArray;
    //ArrayList<Imagecontent> imagecontentArray;

    @Override
    public int getCount() {
        //return imageArray.length;
        return imageArray.size();
    }

    public ImageAdapter(Context context, ArrayList<Uri> imageIDs) {
        this.context = context;
        this.imageArray = imageIDs;
    }
    /*
    public ImageAdapter(Context context, ArrayList<Imagecontent> imagecontentIDS) {
        this.context = context;
        this.imagecontentArray = imagecontentIDS;
    }*/

    @Override
    public Object getItem(int position) {
        //\\return imageArray[position];
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
            // GridView 뷰를 구성할 ImageView 뷰들을 정의합니다.
            // 뷰에 지정할 이미지는 앞에서 정의한 비트맵 객체입니다.

            imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true);
            //Glide로 대체
            Log.d("imageAdapter","____________imageAdapter________ in___________");
            imageView.setImageURI(imageArray.get(position));

            //---------------------------------------------------------------
            // 사진 항목들의 클릭을 처리하는 ImageClickListener 객체를 정의합니다.
            // 그리고 그것을 ImageView의 클릭 리스너로 설정합니다.
/*
            ImageClickListener imageViewClickListener
                    = new ImageClickListener(context, imageArray, position);
            imageView.setOnClickListener(imageViewClickListener);

            ImageLongClickListener imageViewLongClickListener
                    = new ImageLongClickListener(context,position);
            imageView.setOnLongClickListener(imageViewLongClickListener);
*/

        }
        return imageView;
    }
}

