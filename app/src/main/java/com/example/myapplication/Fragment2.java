package com.example.myapplication;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
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

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.net.URI;
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
    private ArrayList<Uri> imageID = new ArrayList<Uri>();
    private ArrayList<Imagecontent> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    DBHelper dbHelper;
    SQLiteDatabase db = null;

    public Fragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        imageID.clear();
        context = container.getContext();
        checkSelfPermission();
        dbHelper = new DBHelper(context,"test.db",null,1);

// Insert the new row, returning the primary key value of the new row
        //long newRowId = db.insert(FeedEntry.TABLE_NAME, null, values);

        View rootView = inflater.inflate(R.layout.fragment_2, container, false);

        //-------------------사진추가__위-------------------------------
        buttonGallery = (Button) rootView.findViewById(R.id.ButtonGallery);
        //imageView2 = (ImageView) rootView.findViewById(R.id.imageView2);
        buttonCamera = (Button) rootView.findViewById(R.id.ButtonCamera);
        int permission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (permission == PackageManager.PERMISSION_DENIED) {
            // 권한 없어서 요청
        } else { // 권한 있음
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQ_IMAGE_CAPTURE);
        }
        /*
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(context.getApplicationContext(), "다운로드 성공 : "+ uri, Toast.LENGTH_SHORT).show();
                Glide.with(context).asDrawable().apply(fitCenterTransform()).load(uri).into(imageView2);
                String str = uri.toString();
                int resID = getResources().getIdentifier(str , "drawable", context.getPackageName());
                //imageView2.setImageURI(uri);
                //imageView2.setImageResource(resID);
                //imageID.add(uri);
                Log.d("fragment2","fragment open");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context.getApplicationContext(), "다운로드 실패", Toast.LENGTH_SHORT).show();
            }
        });
        */


        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(MediaStore.Images.Media.CONTENT_TYPE);
                //i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                //i.setDataAndType(photoURI,"image/*");
                // ******** code for crop image
                /*
                i.putExtra("crop", "true");
                i.putExtra("aspectX", 100);
                i.putExtra("aspectY", 100);
                i.putExtra("outputX", 256);
                i.putExtra("outputY", 356);*/

                try {

                    i.putExtra("data", true);
                    startActivityForResult(
                            Intent.createChooser(i, "Select Picture"), 0);
                } catch (
                        ActivityNotFoundException ex) {
                    ex.printStackTrace();
                }

            }
        });
        //Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        //-------------------사진추가__아래-----------------------------


        gridView = (GridView) rootView.findViewById(R.id.gridViewImages);
        //adapter = new ImageAdapter(context, imageIDs);
        Cursor cursor = dbHelper.select();
        cursor.moveToFirst();
        for(int i = 0 ; i < cursor.getCount(); i++){
            String str_image = cursor.getString(cursor.getColumnIndex("image"));
            Log.d("uri_string===>>>>",str_image);

            File file = new File(str_image);
            //imageView2.setImageBitmap(StringToBitmap(str_image));
            /*
            Glide.with(context)
                    .load(file)
                    .into(imageView2);*/
            //imageView2.setImageURI(Uri.parse(str_image));

            imageID.add(Uri.parse(str_image));
            cursor.moveToNext();
        }
        adapter = new ImageAdapter(context, imageID);
        gridView.setAdapter(adapter);


        adapter.notifyDataSetChanged();
        //AddItemFromFireBase();
        //selectFirebase(0);
        // Inflate the layout for this fragment
        return rootView;
    }


    //----------------사진추가_위----------------------------------
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { //권한을 허용 했을 경우
        if (requestCode == 1) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // 동의
                    Log.d("MainActivity", "권한 허용 : " + permissions[i]);
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
            ActivityCompat.requestPermissions(getActivity(), temp.trim().split(" "), 1);
        } else {
            //Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("image", "image");
        //super method removed
        if (data != null) {
            Uri selectedImage = data.getData();
            //ClipData clipData = data.getClipData();
            /*
            if(clipData != null){
                Log.d("test =====>>>>>>>>", String.valueOf(data.getData()));
            }
            else{
                Log.d("--------------------->", "null!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }


            //imageView2.setImageURI(selectedImage);
            //imageView2.setImageBitmap(BitmapFactory.decodeFile(picturePath));


            //______다중 선택하기
            //----https://namhandong.tistory.com/43------참고사이트//

            SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddhhmmss");
            String filename;
            for (int i = 0; i < clipData.getItemCount(); i++) {

                //______firebase에 갤러리 이미지 업로드하기
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                //파일 명이 중복되지 않도록 날짜를 이용

                filename= sdf.format(new Date())+ ".png";//현재 시간으로 파일명 지정 20191023142634
                //원래 확장자는 파일의 실제 확장자를 얻어와서 사용해야함. 그러려면 이미지의 절대 주소를 구해야함.

                StorageReference imgRef= firebaseStorage.getReference("uploads/"+i+filename);
                //uploads라는 폴더가 없으면 자동 생성
                //StorageReference imgRef = firebaseStorage.getReference("image");
                //-------------imgRef.putFile(clipData.getItemAt(i).getUri());//firebase에 upload하기
                String sturi = clipData.getItemAt(i).getUri().toString();

                mDatabase.child("image").child("image_" + String.valueOf(sdf.format(new Date()))+i).child("image").setValue(sturi);
                //firebase에 갤러리 이미지 업로드하기______

                dbHelper.insert(String.valueOf(clipData.getItemAt(i).getUri()),Integer.toString(i));
                imageID.add(clipData.getItemAt(i).getUri());

            }*/



            //다중 선택하기_____
            dbHelper.insert(selectedImage.toString(),"key");
            //imageView2.setImageURI(Uri.parse(selectedImage.toString()));
            Log.d("gallery==>>>>>>>>",selectedImage.toString());
            //imageView2.setImageBitmap(StringToBitmap(selectedImage.toString()));
            imageID.add(selectedImage);
            //cursor.close();
        } else {
            Toast.makeText(getActivity(), "Try Again!!", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //Uri returnUri;
    //returnUri = data.getData();

    //----------------사진추가_아래--------------------------------


    //firebase database 추가+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

/*
    public void AddItemFromFireBase(){

        arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("image"); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    Imagecontent Imagecontent = snapshot.getValue(Imagecontent.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    arrayList.add(Imagecontent); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                    //Log.d("확인", contact.getName());
                }
                //adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                Log.d("arraylist.size()", String.valueOf(arrayList.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        //gridView = getView().findViewById(R.id.gridViewImages);
        //adapter = new ImageAdapter(context, imageIDs);
        adapter = new ImageAdapter(context, arrayList);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();




    }
*/
    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
}

}