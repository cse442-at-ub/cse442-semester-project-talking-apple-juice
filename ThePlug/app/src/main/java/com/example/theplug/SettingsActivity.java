package com.example.theplug;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch themeSwitch = findViewById(R.id.themeFlip);
        themeSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                /* will switch between a dark and light theme */
            }

        });
    }

}