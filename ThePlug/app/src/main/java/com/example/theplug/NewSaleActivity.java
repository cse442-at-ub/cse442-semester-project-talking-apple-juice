package com.example.theplug;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NewSaleActivity extends AppCompatActivity {


    private ProgressDialog pDialog;
    EditText editName, editPrice, editType, editDesc;
    Button putforSale,  putforBid;
    ImageView prodView;
    private Uri prodImage;
    private static final int GalleryPick = 1;

   // private static String addProductURL = "https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/createproduct.php";
  //  private static final String TAG_SUCCESS = "success";



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


    }
//    private byte[] imageViewToByte(ImageView image){
//        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] bytesArray = stream.toByteArray();
//        return bytesArray;
//    }

    public void init(){
        editName = (EditText) findViewById(R.id.editText4);
        editPrice = (EditText) findViewById(R.id.editText2);
        editType = (EditText) findViewById(R.id.editText6);
        editDesc = (EditText) findViewById(R.id.editText);
        prodView  =  (ImageView)  findViewById(R.id.imageView7);
        putforSale =(Button) findViewById(R.id.button3);
        putforBid = (Button) findViewById(R.id.button4);
    }

    // Allows user to choose image from gallery regardless of device after clicking image
    public void upLoader(View v) {
        Intent gallaryIntent = new Intent();
        gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
        gallaryIntent.setType("image/*");
        startActivityForResult(gallaryIntent, GalleryPick);
    }

    public void imageConvertor(){
    }
    // Allows user to chose product to upload to database
    public void prodUpLoader(){

        String name = editName.getText().toString();
        String price = editPrice.getText().toString();
        String type = editType.getText().toString();
        String desc = editDesc.getText().toString();
        String image = prodView.getContext().toString();

        NewProductActivity npa = new NewProductActivity(this);
        npa.execute("upload", name, type, price, desc, image);
        finish();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
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
