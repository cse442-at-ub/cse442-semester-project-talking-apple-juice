package com.example.theplug;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private EditText passInput;
    private EditText emailInput;
    public SharedPreferences accInfo;
    public SharedPreferences.Editor ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
        {
            setTheme(R.style.lightTheme);
        }else{
            setTheme(R.style.darkTheme);
        }
        setContentView(R.layout.activity_main);
        accInfo = getSharedPreferences("UserInfo", MODE_PRIVATE);
        ed = getSharedPreferences("UserInfo", MODE_PRIVATE).edit();

        String pw = accInfo.getString("PASSWORD", null);
        if(pw == null)
        {
            ed.putString("PASSWORD", "password").apply();
        }

        String em = accInfo.getString("EMAIL", null);
        if(em == null)
        {
            ed.putString("EMAIL", "email").apply();
        }
    }

    /** Called when the user taps the Send button */
    public void loginAttempt(View view) {
        //check account
        passInput = (EditText) findViewById(R.id.Password);
        emailInput = (EditText) findViewById(R.id.Username);
        if((passInput.getText().toString().equals(accInfo.getString("PASSWORD", null))) && (emailInput.getText().toString().equals(accInfo.getString("EMAIL", null))))
        {
            Intent intent = new Intent(this, HomeScreen.class);
            startActivity(intent);
        }else{
            Toast incorrectPass = Toast.makeText(getApplicationContext(), "Invalid Account", Toast.LENGTH_SHORT);
            incorrectPass.show();
        }

//        EditText editText = (EditText) findViewById(R.id.Username);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
    }

    public void goToForgotUser(View view){
        Intent intent = new Intent(this, ForgotUsernameActivity.class);
        startActivity(intent);
    }

    public void goToForgotPass(View view){
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void goToSignup(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}
