package com.example.theplug;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.*;

import android.os.Bundle;

public class ProfileActivity extends AppCompatActivity {
    private EditText oldPass;
    private EditText newPass1;
    private EditText newPass2;
    private SharedPreferences accInfo;
    private SharedPreferences.Editor ed;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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

        ImageView ProfilePic = findViewById(R.id.yourProfilePic);
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

}
