package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

public class Fragment1 extends Fragment implements View.OnClickListener {

    public ContactsAdapter adapter;
    public ListView list;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<Contact> arrayList;
    private FloatingActionButton fab;


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
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.attachToListView(list);
        fab.setOnClickListener(this);

        AddItemFromFireBase();

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                Intent intent = new Intent(getContext(),InsertActivity.class);
                startActivityForResult(intent,1001);//액티비티 띄우기
                break;
        }
        adapter.notifyDataSetChanged();
    }

}