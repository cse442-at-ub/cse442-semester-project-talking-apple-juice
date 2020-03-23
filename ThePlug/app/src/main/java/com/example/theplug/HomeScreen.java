package com.example.theplug;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONArray;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeScreen extends AppCompatActivity {

    public ImageView bid1, bid2;
    public ImageView sale1, sale2;
    public SearchView searchBar;
    public ListView listProd;

    JSONArray productList = new JSONArray();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
        {
            setTheme(R.style.lightTheme);
        }else{
            setTheme(R.style.darkTheme);
        }
        //REQUEST 4 PRODUCTS FROM DATABASE, RECENT 2 OF SALEBID 1 AND RECENT 2 OF SALEBID 0
        setContentView(R.layout.activity_home_screen);

        bid1 = findViewById(R.id.bid1);
        bid2 = findViewById(R.id.bid2);
        sale1 = findViewById(R.id.sale1);
        sale2 = findViewById(R.id.sale2);
        searchBar = findViewById(R.id.searchView);
        listProd = findViewById(R.id.list_view);

        getProduct();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void getResult(){
        class getProduct extends AsyncTask<String, Void, JSONArray>{

            @Override
            protected JSONArray doInBackground(String... strings) {
                String name = searchBar.getQuery().toString();
                String searchScript = "https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/searchProduct.php?Name=" + name ;
                URL url = null;

                try {
                    url = new URL(searchScript);
                    productList =
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }



    }

    private void getProduct(){
        class GetImage extends AsyncTask<String, Void, Bitmap>
        {
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Bitmap bm){
                super.onPostExecute(bm);
                sale1.setImageBitmap(bm);
            }

            @Override
            protected Bitmap doInBackground(String... strings) {
                String id = strings[0];
                String script = "https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/retrieveImage.php?id=" + id;
                URL url = null;
                Bitmap img = null;
                try {
                    url = new URL(script);
                    img = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return img;
            }
        }
        GetImage get = new GetImage();
        get.execute("2"); //eventually, execute this 4 times with different id's
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
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

    public void goToItemDesc(View view){
        Intent intent = new Intent(this, ViewProductActivity.class);
        startActivity(intent);
    }

}
