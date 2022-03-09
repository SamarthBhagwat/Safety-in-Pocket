package com.example.safetyinpocket;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOError;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    Button btn , emergency , activateService, logout;
    ListView listView;
    public static ArrayList<String> contactList;
    FusedLocationProviderClient fusedLocationProviderClient;
    private ArrayAdapter arrayAdapter;
    private  static  DashboardActivity instance;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences1;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    boolean isPressed=false;
    AddContactList addContactList;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_OTP = "otp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        instance=this;
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.app_name) + "</font>"));


        btn = findViewById(R.id.btn);
        listView = findViewById(R.id.myList);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        emergency = findViewById(R.id.emergency);
        activateService = findViewById(R.id.activateService);


        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String number = sharedPreferences.getString(KEY_NUMBER, null);
        String otp = sharedPreferences.getString(KEY_OTP , null);
        ArrayList<String> temp = getIntent().getStringArrayListExtra("keyList");
        contactList = new ArrayList<>();

        if (temp != null) {
            contactList.addAll(temp);
        }
        loadData();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DashboardActivity.this, AddContactList.class);
                intent.putExtra("keyList", contactList);
                startActivity(intent);

            }
        });

        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(DashboardActivity.this , Activated.class);
                        startActivity(intent);

                    }
                    else{
                        Toast.makeText(getApplicationContext() , "Please Activate First to continue" , Toast.LENGTH_LONG).show();

                    }

                }

            }
        });

        activateService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                startForegroundService(new Intent(getApplicationContext(),MyService.class));
                                Toast.makeText(getApplicationContext(), "Shake Service Started", Toast.LENGTH_LONG).show();
                            }
                            else{
                                startService(new Intent(getApplicationContext(),MyService.class));
                            }


                    } else {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS}, 1);

                    }
                }

                }

        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int which_item=i;
                new AlertDialog.Builder(DashboardActivity.this)
                        .setTitle("Are you sure ?")
                        .setMessage("Do you want to delete this number")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                contactList.remove(which_item);
                                arrayAdapter.notifyDataSetChanged();

                                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                                editor1.clear();
                                editor1.commit();

                                save();
                                arrayAdapter.notifyDataSetChanged();



                            }
                        })
                        .setNegativeButton("No",null)
                        .show();

                return true;
            }
        });
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.clear();
//                editor.commit();
//
//                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
//                editor1.clear();
//                editor1.commit();
//                Toast.makeText(DashboardActivity.this, "Logout Successfull", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });

        arrayAdapter = new ArrayAdapter<>(DashboardActivity.this, R.layout.list_view_item, R.id.textView2, contactList);
        listView.setAdapter(arrayAdapter);
    }
    public void createNewInformationDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View informationPopupView = getLayoutInflater().inflate(R.layout.popup , null);
        dialogBuilder.setView(informationPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater() ;
        inflater.inflate(R.menu.sip_menu,menu);
        

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()){
            case R.id.Logout:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                editor1.clear();
                editor1.commit();
                Toast.makeText(DashboardActivity.this, "Logout Successfull", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.use:
                createNewInformationDialog();
                break;

            case R.id.exitapp:
                stopService((new Intent(getApplicationContext(),MyService.class)));
                finishAffinity();
                System.exit(0);


            default:
                return super.onOptionsItemSelected(item);

        }

        return false;
    }


    public static DashboardActivity getInstance(){
        return  instance;
    }

    public  void save() {
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

    }

    private void loadData() {

         sharedPreferences1 = getSharedPreferences("shared preferences",
                MODE_PRIVATE);

        // creating a variable for gson.
        Gson gson = new Gson();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences1.getString("contacts", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<String>>() {}.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        contactList = gson.fromJson(json, type);

        // checking below if the array list is empty or not
        if (contactList == null) {
            // if the array list is empty
            // creating a new array list.
            contactList = new ArrayList<>();
        }}

    public void sendSMS() {
        if(contactList.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please add contacts then try again",Toast.LENGTH_SHORT).show();
        }
        else {


            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                Location location = task.getResult();
                if (location != null) {

                    try {

                        Geocoder geocoder = new Geocoder(DashboardActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        try {
                            SmsManager smsManager = SmsManager.getDefault();
                            String msg = "Alert! It's an Emergency...! I am in trouble, I need help, Please immediately reach me out at the below address.";
                            String address = "Address:  " + addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getLocality() + "," + addresses.get(0).getCountryName();
                            for (String number : contactList) {

                                smsManager.sendTextMessage(number, null, msg, null, null);
                                smsManager.sendTextMessage(number, null, address, null, null);
                                smsManager.sendTextMessage(number, null, "https://www.google.com/maps/search/?api=1&query=" + addresses.get(0).getLatitude() + "%2C" + addresses.get(0).getLongitude(), null, null);
                            }

                            Intent phone_intent = new Intent(Intent.ACTION_CALL);
                            phone_intent.setData(Uri.parse("tel:" + contactList.get(0)));
                            startActivity(phone_intent);



                            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
                        } catch (IOError e) {
                            Toast.makeText(getApplicationContext(), "Message not Sent", Toast.LENGTH_LONG).show();

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

            });
        }





    }
    @Override
    public  void onBackPressed(){
        if (isPressed){

            finishAffinity();

        } else{
            Toast.makeText(getApplicationContext(),"press back again to exit",Toast.LENGTH_SHORT).show();
             isPressed=true;
        }
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                isPressed=false;
            }
        };
        new Handler().postDelayed(runnable,2000);

    }

}