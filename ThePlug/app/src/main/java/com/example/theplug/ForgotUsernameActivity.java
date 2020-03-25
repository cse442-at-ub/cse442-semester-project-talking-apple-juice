package com.example.theplug;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ForgotUsernameActivity extends AppCompatActivity {

    private EditText getUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
        {
            setTheme(R.style.lightTheme);
        }else{
            setTheme(R.style.darkTheme);
        }
        setContentView(R.layout.activity_forgot_username);

        getUsername = findViewById(R.id.unEmailEditText4);
        getUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveUn(v);
            }
        });
    }

    public void retrieveUn(View v){ //REMEMBER TO IMPLEMENT WITH DATABASE
        getUsername = findViewById(R.id.unEmailEditText4);
        if(getUsername.getText().toString().equals("aosaliu@buffalo.edu") || getUsername.getText().toString().equals("ooladepo@buffalo.edu") || getUsername.getText().toString().equals("jtstone@buffalo.edu")){
            getUsername.getText().clear();
            Toast success = Toast.makeText(getApplicationContext(), "Account Found! Your username has been sent to your email", Toast.LENGTH_SHORT);
            success.show();
        }else {
            getUsername.getText().clear();
            Toast success = Toast.makeText(getApplicationContext(), "Account Not Found! Please enter a valid email", Toast.LENGTH_SHORT);
            success.show();
        }

    }
}
