package com.example.theplug;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, HomeScreen.class);
//        EditText editText = (EditText) findViewById(R.id.Username);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
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
