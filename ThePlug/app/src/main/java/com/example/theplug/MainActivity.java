package com.example.theplug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    public static Bitmap prodImg;
    public static String prodName;
    public static String prodDesc;


    private EditText passInput;
    private EditText emailInput;

    private FirebaseAuth auth;

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

        auth = FirebaseAuth.getInstance();

    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }


    /** Called when the user taps the Send button */
    public void loginAttempt(View view) {
        //check account
        passInput = (EditText) findViewById(R.id.Password);
        emailInput = (EditText) findViewById(R.id.Username);
        loginUserAuth(emailInput.getText().toString(), passInput.getText().toString());
    }

    public void loginUserAuth(String em, String pass)
    {
        auth.signInWithEmailAndPassword(em, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("MainActivity", "signInWithEmail:success");
                    Intent intent = new Intent(MainActivity.this, HomeScreen.class);
                    startActivity(intent);
                }else{
                    Log.w("MainActivity", "signInWithEmail:fail", task.getException());
                    Toast incorrectAuth = Toast.makeText(getApplicationContext(), "Invalid Account", Toast.LENGTH_SHORT);
                    incorrectAuth.show();
                }
            }
        });
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
