package com.example.myapplication;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//import com.melnykov.fab.FloatingActionButton;
//import com.github.clans.fab.FloatingActionButton;

public class Fragment1 extends Fragment implements View.OnClickListener {

    public ContactsAdapter adapter;
    public ListView list;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<Contact> arrayList;
    private FloatingActionButton fab;
    private FloatingActionButton itemFab;
    private FloatingActionButton categoryFab;
    private boolean isOpen = false;


    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_1, container, false);
        // Generate sample data

        // Locate the ListView in fragmenttab1.xml
        list = (ListView) rootView.findViewById(R.id.f1_listview);
        fab = (FloatingActionButton) rootView.findViewById(R.id.mainFab);
        itemFab = (FloatingActionButton) rootView.findViewById(R.id.insertfab);
        categoryFab = (FloatingActionButton) rootView.findViewById(R.id.deletefab);
        //fab.attachToListView(list);
        fab.setOnClickListener(this);
        itemFab.setOnClickListener(this);
        categoryFab.setOnClickListener(this);

        AddItemFromFireBase();


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override   // position 으로 몇번째 것이 선택됐는지 값을 넘겨준다
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast myToast = Toast.makeText(getContext(),arrayList.get(position).getName(), Toast.LENGTH_SHORT);
                //myToast.show();
                show(position);

            }
        });


        return rootView;
    }

    public void AddItemFromFireBase(){

        arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("User"); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    Contact contact = snapshot.getValue(Contact.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    arrayList.add(contact); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                    //Log.d("확인", contact.getName());
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        adapter = new ContactsAdapter(getContext(), arrayList);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mainFab:

                if(!isOpen){
                    ObjectAnimator.ofFloat(itemFab, "translationY", -400f).start();
                    ObjectAnimator.ofFloat(categoryFab, "translationY", -200f).start();
                    isOpen = true;
                }
                else{
                    ObjectAnimator.ofFloat(itemFab, "translationY", 0f).start();
                    ObjectAnimator.ofFloat(categoryFab, "translationY", 0f).start();
                    isOpen = false;
                }

                break;
            case R.id.insertfab:
                Intent intent = new Intent(getContext(), InsertContactActivity.class);
                startActivityForResult(intent,1001);//액티비티 띄우기
                break;
            case R.id.deletefab:
                //Toast.makeText(getContext(), "categoryFab", Toast.LENGTH_SHORT).show();

                break;
        }

    }

    public void show(int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("상세정보");
        builder.setMessage("\n이름 : "+arrayList.get(position).getName() + "\n\n" + "전화번호 : " + arrayList.get(position).getPhone() + "\n");
        builder.setPositiveButton("전화걸기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getContext(),arrayList.get(position).getPhone().replaceAll("-", ""),Toast.LENGTH_LONG).show();
                        Intent tt = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + arrayList.get(position).getPhone().replaceAll("-", "")));
                        startActivity(tt);
                        //전화거는 거


                    }
                });
        builder.setNegativeButton("돌아가기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNeutralButton("연락처 삭제",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                        //연락처 삭제

                        removeContact(arrayList, position);


                    }
                });

        builder.show();
    }

    public void removeContact(ArrayList<Contact> arrayList, int position){
        String id = arrayList.get(position).getId();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User").child("User_" + id);
        mDatabase.removeValue();
    }


}