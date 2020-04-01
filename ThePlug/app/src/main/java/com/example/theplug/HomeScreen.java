package com.example.theplug;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HomeScreen extends AppCompatActivity {

    public ImageView bid1;
    public ImageView bid2;
    public ImageView sale1;
    public ImageView sale2;
    public int imageIndex = 0;
    public int[] recentIDs = {0,0,0,0};
    public Bitmap temp = null;

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

        //for now, the 4 products will just be the most recent posts by ID.
        bid1 = findViewById(R.id.bid1);
        bid2 = findViewById(R.id.bid2);
        sale1 = findViewById(R.id.sale1);
        sale2 = findViewById(R.id.sale2);

        getProduct();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void getProduct(){
        //STEP 1: GET LATEST ID
        //STEP 2: GET LATESTID, LATESTID-1, LATESTID-2, LATESTID-3 AND STORE
        //STEP 3: PUT IMAGES FROM THESE IDS IN RESPECTIVE IMAGEBUTTONS AND ID'S THEIR CLICKHANDLERS

        class GetImageData extends AsyncTask<String, Void, String>
        {
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                if(s.equals("IDs Retrieved"))
                {
                    new GetImageData().execute("images", Integer.toString(recentIDs[0]));
                }else if(s.equals("Image1 Retrieved"))
                {
                    bid1.setImageBitmap(temp);
                    bid1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(HomeScreen.this, ViewProductActivity.class);
                            String assocID = Integer.toString(recentIDs[0]);
                            intent.putExtra("ID", assocID);
                            startActivity(intent);
                        }
                    });
                    new GetImageData().execute("images", Integer.toString(recentIDs[1]));
                }else if(s.equals("Image2 Retrieved"))
                {
                    bid2.setImageBitmap(temp);
                    bid2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(HomeScreen.this, ViewProductActivity.class);
                            String assocID = Integer.toString(recentIDs[1]);
                            intent.putExtra("ID", assocID);
                            startActivity(intent);
                        }
                    });
                    new GetImageData().execute("images", Integer.toString(recentIDs[2]));
                }else if(s.equals("Image3 Retrieved"))
                {
                    sale1.setImageBitmap(temp);
                    sale1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(HomeScreen.this, ViewProductActivity.class);
                            String assocID = Integer.toString(recentIDs[2]);
                            intent.putExtra("ID", assocID);
                            startActivity(intent);
                        }
                    });
                    new GetImageData().execute("images", Integer.toString(recentIDs[3]));
                }else if(s.equals("Image4 Retrieved")){
                    sale2.setImageBitmap(temp);
                    sale2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(HomeScreen.this, ViewProductActivity.class);
                            String assocID = Integer.toString(recentIDs[3]);
                            intent.putExtra("ID", assocID);
                            startActivity(intent);
                        }
                    });
                }else{

                }
            }

            @Override
            protected String doInBackground(String... strings) {
                String type = strings[0];
                if(type.equals("ID"))
                {
                    String result = "0";
                    try {
                        URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/getRecentID.php");
                        HttpURLConnection httpCon;
                        httpCon = (HttpURLConnection) url.openConnection();
                        httpCon.setRequestMethod("GET");

                        InputStream inStr = httpCon.getInputStream();
                        BufferedReader buffR = new BufferedReader(new InputStreamReader(inStr, "iso-8859-1"));
                        String line = "";
                        while ((line = buffR.readLine()) != null) {
                            result += line;
                        }
                        buffR.close();
                        inStr.close();
                        httpCon.disconnect();
                        //at this point, we have 4 id's separated by "|"
                        String[] collection = result.split("\\|");
                        recentIDs[0] = Integer.parseInt(collection[0]);
                        recentIDs[1] = Integer.parseInt(collection[1]);
                        recentIDs[2] = Integer.parseInt(collection[2]);
                        recentIDs[3] = Integer.parseInt(collection[3]);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "IDs Retrieved";
                }else if(type.equals("images")){
                    String id = strings[1];
                    String imgScript = "https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/retrieveImage.php?id=" +id;
                    URL url = null;
                    Bitmap img = null;
                    try {
                        url = new URL(imgScript);
                        img = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    temp = img;
                    imageIndex++;
                    return "Image" +imageIndex +" Retrieved";
                }else{
                    return "error";
                }
            }
        }
        GetImageData get = new GetImageData();
        get.execute("ID");
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
}