package com.example.theplug;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.Random;

public class NewSaleActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    EditText editName, editPrice, editType, editDesc;
    Button putforSale,  putforBid;
    ImageView prodView;
    private Uri prodImage;
    private static final int GalleryPick = 1;

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

        init();

        putforSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prodUpLoader();
            }
        });

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

    // Allows user to choose image from gallery regardless of device after clicking image
    public void upLoader(View v) {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    public void init(){
        editName = (EditText) findViewById(R.id.editText4);
        editPrice = (EditText) findViewById(R.id.editText2);
        editType = (EditText) findViewById(R.id.editText6);
        editDesc = (EditText) findViewById(R.id.editText);
        prodView  =  (ImageView)  findViewById(R.id.imageView7);
        putforSale =(Button) findViewById(R.id.button3);
        putforBid = (Button) findViewById(R.id.button4);
    }

    public void prodUpLoader(){
        String name = editName.getText().toString();
        String price = editPrice.getText().toString();
        String type = editType.getText().toString();
        String desc = editDesc.getText().toString();
        int randomVal = new Random().nextInt(10000);
        String id = Integer.toString(randomVal);
        String selltype = "0";

        BitmapDrawable bmd = (BitmapDrawable) prodView.getDrawable();
        Bitmap itemImg = bmd.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        itemImg.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        NewProductActivity npa = new NewProductActivity(this);
        npa.execute("upload", name, type, price, desc, id, selltype, encImage);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
//        if ((resCode == RESULT_OK) && reqCode == 1){
//            ImageButton findImg = findViewById(R.id.findImgButton);
//
//            try{
//                InputStream stream = getContentResolver().openInputStream(data.getData());
//                Bitmap userImg = BitmapFactory.decodeStream(stream);
//                findImg.setImageBitmap(userImg);
//            }catch (FileNotFoundException e)
//            {
//                e.printStackTrace();
//            }
//        }
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == GalleryPick && resultCode == RESULT_OK){
            prodImage  = data.getData();
            prodView.setImageURI(prodImage);
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
