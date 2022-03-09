package com.example.safetyinpocket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    EditText enternumber;
    Button getotpbutton;
    SharedPreferences sharedPreferences;
    ProgressBar progressBar;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NUMBER = "number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        enternumber = findViewById(R.id.editTextPhone);
        getotpbutton = findViewById(R.id.buttongetotp);
        progressBar = findViewById(R.id.progressbar_sending_otp);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String number = sharedPreferences.getString(KEY_NUMBER, null);

        if (number != null) {
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
        }

        getotpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!enternumber.getText().toString().trim().isEmpty()) {
                    if ((enternumber.getText().toString().trim().length() == 10)) {

                        progressBar.setVisibility(View.VISIBLE);
                        getotpbutton.setVisibility(View.INVISIBLE);

                        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + enternumber.getText().toString(), 60, TimeUnit.SECONDS, MainActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility(View.GONE);
                                getotpbutton.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                getotpbutton.setVisibility(View.VISIBLE);
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String backendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                progressBar.setVisibility(View.GONE);
                                getotpbutton.setVisibility(View.VISIBLE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(KEY_NUMBER, enternumber.getText().toString());
                                editor.apply();

                                Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                                intent.putExtra("mobile", enternumber.getText().toString());
                                intent.putExtra("backendotp", backendotp);

                                startActivity(intent);
                            }
                        });
                        //                      Intent intent=new Intent(getApplicationContext(),VerifyOTP.class);
                        //                      intent.putExtra("mobile",enternumber.getText().toString());
                        //                      startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter correct number", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        finishAffinity();
        System.exit(0);

    }
}
