package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
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
        country = new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); //getMapAsync must be called on the main thread.

        return rootView;
    }

    @Override //구글맵을 띄울준비가 됐으면 자동호출된다.
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        arrayList = new ArrayList<>();
        //지도타입 - 일반
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //oneMarker();
        //manyMarker();
        //drawPloyLines();

        findMarkerFromFireBase();
        //putMarker();
    }


    public void findMarkerFromFireBase(){

         // User 객체를 담을 어레이 리스트 (어댑터쪽으로)
        MarkerOptions markerOptions = new MarkerOptions();
        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("Marker"); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                System.out.println("Hello");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    myMarker mymarker = snapshot.getValue(myMarker.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    arrayList.add(mymarker); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                    System.out.println("안녕");
                }
                System.out.println(String.valueOf(arrayList.size()));
                putMarker(arrayList);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });



        //drawMultiPloyLines();

    }

    //마커하나찍는 기본 예제
    public void putMarker(ArrayList<myMarker> arrayList) {
        // 파이어 베이스에서 가져온 마커 리스트

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



        // 구글 맵에 표시할 마커에 대한 옵션 설정  (알파는 좌표의 투명도이다.)


        // 마커를 생성한다. showInfoWindow를 쓰면 처음부터 마커에 상세정보가 뜨게한다. (안쓰면 마커눌러야뜸)
        //mMap.addMarker(makerOptions); //.showInfoWindow();

        //정보창 클릭 리스너
        mMap.setOnInfoWindowClickListener(infoWindowClickListener);

        //마커 클릭 리스너
        mMap.setOnMarkerClickListener(markerClickListener);

        //카메라를 여의도 위치로 옮긴다.
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));
        //처음 줌 레벨 설정 (해당좌표=>서울, 줌레벨(16)을 매개변수로 넣으면 된다.) (위에 코드대신 사용가능)(중첩되면 이걸 우선시하는듯)

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(arrayList.get(1).getLat(), arrayList.get(1).getLng()), 16));

    }



        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 16));
