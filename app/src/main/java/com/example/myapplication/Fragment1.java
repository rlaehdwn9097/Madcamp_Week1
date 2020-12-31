package com.example.myapplication;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment1 extends Fragment {

    public ListItemAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment1 newInstance(String param1, String param2) {
        Fragment1 fragment = new Fragment1();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_1, container, false);
        // Generate sample data

        // Locate the ListView in fragmenttab1.xml
        ListView list = (ListView) rootView.findViewById(R.id.f1_listview);

        // Binds the Adapter to the ListView
        adapter = new ListItemAdapter();
        list.setAdapter(adapter);
        AddItemFromContacts();

        adapter.notifyDataSetChanged();

        return rootView;
    }

    public void AddItemFromContacts(){

        try{
            InputStream is = getContext().getAssets().open("contacts.json");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while(line != null){
                buffer.append(line);
                line = reader.readLine();
            }

            String jsonData = buffer.toString();


            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray contactsArray = jsonObject.getJSONArray("Contacts");
            for(int i=0; i<contactsArray.length(); i++)
            {
                JSONObject contactsObject = contactsArray.getJSONObject(i);
                adapter.addItem(contactsObject.getString("name"),
                        contactsObject.getString("number"),contactsObject.getString("email"));
            }


            /*
            JSONArray jsonArray = new JSONArray(jsonData);

            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("test_name");
                String phone = jsonObject.getString("test_phone");
                String email = jsonObject.getString("test_email");
                Log.d("Name", name);
                Log.d("phone", phone);
                Log.d("email", email);
                adapter.addItem(name, phone, email);
            }
            */
        }
        catch(IOException | JSONException e){
            e.printStackTrace();
        }
    }
}