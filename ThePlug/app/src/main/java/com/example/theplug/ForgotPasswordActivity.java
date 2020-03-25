package com.example.theplug;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText resetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
        {
            setTheme(R.style.lightTheme);
        }else{
            setTheme(R.style.darkTheme);
        }
        setContentView(R.layout.activity_forgot_password);


    }


    public void passReset(View v){ //REMEMBER TO IMPLEMENT WITH DATABASE FOR SPRINT 3

        resetPass  =  findViewById(R.id.linkedEmailEditText4);
        if(resetPass.getText().toString().equals("aosaliu@buffalo.edu") || resetPass.getText().toString().equals("ooladepo@buffalo.edu") || resetPass.getText().toString().equals("jtstone@buffalo.edu")){
            resetPass.getText().clear();
            Toast success = Toast.makeText(getApplicationContext(), "Account Found! Link to reset password has been sent to email", Toast.LENGTH_SHORT);
            success.show();
        }else {
            resetPass.getText().clear();
            Toast success = Toast.makeText(getApplicationContext(), "Account Not Found! Please enter a valid email", Toast.LENGTH_SHORT);
            success.show();
        }
    }
}
