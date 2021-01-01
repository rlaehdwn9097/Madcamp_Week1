package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertActivity extends AppCompatActivity {

    public double min = 9000;
    public double max = 9999;
    public int contactid;

    public Button btn1;
    public EditText name;
    public EditText email;
    public EditText phone;


    private DatabaseReference mDatabase;
    public ContactsAdapter adapter;

    public InsertActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btn1 = (Button) findViewById(R.id.btn1);
        name = (EditText) findViewById(R.id.insert_name);
        email = (EditText) findViewById(R.id.insert_email);
        phone = (EditText) findViewById(R.id.insert_phone);



        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewUser(name.getText().toString(), email.getText().toString(), phone.getText().toString(),"https://firebasestorage.googleapis.com/v0/b/andproject-dfe1b.appspot.com/o/cat0.jpg?alt=media&token=4b3bdcc5-892f-40b3-a4f4-c16f81e5cbd4");
                finish();
            }
        });

    }

    private void writeNewUser(String name,String phone, String email, String image) {
        //Contact contact = new Contact(name, phone, email, image);


        contactid = (int) ((Math.random() * (max - min)) + min);

        mDatabase.child("User").child("User_" + String.valueOf(contactid)).child("name").setValue(name);
        mDatabase.child("User").child("User_" + String.valueOf(contactid)).child("email").setValue(email);
        mDatabase.child("User").child("User_" + String.valueOf(contactid)).child("phone").setValue(phone);
        mDatabase.child("User").child("User_" + String.valueOf(contactid)).child("image").setValue(image);

        Toast myToast = Toast.makeText(this.getApplicationContext(), "입력되었습니다.", Toast.LENGTH_SHORT);
        myToast.show();

    }


}