package com.example.theplug;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ViewProductActivity extends AppCompatActivity {

    public ImageView imgV;
    public TextView imgName;
    public TextView imgDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
        {
            setTheme(R.style.lightTheme);
        }else{
            setTheme(R.style.darkTheme);
        }
        setContentView(R.layout.activity_view_product);

        imgV = findViewById(R.id.productImg);
        imgName = findViewById(R.id.itemNameTextView8);
        imgDesc = findViewById(R.id.itemDescTextView7);
        imgV.setImageBitmap(MainActivity.prodImg);
        imgName.setText(MainActivity.prodName);
        imgDesc.setText(MainActivity.prodDesc);
    }
}