/*
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("1", "onInfoWindowClick: ");
                Toast.makeText(getContext(), "눌렀습니다!!", Toast.LENGTH_LONG);
                return false;
            }
        });
*/


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
        //System.out.println("1");
        //System.out.println(((List) addressList).get(0).toString());

        Address location = addressList.get(0);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        address = "";
        address += location.getAddressLine(0);
        System.out.println(address);

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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));

    }




    //마커정보창 클릭리스너는 다작동하나, 마커클릭리스너는 snippet정보가 있으면 중복되어 이벤트처리가 안되는거같다.
    // oneMarker(); 는 동작하지않으나 manyMarker(); 는 snippet정보가 없어 동작이가능하다.

    //정보창 클릭 리스너
    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            String markerId = marker.getId();
            Toast.makeText(getContext(), "정보창 클릭 Marker ID : "+markerId, Toast.LENGTH_SHORT).show();
            show(marker);
        }

        void show(Marker marker)
        {
            final EditText edittext = new EditText(getContext());

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("AlertDialog Title");
            builder.setMessage("AlertDialog Content");
            builder.setView(edittext);
            builder.setPositiveButton("추가",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            marker.setTitle(edittext.getText().toString());
                            writeNewMarkerToFirebase(marker);
                            Toast.makeText(getContext(),"입력되었습니다." ,Toast.LENGTH_LONG).show();
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
                Toast.makeText(getContext(), "마커 클릭 Marker ID : "+markerId+"("+location.latitude+" "+location.longitude+")", Toast.LENGTH_SHORT).show();
                removeMarkerFromFirebase(marker);
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
    /*
    public void oneMarker() {
        // 서울 여의도에 대한 위치 설정
        LatLng seoul = new LatLng(37.52487, 126.92723);
        LatLng kaist = new LatLng(36.221920,  127.214680);


        // 구글 맵에 표시할 마커에 대한 옵션 설정  (알파는 좌표의 투명도이다.)
        MarkerOptions makerOptions = new MarkerOptions();
        makerOptions
                .position(seoul)
                .title("원하는 위치(위도, 경도)에 마커를 표시했습니다.")
                .snippet("여기는 여의도인거같네여!!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .alpha(0.5f);

        // 마커를 생성한다. showInfoWindow를 쓰면 처음부터 마커에 상세정보가 뜨게한다. (안쓰면 마커눌러야뜸)
        mMap.addMarker(makerOptions); //.showInfoWindow();

        //정보창 클릭 리스너
        mMap.setOnInfoWindowClickListener(infoWindowClickListener);

        //마커 클릭 리스너
        mMap.setOnMarkerClickListener(markerClickListener);

        //카메라를 여의도 위치로 옮긴다.
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));
        //처음 줌 레벨 설정 (해당좌표=>서울, 줌레벨(16)을 매개변수로 넣으면 된다.) (위에 코드대신 사용가능)(중첩되면 이걸 우선시하는듯)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 16));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getContext(), "눌렀습니다!!", Toast.LENGTH_LONG);
                return false;
            }
        });
    }
    */
    /*
    public void drawMultiPloyLines() {

        // 서울 여의도에 대한 위치 설정
        LatLng seoul = new LatLng(37.52487, 126.92723);
        LatLng kaist = new LatLng(36.372147,127.3582013);
        LatLng beijing = new LatLng(39.9390731,116.1172792);
        LatLng moscow = new LatLng(55.5815245,36.8251409);
        LatLng praha = new LatLng(50.0598058,14.3255421);
        LatLng berlin = new LatLng(52.5069704,13.2846503);
        LatLng paris = new LatLng(48.8589507,2.2770205);
        LatLng london = new LatLng(51.5287718,-0.2416802);
        LatLng newyork = new LatLng(40.6976637,-74.1197637);
        LatLng losangeles = new LatLng(34.0207305,-118.6919144);
        LatLng tokyo = new LatLng(35.6684415,139.6007845);

        //ArrayList<LatLng> country = new ArrayList<>();
        country.add(kaist);
        country.add(seoul);
        country.add(beijing);
        country.add(moscow);
        country.add(praha);
        country.add(berlin);
        country.add(paris);
        country.add(london);
        country.add(newyork);
        country.add(losangeles);
        country.add(tokyo);




        MarkerOptions makerOptions = new MarkerOptions();

        for(int i=0; i < country.size(); i++){
            makerOptions
                    .position(country.get(i))
                    .title("마커" + i);


            mMap.addMarker(makerOptions);
        }

        //정보창 클릭 리스너
        mMap.setOnInfoWindowClickListener(infoWindowClickListener);

        //마커 클릭 리스너
        mMap.setOnMarkerClickListener(markerClickListener);

        //카메라를 여의도 위치로 옮긴다.
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));
        //처음 줌 레벨 설정 (해당좌표=>서울, 줌레벨(16)을 매개변수로 넣으면 된다.) (위에 코드대신 사용가능)(중첩되면 이걸 우선시하는듯)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kaist, 16));

        for (int i = 0; i < country.size() - 1; i++) {
            LatLng src = country.get(i);
            LatLng dest = country.get(i + 1);

            // mMap is the Map Object
            Polyline line = mMap.addPolyline(
                    new PolylineOptions().add(
                            new LatLng(src.latitude, src.longitude),
                            new LatLng(dest.latitude,dest.longitude)
                    ).width(5).color(Color.BLUE).geodesic(true)
            );
        }

    }
    */
    /*

    ////////////////////////  구글맵 마커 여러개생성 및 띄우기 //////////////////////////
    public void manyMarker() {
        // for loop를 통한 n개의 마커 생성
        for (int idx = 0; idx < 10; idx++) {
            // 1. 마커 옵션 설정 (만드는 과정)
            MarkerOptions makerOptions = new MarkerOptions();
            makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                    .position(new LatLng(37.52487 + idx, 126.92723))
                    .title("마커" + idx); // 타이틀.

            // 2. 마커 생성 (마커를 나타냄)
            mMap.addMarker(makerOptions);
        }
        //정보창 클릭 리스너
        mMap.setOnInfoWindowClickListener(infoWindowClickListener);

        //마커 클릭 리스너
        mMap.setOnMarkerClickListener(markerClickListener);

        // 카메라를 위치로 옮긴다.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.52487, 126.92723)));
    }

    public void drawPloyLines() {
        // 서울 여의도에 대한 위치 설정
        LatLng seoul = new LatLng(37.52487, 126.92723);
        LatLng kaist = new LatLng(36.372147,127.3582013);


        MarkerOptions makerOptions = new MarkerOptions();

        // 구글 맵에 표시할 마커에 대한 옵션 설정  (알파는 좌표의 투명도이다.)

        makerOptions
                .position(seoul)
                .title("원하는 위치(위도, 경도)에 마커를 표시했습니다.")
                .snippet("여기는 여의도인거같네여!!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .alpha(0.5f);

        // 마커를 생성한다. showInfoWindow를 쓰면 처음부터 마커에 상세정보가 뜨게한다. (안쓰면 마커눌러야뜸)
        mMap.addMarker(makerOptions); //.showInfoWindow();

        makerOptions
                .position(kaist)
                .title("원하는 위치(위도, 경도)에 마커를 표시했습니다.")
                .snippet("여기는 카이스트인거같네여!!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .alpha(0.5f);

        // 마커를 생성한다. showInfoWindow를 쓰면 처음부터 마커에 상세정보가 뜨게한다. (안쓰면 마커눌러야뜸)
        mMap.addMarker(makerOptions); //.showInfoWindow();


        //정보창 클릭 리스너
        mMap.setOnInfoWindowClickListener(infoWindowClickListener);

        //마커 클릭 리스너
        mMap.setOnMarkerClickListener(markerClickListener);

        //카메라를 여의도 위치로 옮긴다.
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));
        //처음 줌 레벨 설정 (해당좌표=>서울, 줌레벨(16)을 매개변수로 넣으면 된다.) (위에 코드대신 사용가능)(중첩되면 이걸 우선시하는듯)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 16));

        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(seoul, kaist)
                .width(5)
                .color(Color.RED));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getContext(), "눌렀습니다!!", Toast.LENGTH_LONG);
                return false;
            }
        });

    }

<<<<<<< HEAD
=======
    //마커정보창 클릭리스너는 다작동하나, 마커클릭리스너는 snippet정보가 있으면 중복되어 이벤트처리가 안되는거같다.
    // oneMarker(); 는 동작하지않으나 manyMarker(); 는 snippet정보가 없어 동작이가능하다.

    //정보창 클릭 리스너
    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            String markerId = marker.getId();
            Log.d("2", "onInfoWindowClick: ");
            Toast.makeText(getContext(), "정보창 클릭 Marker ID : "+markerId, Toast.LENGTH_SHORT).show();
        }
    };

    //마커 클릭 리스너
    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            String markerId = marker.getId();
            Log.d("3", "onInfoWindowClick: ");
            //선택한 타겟위치
            LatLng location = marker.getPosition();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,10));
            Toast.makeText(getContext(), "마커 클릭 Marker ID : "+markerId+"("+location.latitude+" "+location.longitude+")", Toast.LENGTH_SHORT).show();
            return false;
        }
    };


    /*
    try {
      final ExifInterface exifInterface = new ExifInterface(imagePath);
      float[] latLong = new float[2];
      if (exifInterface.getLatLong(latLong)) {
        // Do stuff with lat / long...
      }
    } catch (IOException e) {
      logger.info("Couldn't read exif info: " + e.getLocalizedMessage());
    }
>>>>>>> main
     */

}