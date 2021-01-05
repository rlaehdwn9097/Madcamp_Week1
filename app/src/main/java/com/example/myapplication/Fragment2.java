package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Fragment2 extends Fragment {

    private static final int REQ_IMAGE_CAPTURE = 1;
    //--------------------사진 추가-----------------------------
    Button buttonCamera, buttonGallery;
    ImageView imageView2;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    //다운로드할 파일을 가르키는 참조 만들기
    StorageReference pathReference = storageReference.child("uploads/");

    //--------------------------------------------------------

    GridView gridView;
    ImageAdapter adapter;
    private Context context;
    Button btn;

    //private ArrayList<Uri> imageID = new ArrayList<qi>();
    public ArrayList<ImageURL> imageID;

    public Fragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = container.getContext();
        View rootView = inflater.inflate(R.layout.fragment_2, container, false);

        //-------------------사진추가__위-------------------------------
        buttonGallery = (Button) rootView.findViewById(R.id.ButtonGallery);
        imageView2 = (ImageView) rootView.findViewById(R.id.imageView2);
        buttonCamera = (Button) rootView.findViewById(R.id.ButtonCamera);
        int permission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if(permission == PackageManager.PERMISSION_DENIED){
            // 권한 없어서 요청
        } else{ // 권한 있음
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),REQ_IMAGE_CAPTURE);
        }

        return rootView;
    }




}
