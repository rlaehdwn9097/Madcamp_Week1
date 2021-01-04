package com.example.myapplication;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment2 extends Fragment {

    private static final int REQ_IMAGE_CAPTURE = 1;
    //--------------------사진 추가-----------------------------
    Button buttonCamera, buttonGallery;
    ImageView imageView2;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    //다운로드할 파일을 가르키는 참조 만들기
    StorageReference pathReference = storageReference.child("uploads/020210104042033.png");

    //--------------------------------------------------------

    GridView gridView;
    ImageAdapter adapter;
    private Context context;
    Button btn;


    /*
    private int[] imageIDs = new int[] {
            R.drawable.cat1,
            /*
            R.drawable.cat2,
            R.drawable.cat3,
            R.drawable.cat4,
            R.drawable.cat5,
            R.drawable.cat6,
            R.drawable.cat7,
            R.drawable.cat8,
            R.drawable.cat9,
            R.drawable.cat10,
            R.drawable.cat11,
            R.drawable.cat12,
            R.drawable.cat13,
            R.drawable.cat14,
            R.drawable.cat15,
            R.drawable.cat16,
            R.drawable.cat17,
            R.drawable.cat18,
            R.drawable.cat19,
            R.drawable.cat20,



    };
*/


    private ArrayList<Uri> imageID = new ArrayList<Uri>();



    public Fragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = container.getContext();
        checkSelfPermission();

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


        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(context.getApplicationContext(), "다운로드 성공 : "+ uri, Toast.LENGTH_SHORT).show();
                //Glide.with(context).asDrawable().apply(fitCenterTransform()).load(uri).into(imageID);

                //imageView2.setImageURI(uri);
                imageID.add(uri);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context.getApplicationContext(), "다운로드 실패", Toast.LENGTH_SHORT).show();
            }
        });

        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(MediaStore.Images.Media.CONTENT_TYPE);
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);



                try {

                    i.putExtra("data", true);
                    startActivityForResult(
                            Intent.createChooser(i, "Select Picture"), 0);
                }catch (
                        ActivityNotFoundException ex){
                    ex.printStackTrace();
                }

            }
        });

        //-------------------사진추가__아래-----------------------------



        gridView = (GridView) rootView.findViewById(R.id.gridViewImages);
        //adapter = new ImageAdapter(context, imageIDs);
        adapter = new ImageAdapter(context, imageID);
        gridView.setAdapter(adapter);


        adapter.notifyDataSetChanged();


        // Inflate the layout for this fragment
        return rootView;
    }



    //----------------사진추가_위----------------------------------
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    { //권한을 허용 했을 경우
        if(requestCode == 1){
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // 동의
                    Log.d("MainActivity","권한 허용 : " + permissions[i]);
                }
            }
        }

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
            ActivityCompat.requestPermissions(getActivity(), temp.trim().split(" "),1); }
        else {
            //Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }

    }





    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("image","image");
        //super method removed
        if (data != null) {
            Uri selectedImage = data.getData();
            ClipData clipData = data.getClipData();
            /*
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);*/
            imageView2.setImageURI(selectedImage);
            //imageView2.setImageBitmap(BitmapFactory.decodeFile(picturePath));


            //______다중 선택하기
                        //----https://namhandong.tistory.com/43------참고사이트//
            SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddhhmmss");
            String filename;
            for(int i = 0 ; i < clipData.getItemCount(); i++){
                //______firebase에 갤러리 이미지 업로드하기
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                //파일 명이 중복되지 않도록 날짜를 이용

                filename= sdf.format(new Date())+ ".png";//현재 시간으로 파일명 지정 20191023142634
                //원래 확장자는 파일의 실제 확장자를 얻어와서 사용해야함. 그러려면 이미지의 절대 주소를 구해야함.

                StorageReference imgRef= firebaseStorage.getReference("uploads/"+i+filename);
                //uploads라는 폴더가 없으면 자동 생성
                //StorageReference imgRef = firebaseStorage.getReference("image");
                imgRef.putFile(clipData.getItemAt(i).getUri());//firebase에 upload하기

                //firebase에 갤러리 이미지 업로드하기______

                //imageID.add(clipData.getItemAt(i).getUri());
            }

            //다중 선택하기_____
            //imageID.add(selectedImage);
            //cursor.close();
        } else {
            Toast.makeText(getActivity(), "Try Again!!", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //Uri returnUri;
    //returnUri = data.getData();

    //----------------사진추가_아래--------------------------------


/*
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
*/
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment2.
     */
    /*
    // TODO: Rename and change types and number of parameters
    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

}
