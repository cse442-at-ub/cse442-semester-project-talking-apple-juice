package com.example.theplug;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.io.ByteArrayOutputStream;

public class ProfilePicChangeActivity extends AppCompatActivity {

    private static final int GalVal = 1;
    private Uri profImage;
    ImageButton profView;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
        {
            setTheme(R.style.lightTheme);
        }else{
            setTheme(R.style.darkTheme);
        }
        setContentView(R.layout.activity_profile_pic_change);
        profView = findViewById(R.id.profileImageButton);
        submit = findViewById(R.id.changePicButton5);

        profView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upLoader(v);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePic();
            }
        });
    }

    // Allows user to choose image from gallery regardless of device after clicking image
    public void upLoader(View v) {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalVal);
    }

    public void updatePic(){
        BitmapDrawable bmd = (BitmapDrawable) profView.getDrawable();
        Bitmap itemImg = bmd.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        itemImg.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

        String user = MainActivity.storedUsername;

        BackgroundActivity ba = new BackgroundActivity(this);
        ba.execute("profile", user, encImage);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GalVal && resultCode == RESULT_OK){
            profImage = data.getData();
            profView.setImageURI(profImage);
        }
    }
}
