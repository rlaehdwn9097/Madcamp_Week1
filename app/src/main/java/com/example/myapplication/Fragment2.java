package com.example.myapplication;


import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Fragment2 extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    int i = 1;
    private static final int REQ_IMAGE_CAPTURE = 1;
    private Boolean isPermission = true;
    private File tempFile;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;
    private FloatingActionButton fab;
    private FloatingActionButton itemFab;
    private FloatingActionButton deleteFab;
    private boolean isOpen = false;


    //--------------------사진 추가-----------------------------
    public ImageView imageView2;
    public FirebaseStorage storage = FirebaseStorage.getInstance();
    public StorageReference storageReference = storage.getReference();
    //다운로드할 파일을 가르키는 참조 만들기
    public StorageReference pathReference = storageReference.child("uploads/020210104042033.png");

    //--------------------------------------------------------
    public GridView gridView;
    public ImageAdapter adapter;
    public Context context;
    public ArrayList<Uri> imageID = new ArrayList<Uri>();
    public DBHelper dbHelper;

    public Fragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        imageID.clear();
        context = getContext();
        checkSelfPermission();
        dbHelper = new DBHelper(context,"test.db",null,1);
        View rootView = inflater.inflate(R.layout.fragment_2, container, false);

        //-------------------사진추가__위-------------------------------
        fab = (FloatingActionButton) rootView.findViewById(R.id.mainFab);
        itemFab = (FloatingActionButton) rootView.findViewById(R.id.insertfab);
        deleteFab = (FloatingActionButton) rootView.findViewById(R.id.deletefab);
        gridView = (GridView) rootView.findViewById(R.id.gridViewImages);
        fab.setOnClickListener(this);
        itemFab.setOnClickListener(this);
        deleteFab.setOnClickListener(this);

        //*******************카메라__아래***********************************

        initFragment2();

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                DBHelper dbHelper;
                dbHelper = new DBHelper(context,"test2.db",null,1);
                deleteDialog(dbHelper, position);
                return true;
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Intent intent = new Intent(context, PaperActivity.class);

                intent.putExtra("image ID", imageID);
                intent.putExtra("position", position);
                intent.putExtra("length",imageID.size());
                Log.d("viewpager","viewpager in");
                context.startActivity(intent);

            }
        });

        return rootView;
    }


    //----------------사진추가_위----------------------------------

    public void initFragment2(){
        imageID.clear();
        Cursor cursor = dbHelper.select();
        cursor.moveToFirst();
        for(int i = 0 ; i < cursor.getCount(); i++){
            String str_image = cursor.getString(cursor.getColumnIndex("image"));
            Log.d("uri_string===>>>>",str_image);

            File file = new File(str_image);
            imageID.add(Uri.parse(str_image));
            cursor.moveToNext();
        }
        adapter = new ImageAdapter(context, imageID);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    public void checkSelfPermission() {
        String temp = "";
        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }
        //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }
        if (TextUtils.isEmpty(temp) == false) {
            // 권한 요청
            ActivityCompat.requestPermissions(getActivity(), temp.trim().split(" "), 1);
        } else {
            //Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mainFab:
                if(!isOpen){
                    //ObjectAnimator.ofFloat(itemFab, "translationY", -400f).start();
                    ObjectAnimator.ofFloat(deleteFab, "translationY", -200f).start();
                    isOpen = true;
                }
                else{
                    //ObjectAnimator.ofFloat(itemFab, "translationY", 0f).start();
                    ObjectAnimator.ofFloat(deleteFab, "translationY", 0f).start();
                    isOpen = false;
                }
                break;
            case R.id.deletefab:
                //Toast.makeText(getContext(), "categoryFab", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(MediaStore.Images.Media.CONTENT_TYPE);
                //i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


                try {

                    i.putExtra("data", true);

                    startActivityForResult(
                            Intent.createChooser(i, "Select Picture"), 0);
                } catch (
                        ActivityNotFoundException ex) {
                    ex.printStackTrace();
                }
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("image", "image");
        //super method removed
        if (data != null) {

            Uri selectedImage = data.getData();

            SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddhhmmss");
            String filename;
            filename= sdf.format(new Date())+ ".png";//현재 시간으로 파일명 지정 20191023142634
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference imgRef= firebaseStorage.getReference("uploads/"+filename);
            imgRef.putFile(selectedImage);


            //다중 선택하기_____
            dbHelper.insert(selectedImage.toString(),String.valueOf(dbHelper.select().getCount()));
            i++;
            Log.d("gallery==>>>>>>>>",selectedImage.toString());

        } else {
            Toast.makeText(getActivity(), "Try Again!!", Toast.LENGTH_SHORT).show();
        }
        initFragment2();
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void deleteDialog(DBHelper dbHelper, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("삭제 확인"); builder.setMessage("삭제하시겠습니까?");
        builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //예 눌렀을때의 이벤트 처리
                dbHelper.delete(String.valueOf(position));
                Toast.makeText(getContext(),"삭제되었습니다.",Toast.LENGTH_LONG).show();
                initFragment2();
            }
        });
        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //아니오 눌렀을때의 이벤트 처리
            }
        });
        builder.show();

    }


    @Override
    public boolean onLongClick(View v) {
        return true;
    }
}