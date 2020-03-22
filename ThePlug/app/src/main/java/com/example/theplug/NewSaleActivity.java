package com.example.theplug;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class NewSaleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
        {
            setTheme(R.style.lightTheme);
        }else{
            setTheme(R.style.darkTheme);
        }
        setContentView(R.layout.activity_new_sale);

//        ImageButton findImg = findViewById(R.id.findImgButton);
//        findImg.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(Intent.createChooser(intent, "Choose an image"), 1);
//            }
//        });
//
//        Button newSale = findViewById(R.id.button3);
//        newSale.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ImageButton findImg = findViewById(R.id.findImgButton);
//                EditText name = findViewById(R.id.itemName);
//                EditText desc = findViewById(R.id.editText);
//
//                MainActivity.prodImg = ((BitmapDrawable)findImg.getDrawable()).getBitmap();
//                MainActivity.prodName = name.getText().toString();
//                MainActivity.prodDesc = desc.getText().toString();
//
//                //set activity_view_product things to the values in imagebutton, name, and description
//            }
//        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, @Nullable Intent data)
    {
        if ((resCode == RESULT_OK) && reqCode == 1){
            ImageButton findImg = findViewById(R.id.findImgButton);

            try{
                InputStream stream = getContentResolver().openInputStream(data.getData());
                Bitmap userImg = BitmapFactory.decodeStream(stream);
                findImg.setImageBitmap(userImg);
            }catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem page){
        if(page.getItemId() == R.id.accountButton)
        {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        if(page.getItemId() == R.id.messageButton)
        {
            Intent intent = new Intent(this, MessagesActivity.class);
            startActivity(intent);
            return true;
        }
        if(page.getItemId() == R.id.transaction_historyButton)
        {
            Intent intent = new Intent(this, TransactionsActivity.class);
            startActivity(intent);
            return true;
        }
        if(page.getItemId() == R.id.settingsButton)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if(page.getItemId() == R.id.newSaleButton)
        {
            Intent intent = new Intent(this, NewSaleActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }


}
