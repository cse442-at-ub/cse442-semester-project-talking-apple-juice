package com.example.theplug;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.*;

import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ProfileActivity extends AppCompatActivity {
    private EditText oldPass;
    private EditText newPass1;
    private EditText newPass2;
    private EditText curEmail;
    private EditText newEmail;
    private EditText verEmailF;
    private SharedPreferences accInfo;
    private SharedPreferences.Editor ed;

    public Bitmap temp;
    public ImageView profilePic;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
        {
            setTheme(R.style.lightTheme);
        }else{
            setTheme(R.style.darkTheme);
        }
        setContentView(R.layout.activity_profile);

        accInfo = getSharedPreferences("UserInfo", MODE_PRIVATE);
        ed = getSharedPreferences("UserInfo", MODE_PRIVATE).edit();

        Button confirmPassChange = findViewById(R.id.confirmPassButton);
        confirmPassChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                    updatePass();
            }
        });

        Button confirmEmailChange = findViewById(R.id.confirmEmailButton);
        confirmEmailChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                  updateEmail();
            }
        });

        profilePic = findViewById(R.id.yourProfilePic);

        GetProfilePicData gp = new GetProfilePicData();
        gp.execute("pfp", MainActivity.storedUsername);
    }

    private void updateEmail(){
        curEmail = (EditText)findViewById(R.id.currentEmail);
        newEmail = (EditText)findViewById(R.id.changeEmail);
        verEmailF = (EditText)findViewById(R.id.confirmEmail);
        String oldE = curEmail.getText().toString();
        String newE = newEmail.getText().toString();
        String verE = verEmailF.getText().toString();
        if(oldE.equals("") || newE.equals("") || verE.equals(""))
        {
            Toast err = Toast.makeText(getApplicationContext(), "Please fill out all boxes.", Toast.LENGTH_SHORT);
            err.show();
        }
        else if(!(oldE.equals(accInfo.getString("EMAIL", null))))
        {
            Toast err = Toast.makeText(getApplicationContext(), "Incorrect current email.", Toast.LENGTH_SHORT);
            err.show();
        }
        else if(!(newE.equals(verE)))
        {
            Toast err = Toast.makeText(getApplicationContext(), "New Emails don't match.", Toast.LENGTH_SHORT);
            err.show();
        }else {
            ed.putString("EMAIL", newE).commit();
            EditText editText = findViewById(R.id.currentEmail);
            editText.getText().clear();
            editText = findViewById(R.id.changeEmail);
            editText.getText().clear();
            editText = findViewById(R.id.confirmEmail);
            editText.getText().clear();
            Toast success = Toast.makeText(getApplicationContext(), "Email changed!", Toast.LENGTH_SHORT);
            success.show();
        }
    }

    private void updatePass() {
        oldPass = (EditText) findViewById(R.id.currentPass);
        newPass1 = (EditText) findViewById(R.id.newPass);
        newPass2 = (EditText) findViewById(R.id.newPassVerify);
        String oldP = oldPass.getText().toString();
        String newP = newPass1.getText().toString();
        String verP = newPass2.getText().toString();
        if(oldP.equals("") || newP.equals("") || verP.equals(""))
        {
            Toast err = Toast.makeText(getApplicationContext(), "Please fill out all boxes.", Toast.LENGTH_SHORT);
            err.show();
        }
        else if(!(oldP.equals(accInfo.getString("PASSWORD", null))))
        {
            Toast err = Toast.makeText(getApplicationContext(), "Incorrect current password.", Toast.LENGTH_SHORT);
            err.show();
        }
        else if(!(newP.equals(verP)))
        {
            Toast err = Toast.makeText(getApplicationContext(), "New passwords don't match.", Toast.LENGTH_SHORT);
            err.show();
        }else{
            ed.putString("PASSWORD", newP).commit();
            EditText editText = findViewById(R.id.currentPass);
            editText.getText().clear();
            editText = findViewById(R.id.newPass);
            editText.getText().clear();
            editText = findViewById(R.id.newPassVerify);
            editText.getText().clear();
            Toast success = Toast.makeText(getApplicationContext(), "Password changed!", Toast.LENGTH_SHORT);
            success.show();
        }
    }
    public void goToPicChange(View view){
        Intent intent = new Intent(this, ProfilePicChangeActivity.class);
        startActivity(intent);
    }


    class GetProfilePicData extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            if(s.equals("Image Retrieved"))
            {
               profilePic.setImageBitmap(temp);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            if(type.equals("pfp")){
                String user = strings[1];
                String imgScript = "https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/retrievePFP.php?un=" +user;
                Bitmap img = null;
                try {
                    URL url = new URL(imgScript);
                    img = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                temp = img;
                return "Image Retrieved";
            }else{
                return "error";
            }
        }
    }
}
