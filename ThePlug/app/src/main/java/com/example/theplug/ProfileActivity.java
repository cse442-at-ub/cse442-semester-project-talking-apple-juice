package com.example.theplug;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.*;

import android.os.Bundle;

public class ProfileActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button confirmPassChange = findViewById(R.id.confirmButton);
        confirmPassChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                    clearPassInputs();
            }
        });

        ImageView ProfilePic = findViewById(R.id.yourProfilePic);
    }

    private void clearPassInputs() {
        EditText editText = findViewById(R.id.currentPass);
        editText.getText().clear();
        editText = findViewById(R.id.newPass);
        editText.getText().clear();
        editText = findViewById(R.id.newPassVerify);
        editText.getText().clear();
    }

}
