package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.myapplication.customMarkerFunction.removeMarkerFromFirebase;
import static com.example.myapplication.customMarkerFunction.writeNewMarkerToFirebase;

public class Fragment3 extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText editText;
    private Button button;
    private Geocoder geocoder;
    public ArrayList<LatLng> country;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    public String address;
    private int doubleClickFlag = 0;
    private final long  CLICK_DELAY = 1000;
    private ArrayList<myMarker> arrayList;
    public SupportMapFragment mapFragment;
    public MarkerOptions markerOptions;

    public Fragment3() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_3, container, false);

        editText = (EditText) rootView.findViewById(R.id.decoder_editText);
        button=(Button)rootView.findViewById(R.id.button);
        button.setOnClickListener(this::onClick);
        arrayList = new ArrayList<>();

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); //getMapAsync must be called on the main thread.

        return rootView;
    }



    @Override //구글맵을 띄울준비가 됐으면 자동호출된다.
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //지도타입 - 일반
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        findMarkerFromFireBase();

    }


    public void findMarkerFromFireBase(){
        arrayList.clear();
         // User 객체를 담을 어레이 리스트 (어댑터쪽으로)
        MarkerOptions markerOptions = new MarkerOptions();
        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("Marker"); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                //arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    myMarker mymarker = snapshot.getValue(myMarker.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    arrayList.add(mymarker); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비

                }
                System.out.println(String.valueOf(arrayList.size()));
                Collections.sort(arrayList);

                putMarker(arrayList);
                drawPloyLines(arrayList);
                setCamera(arrayList.get(arrayList.size()-1));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });


    }

    //마커하나찍는 기본 예제
    public void putMarker(ArrayList<myMarker> arrayList) {
        // 파이어 베이스에서 가져온 마커 리스트
        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();

        for(int i = 0; i < arrayList.size(); i++){
            myMarker mymarker = arrayList.get(i);
            markerOptions
                    .position(new LatLng(mymarker.getLat(), mymarker.getLng()))
                    .title(mymarker.getTitle())
                    .snippet(mymarker.getSnippet());

            mMap.addMarker(markerOptions);
            System.out.println(mymarker.getSnippet() +" 추가");
        }

        // .showInfoWindow();

        //정보창 클릭 리스너
        mMap.setOnInfoWindowClickListener(infoWindowClickListener);

        //마커 클릭 리스너
        mMap.setOnMarkerClickListener(markerClickListener);


    }


    public void findLocationFromString(){

        geocoder = new Geocoder(getContext());

        String str=editText.getText().toString();
        List<Address> addressList = null;
        try {
            // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
            addressList = geocoder.getFromLocationName(
                    str, // 주소
                    100); // 최대 검색 결과 개수
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("--------------->", String.valueOf(addressList.get(0)));
        Address location = addressList.get(0);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        address = "";
        address += location.getAddressLine(0);

        // 좌표(위도, 경도) 생성
        LatLng point = new LatLng(latitude, longitude);
        // 마커 생성
        MarkerOptions mOptions2 = new MarkerOptions();
        mOptions2.title("추가하려면 여기를 눌러주세요.");
        mOptions2.snippet(address);
        mOptions2.position(point);
        // 마커 추가
        mMap.addMarker(mOptions2);
        // 해당 좌표로 화면 줌
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,13));
        editText.setText(null);

    }

    public void drawPloyLines(ArrayList<myMarker> arrayList){

        for (int i = 0; i < arrayList.size() - 1; i++) {
            myMarker src = arrayList.get(i);
            myMarker dest = arrayList.get(i+1);

            // mMap is the Map Object
            Polyline line = mMap.addPolyline(
                    new PolylineOptions().add(
                            new LatLng(src.getLat(), src.getLng()),
                            new LatLng(dest.getLat(),dest.getLng())
                    ).width(5).color(Color.BLUE).geodesic(true)
            );
        }

    }

    public void setCamera(myMarker myMarker){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myMarker.getLat(), myMarker.getLng()),15));
        LatLng latLng = new LatLng(myMarker.getLat(), myMarker.getLng());

    }


    //정보창 클릭 리스너
    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            String markerId = marker.getId();
            //Toast.makeText(getContext(), "정보창 클릭 Marker ID : "+markerId, Toast.LENGTH_SHORT).show();
            show(marker);
        }

        void show(Marker marker)
        {
            final EditText edittext = new EditText(getContext());

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("-정보수정-");
            builder.setMessage("타이틀을 입력하세요.");
            builder.setView(edittext);
            builder.setPositiveButton("추가",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            marker.setTitle(edittext.getText().toString());
                            writeNewMarkerToFirebase(marker);
                            findMarkerFromFireBase();
                            Toast.makeText(getContext(),"추가되었습니다." ,Toast.LENGTH_LONG).show();
                        }
                    });
            builder.setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();
        }

    };

    //마커 클릭 리스너
    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            String markerId = marker.getId();
            //선택한 타겟위치
            LatLng location = marker.getPosition();


            doubleClickFlag++;
            Handler handler = new Handler();
            Runnable clickRunnable = new Runnable() {
                @Override
                public void run() {
                    doubleClickFlag = 0;
                    // todo 클릭 이벤트
                    //Toast.makeText(getContext(), "한번 터치", Toast.LENGTH_SHORT).show();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
                }
            };
            if( doubleClickFlag == 1 ) {
                handler.postDelayed( clickRunnable, CLICK_DELAY );
            }else if( doubleClickFlag == 2 ) {
                doubleClickFlag = 0;
                // todo 더블클릭 이벤트 REMOE_MARKER()
                Toast.makeText(getContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                removeMarkerFromFirebase(marker, arrayList);
                arrayList.clear();
                findMarkerFromFireBase();
                marker.remove();
            }

            return false;
        }
    };


    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                findLocationFromString();
                break;
        }

    }


}