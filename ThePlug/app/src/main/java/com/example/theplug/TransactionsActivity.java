package com.example.theplug;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import static com.example.theplug.ViewProductActivity.watchChecker;

public class TransactionsActivity extends AppCompatActivity {

    private static RecyclerView prodList , watchList;
    public  static RecyclerView.Adapter<ViewProductAdapter.ViewHolder> mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
        {
            setTheme(R.style.lightTheme);
        }else{
            setTheme(R.style.darkTheme);
        }
        setContentView(R.layout.activity_transactions);



        final Bundle extras = getIntent().getExtras();

        prodList = findViewById(R.id.soldRecycler);
        watchList = findViewById(R.id.watchRecycler);

        new productSold().execute("soldList");
        new productSold().execute("watchList");

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


    public static class productSold extends AsyncTask<String, Void, String> {


        String[] parsedResp;
        ArrayList<String> soldList;
        ArrayList<String> watcherList;

       @Override
       protected String doInBackground(String... params) {
           String check = params[0];
           String soldProdScript = "https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/uploadSoldProdSEC.php";
           String getSoldProdScript = "https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/getSoldProd.php";
           String watchScript = "https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/uploadWatchSEC.php";
           String getWatchScript = "https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/getWatchList.php";
           if (check.equals("sold")) {
               try {
                   String name = params[1];
                   String price = params[2];
                   String desc = params[3];
                   String id = params[4];
                   String img = params[5];
                   String com = params[6];
                   String user = params[7];
                   URL url = new URL(soldProdScript);
                   HttpURLConnection httpCon;
                   httpCon = (HttpURLConnection) url.openConnection();
                   httpCon.setRequestMethod("POST");
                   httpCon.setDoOutput(true);
                   httpCon.setDoInput(true);
                   OutputStream outStr = httpCon.getOutputStream();
                   BufferedWriter buffW = new BufferedWriter(new OutputStreamWriter(outStr, "UTF-8"));
                   String req = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8")
                           //  + "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")
                           + "&" + URLEncoder.encode("price", "UTF-8") + "=" + URLEncoder.encode(price, "UTF-8")
                           + "&" + URLEncoder.encode("desc", "UTF-8") + "=" + URLEncoder.encode(desc, "UTF-8")
                           + "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8")
                           + "&" + URLEncoder.encode("ei", "UTF-8") + "=" + URLEncoder.encode(img, "UTF-8")
                           + "&" + URLEncoder.encode("co", "UTF-8") + "=" + URLEncoder.encode(com, "UTF-8")
                           + "&" + URLEncoder.encode("uname", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8");
                   ;
                   buffW.write(req);
                   buffW.flush();
                   buffW.close();
                   outStr.close();

                   InputStream inStr = httpCon.getInputStream();
                   BufferedReader buffR = new BufferedReader(new InputStreamReader(inStr, "iso-8859-1"));
                   String result = "";
                   String line = "";
                   while ((line = buffR.readLine()) != null) {
                       result += line;
                   }
                   buffR.close();
                   inStr.close();
                   httpCon.disconnect();
                   return result;
               } catch (UnsupportedEncodingException e) {
                   e.printStackTrace();
               } catch (ProtocolException e) {
                   e.printStackTrace();
               } catch (MalformedURLException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           } else if (check.equals("soldList")) {
               String response = "";
               try {
                   URL url = new URL(getSoldProdScript);

                   HttpURLConnection httpCon;
                   httpCon = (HttpURLConnection) url.openConnection();
                   httpCon.setRequestMethod("GET");

                   InputStream inStr = httpCon.getInputStream();
                   BufferedReader buffR = new BufferedReader(new InputStreamReader(inStr, "iso-8859-1"));
                   String line = "";
                   while ((line = buffR.readLine()) != null) {
                       response += line;
                   }
                   buffR.close();
                   inStr.close();
                   httpCon.disconnect();

                   if (response.equals("nothing found")) {
                       parsedResp = new String[1];
                       parsedResp[0] = "nothing found";
                   } else {
                       parsedResp = response.split("\\*");
                   }
               } catch (ProtocolException e) {
                   e.printStackTrace();
               } catch (MalformedURLException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }
               return "Product Sold";

           }else if (check.equals("watch")) {
               try {
                   String name = params[1];
                   String price = params[2];
                   String desc = params[3];
                   String id = params[4];
                   String img = params[5];
                   String user = params[6];
                   String watcher = params[7];
                   String watchStatus = params[8];

                   URL url = new URL(watchScript);
                   HttpURLConnection httpCon;
                   httpCon = (HttpURLConnection) url.openConnection();
                   httpCon.setRequestMethod("POST");
                   httpCon.setDoOutput(true);
                   httpCon.setDoInput(true);
                   OutputStream outStr = httpCon.getOutputStream();
                   BufferedWriter buffW = new BufferedWriter(new OutputStreamWriter(outStr, "UTF-8"));
                   String req = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8")
                           + "&" + URLEncoder.encode("price", "UTF-8") + "=" + URLEncoder.encode(price, "UTF-8")
                           + "&" + URLEncoder.encode("desc", "UTF-8") + "=" + URLEncoder.encode(desc, "UTF-8")
                           + "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8")
                           + "&" + URLEncoder.encode("ei", "UTF-8") + "=" + URLEncoder.encode(img, "UTF-8")
                           + "&" + URLEncoder.encode("uname", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8")
                           + "&" + URLEncoder.encode("sname", "UTF-8") + "=" + URLEncoder.encode(watcher, "UTF-8")
                           + "&" + URLEncoder.encode("stat", "UTF-8") + "=" + URLEncoder.encode(watchStatus, "UTF-8");
                   ;
                   buffW.write(req);
                   buffW.flush();
                   buffW.close();
                   outStr.close();

                   InputStream inStr = httpCon.getInputStream();
                   BufferedReader buffR = new BufferedReader(new InputStreamReader(inStr, "iso-8859-1"));
                   String result = "";
                   String line = "";
                   while ((line = buffR.readLine()) != null) {
                       result += line;
                   }
                   buffR.close();
                   inStr.close();
                   httpCon.disconnect();
                   return result;
               } catch (UnsupportedEncodingException e) {
                   e.printStackTrace();
               } catch (ProtocolException e) {
                   e.printStackTrace();
               } catch (MalformedURLException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }else if (check.equals("watchList")) {
               String response = "";
               try {
                   URL url = new URL(getWatchScript);

                   HttpURLConnection httpCon;
                   httpCon = (HttpURLConnection) url.openConnection();
                   httpCon.setRequestMethod("GET");

                   InputStream inStr = httpCon.getInputStream();
                   BufferedReader buffR = new BufferedReader(new InputStreamReader(inStr, "iso-8859-1"));
                   String line = "";
                   while ((line = buffR.readLine()) != null) {
                       response += line;
                   }
                   buffR.close();
                   inStr.close();
                   httpCon.disconnect();

                   if (response.equals("nothing found")) {
                       parsedResp = new String[1];
                       parsedResp[0] = "nothing found";
                   } else {
                       parsedResp = response.split("\\*");
                   }

               } catch (ProtocolException e) {
                   e.printStackTrace();
               } catch (MalformedURLException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }
               return "Grabbed Watch List";
           }
               return null;
       }

       @Override
       protected void onPreExecute() {
           super.onPreExecute();
       }

       @Override
       protected void onPostExecute(String str) {
           if (str.equals("Product Sold")) {
               soldList = new ArrayList<>();

               for (String message : parsedResp) {
                   String[] msg = message.split("\\|"); //Split the string array by each "|"
                   try {
                       if (msg[5].equals(storedUsername)) {  //Compared the current user with the product user
                           String prodName = msg[0];    //Represents the reviewMessage from the user. Index 1 is the message
                           soldList.add(prodName);    //Arraylist that stores all those values
                       }
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }

               prodList.setHasFixedSize(true);
               prodList.setLayoutManager(new LinearLayoutManager(new TransactionsActivity()));
               mAdapter = new ViewProductAdapter(soldList);
               prodList.setAdapter(mAdapter);

           } else if (str.equals("Grabbed Watch List")) {
               watcherList = new ArrayList<>();

               for (String message : parsedResp) {
                   String[] msg = message.split("\\|"); //Split the string array by each "|"
                   try {
                       if (msg[4].equals(storedUsername)) {  //Compared the current user with the product user
                           String prodName = msg[0];    //Represents the item name.
                           watcherList.add(prodName);    //Arraylist that stores all those values
                       }
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }

               watchList.setHasFixedSize(true);
               watchList.setLayoutManager(new LinearLayoutManager(new TransactionsActivity()));
               mAdapter = new ViewProductAdapter(watcherList);
               watchList.setAdapter(mAdapter);


           }
       }
    }
   }



