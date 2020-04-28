package com.example.theplug;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.example.theplug.MainActivity.storedUsername;


public class HomeScreen extends AppCompatActivity {

    private static final String CHANNEL_ID = "1";
    public ImageView bid1, bid2;
    public ImageView sale1, sale2;
    public int imageIndex = 0;
    public int[] recentIDs = {0,0,0,0};
    public String[] recentProdSeller = {"","","",""};
    public Bitmap temp = null;
    public String[] watchedIDs;

    public TextView searchProd;

    public int notificationId = 0;

    public String prodOwner;
    public String seen;


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

        init();

        getProduct();

        createNotificationChannel();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void init(){
        searchProd  = findViewById(R.id.searchV);

        //for now, the 4 products will just be the most recent posts by ID.
        bid1 = findViewById(R.id.bid1);
        bid2 = findViewById(R.id.bid2);
        sale1 = findViewById(R.id.sale1);
        sale2 = findViewById(R.id.sale2);

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_01);
            String description = getString(R.string.channel_Desc);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    //notiies sellers when a user views their product
    public void addNotification(String s) {
        Intent intent = new Intent(this, HomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);



        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.deafulticon)
                .setContentTitle("An item has been viewed!!")
                .setContentText(s)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());

    }

    // Notifies users when a watched product has been sold
    public void addWatchedSoldNotification(String s) {
        Intent intent = new Intent(this, ViewProductActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.deafulticon)
                .setContentTitle("Update on an item you're watching!")
                .setContentText(s)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());


    }
    public void gotoSearch(View view){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
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
                            String ID = Integer.toString(recentIDs[0]);
                            prodOwner = recentProdSeller[0];
                            if(!storedUsername.equals(prodOwner)) { //If the current user is not the same as the product seller.
                                new GetImageData().execute("view", ID, storedUsername );

                                 //notify product seller

                            }

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
                            prodOwner = recentProdSeller[1];
                            String ID = Integer.toString(recentIDs[1]);
                            if(!storedUsername.equals(prodOwner)) { //If the current user is not the same as the product seller.
                                new GetImageData().execute("view", ID, storedUsername );
                               // addNotification();                 //notify product seller


                            }
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
                            prodOwner = recentProdSeller[2];
                            String ID = Integer.toString(recentIDs[2]);
                            if(!storedUsername.equals(prodOwner)) { //If the current user is not the same as the product seller. SELLUSER IS NULL.... Y?
                                new GetImageData().execute("view", ID, storedUsername);
                              //  addNotification();                 //notify product owner


                            }
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
                            prodOwner = recentProdSeller[3];
                            String ID = Integer.toString(recentIDs[3]);
                            if(!storedUsername.equals(prodOwner)) { //If the current user is not the same as the product seller.
                                new GetImageData().execute("view", ID, storedUsername );
                               // addNotification();                 //notify product seller

                            }

                            Intent intent = new Intent(HomeScreen.this, ViewProductActivity.class);
                            String assocID = Integer.toString(recentIDs[3]);
                            intent.putExtra("ID", assocID);
                            startActivity(intent);
                        }
                    });
                }else if(s.contains(" has viewed ")){

                        addNotification(s);


                }else if(s.equals("Watches Retrieved")){
                    if(watchedIDs.length > 0)
                    {
                        for(String anID: watchedIDs)
                        {
                            new GetImageData().execute("checkSold", anID);
                            //new script to check if this id exists in Sold
                        }
                    }
                }else if(s.endsWith("has been sold!"))
                {
                    addWatchedSoldNotification(s); //show a notif
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

                        GetImageData test = new GetImageData();
                        test.execute("getViews", Integer.toString(recentIDs[0]));

                        //Store ID of the 4 recents products
                        recentIDs[0] = Integer.parseInt(collection[0]);
                        recentIDs[1] = Integer.parseInt(collection[2]);
                        recentIDs[2] = Integer.parseInt(collection[4]);
                        recentIDs[3] = Integer.parseInt(collection[6]);

                        //Store the username of those 4 recent products
                        recentProdSeller[0] = collection[1];
                        recentProdSeller[1] = collection[3];
                        recentProdSeller[2] = collection[5];
                        recentProdSeller[3] = collection[7];

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

                }else if(type.equals("view")){
                    String updateView ="https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/UpdateView.php";
                    try {
                        String id = strings[1];
                        String viewer = strings[2];
                        URL url = new URL(updateView);
                        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                        httpCon.setRequestMethod("POST");
                        httpCon.setDoOutput(true);
                        httpCon.setDoInput(true);
                        OutputStream outStr = httpCon.getOutputStream();
                        BufferedWriter buffW = new BufferedWriter(new OutputStreamWriter(outStr, "UTF-8"));
                        String req = URLEncoder.encode("id","UTF-8") + "=" +URLEncoder.encode(id , "UTF-8")
                                +"&" +URLEncoder.encode("view","UTF-8") + "=" +URLEncoder.encode(viewer , "UTF-8");
                        buffW.write(req);
                        buffW.flush();
                        buffW.close();
                        outStr.close();

                        InputStream inStr = httpCon.getInputStream();
                        BufferedReader buffR = new BufferedReader(new InputStreamReader(inStr, "iso-8859-1"));
                        String result = "";
                        String line = "";
                        while((line = buffR.readLine()) != null)
                        {
                            result += line;
                        }
                        buffR.close();
                        inStr.close();
                        httpCon.disconnect();
                        return result;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "View Sent";
                }
                else if(type.equals("getViews"))
                {
                    String result = "";
                    try {
                        URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/getViews.php?un=" + storedUsername );
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

                        seen = result;
                        return result; //This variable result returns the return string from the php file which is the echos
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "error";

                }else if(type.equals("getWatched"))
                {
                    String result = "";
                    try {
                        URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/getWatchedItemsSEC.php?un=" +storedUsername);
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
                        //at this point, we have some id's separated by "|"
                        watchedIDs = result.split("\\|");
                        return "Watches Retrieved";
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "error";
                }else if(type.equals("checkSold"))
                {
                    String result = "";
                    try {
                        URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/checkForSoldItemSEC.php?id=" +strings[1]);
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
                        //at this point, we have some id's separated by "|"
                        return result;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "error";
                }else{
                    return "error";
                }
            }
        }
        GetImageData get = new GetImageData();
        get.execute("ID");

        GetImageData one = new GetImageData();
        one.execute("getViews");


        GetImageData watch = new GetImageData();
        watch.execute("getWatched");
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