package com.example.theplug;

import androidx.appcompat.app.AppCompatActivity;

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
    public SharedPreferences accInfo;
    public SharedPreferences.Editor ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accInfo = getSharedPreferences("UserInfo", MODE_PRIVATE);
        ed = getSharedPreferences("UserInfo", MODE_PRIVATE).edit();
        String pw = accInfo.getString("PASSWORD", null);
        if(pw == null)
        {
            ed.putString("PASSWORD", "password").apply();
        }
    }

    /** Called when the user taps the Send button */
    public void loginAttempt(View view) {
        //check password
        passInput = (EditText) findViewById(R.id.Password);
        if(passInput.getText().toString().equals(accInfo.getString("PASSWORD", null)))
        {
            Intent intent = new Intent(this, HomeScreen.class);
            startActivity(intent);
        }else{
            Toast incorrectPass = Toast.makeText(getApplicationContext(), "Wrong password!", Toast.LENGTH_SHORT);
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
    public void goToItemDesc(View view){
        Intent intent = new Intent(this, ViewProductActivity.class);
        startActivity(intent);
    }

}
