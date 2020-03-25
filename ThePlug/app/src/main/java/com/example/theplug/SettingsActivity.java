package com.example.theplug;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    public Button editButton, deleteButton;
    public EditText prodName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
        {
            setTheme(R.style.lightTheme);
        }else{
            setTheme(R.style.darkTheme);
        }
        setContentView(R.layout.activity_settings);

        Switch themeSwitch = findViewById(R.id.themeFlip);

        themeSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

                finish();
                startActivity(new Intent(SettingsActivity.this, SettingsActivity.this.getClass()));
            }

        });

        init();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productJson test =  new productJson();
                test.getData();
//                 prodDeleter();
            }
        });
    }

    public void init(){

        prodName = (EditText) findViewById(R.id.nameProd);
        editButton = (Button) findViewById(R.id.editProd);
        deleteButton = (Button) findViewById(R.id.deleteProd);

    }

    public void prodDeleter(){

        String name  = prodName.getText().toString();

        NewProductActivity npa = new NewProductActivity(this);
        npa.execute("delete", name);
        finish();
    }






}