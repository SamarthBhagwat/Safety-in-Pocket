package com.example.safetyinpocket;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Activated extends AppCompatActivity {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activated);
        btn=findViewById(R.id.button4);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.app_name) + "</font>"));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    DashboardActivity.getInstance().sendSMS();


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}