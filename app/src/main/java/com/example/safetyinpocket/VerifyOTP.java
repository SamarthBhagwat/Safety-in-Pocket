package com.example.safetyinpocket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerifyOTP extends AppCompatActivity {
    EditText inputotp1, inputotp2, inputotp3, inputotp4, inputotp5, inputotp6;
    Button verifyotp;
    TextView textView;
    String getotpbackend;


    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_OTP = "otp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        inputotp1 = findViewById(R.id.inputotp1);
        inputotp2 = findViewById(R.id.inputotp2);
        inputotp3 = findViewById(R.id.inputotp3);
        inputotp4 = findViewById(R.id.inputotp4);
        inputotp5 = findViewById(R.id.inputotp5);
        inputotp6 = findViewById(R.id.inputotp6);
        verifyotp = findViewById(R.id.buttonsubmitotp);
        textView = findViewById(R.id.mobilenumbertext);
        textView.setText(String.format(
                "+91-%s", getIntent().getStringExtra("mobile")
        ));
        getotpbackend = getIntent().getStringExtra("backendotp");
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String otp = sharedPreferences.getString(KEY_OTP, null);

        if (otp != null) {
            Intent intent = new Intent(VerifyOTP.this, DashboardActivity.class);
            startActivity(intent);
        }

        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otp1 = inputotp1.getText().toString();
                String otp2 = inputotp2.getText().toString();
                String otp3 = inputotp3.getText().toString();
                String otp4 = inputotp4.getText().toString();
                String otp5 = inputotp5.getText().toString();
                String otp6 = inputotp6.getText().toString();
                String inputnumber = otp1 + otp2 + otp3 + otp4 + otp5 + otp6;
                if (!inputnumber.isEmpty()) {

                    String entercodeotp = inputnumber;

                    if (getotpbackend != null) {

                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                getotpbackend, entercodeotp);
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString(KEY_OTP, inputnumber);
                                            editor.apply();
                                            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(VerifyOTP.this, "Enter the correct OTP", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        Toast.makeText(VerifyOTP.this, "otp verify", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(VerifyOTP.this, "Please check Internet", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VerifyOTP.this, "please enter all numbers", Toast.LENGTH_SHORT).show();

                }
            }

        });

        numberotpmove();

    }

    private void numberotpmove() {

        inputotp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty())
                    inputotp2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputotp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty())
                    inputotp3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputotp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty())
                    inputotp4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputotp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty())
                    inputotp5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputotp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty())
                    inputotp6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
