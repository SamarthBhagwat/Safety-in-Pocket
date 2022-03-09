package com.example.safetyinpocket;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;

public class AddContactList extends AppCompatActivity {

    Button button ;
    EditText editText ;
    EditText edName;
    String phoneNumber ;
    ArrayList<String> contactList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact_list);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.app_name) + "</font>"));

        button = findViewById(R.id.button);
        editText = findViewById(R.id.editTextPhone);
        edName=findViewById(R.id.edName);



        contactList.addAll(getIntent().getStringArrayListExtra("keyList"));
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(i,101);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = editText.getText().toString();
                editText.setText("");
                if(!phoneNumber.trim().isEmpty()) {
                    if ((phoneNumber.trim().length() == 10)) {
                        contactList.add(phoneNumber);
//                        editText.setText("");
                        Toast.makeText(getApplicationContext(), "Contact Added", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddContactList.this, DashboardActivity.class);
                        intent.putExtra("keyList" , contactList);
                        saveData();
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(AddContactList.this,"Please enter a valid mobile number",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(AddContactList.this,"Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    public  void saveData() {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences sharedPreferences1 = getSharedPreferences("shared preferences", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json;
        json = gson.toJson(contactList);

        // below line is to save data in shared
        // prefs in the form of string.
        editor1.putString("contacts", json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor1.apply();

        // after saving data we are displaying a toast message.
        Toast.makeText(this, "Contact added ", Toast.LENGTH_SHORT).show();
    }
    @Override
    public  void onBackPressed(){
        startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode == Activity.RESULT_OK){
            Uri uri =  data.getData();
            String cols[] = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
            Cursor rs = getContentResolver().query(uri,cols,null,null,null);
            if(rs.moveToFirst()){
                String num = rs.getString(0);
                num.trim();
                if(num.charAt(0)=='+')
                    num = num.substring(3);
                else if (num.charAt(0)=='0')
                    num=num.substring(1);
                num = num.replaceAll("\\s","");
                editText.setText(num);
                edName.setText(rs.getString(1));
            }
        }
    }
}