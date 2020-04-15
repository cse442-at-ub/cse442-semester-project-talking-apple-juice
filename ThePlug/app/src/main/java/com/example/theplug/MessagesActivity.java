package com.example.theplug;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class MessagesActivity extends AppCompatActivity implements MessageListAdapter.ItemClickListener{

    //initialize our inbox elements

    public RecyclerView msgList;
    public MessageListAdapter recAdapter;
    public ArrayList store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set light or dark theme
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
        {
            setTheme(R.style.lightTheme);
        }else{
            setTheme(R.style.darkTheme);
        }
        setContentView(R.layout.activity_messages);

        //get interactible version of our inbox elements
        msgList = findViewById(R.id.messageList);

        //grab ALL messages sent to whoever is logged in and using this process.
        GetMessageData gmd = new GetMessageData();
        gmd.execute("MsgList");
    }

    //triple dot menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //go to wherever you press!
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

    @Override
    public void onItemClick(View view, int position) {
        Intent in = new Intent(this, ViewMessagesActivity.class);
        in.putExtra("Sender", recAdapter.getItem(position));
        in.putExtra("Title", "test");
        in.putExtra("Body", "lol");
        startActivity(in);
    }

    //INNER CLASS: will collect data of messages meant for the user, and store them so we dont need to run any more unnecessary php scripts
    class GetMessageData extends AsyncTask<String, Void, String> {

        //initialize our variable holders
        String[] parsedResp;

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0]; //get the type of activity we're going to do. this is the first parameter passed in gmd.execute()
            if(type.equals("MsgList")) {
                    String response = "";
                    try {
                        //initialize the URL. since this php script uses GET requests to get stored data, we need to set the un to be equal to the logged in user's username.
                        URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/getDMSendersSEC.php?un=" +MainActivity.storedUsername);
                        //initialize http connection and access the script.
                        HttpURLConnection httpCon;
                        httpCon = (HttpURLConnection) url.openConnection();
                        httpCon.setRequestMethod("GET");

                        //read the response from the script, which will be EACH SENDER WITH THEIR MOST RECENT TIME separated by a '*' character. EACH USER/TIME COMBO SEPARATED BY "|"
                        InputStream inStr = httpCon.getInputStream();
                        BufferedReader buffR = new BufferedReader(new InputStreamReader(inStr, "iso-8859-1"));
                        String line = ""; //build our response with the bufferedreader.
                        while ((line = buffR.readLine()) != null) {
                            response += line;
                        }
                        buffR.close();
                        inStr.close();
                        httpCon.disconnect();
                        parsedResp = response.split("\\|"); //split EACH MESSAGE and store EACH INDIVIDUAL MESSAGE in an array index.
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "List Retrieved"; //send this string to OnPostExecute. Note: this will still execute even if an error occurs, beware/fix later. Shouldn't be an issue RN.
            }else{
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            if(s.equals("List Retrieved")) {
                store = new ArrayList<String>();
                for (String user : parsedResp) {
                    store.add(user);   //Arraylist that stores all those values
                }
                msgList.setHasFixedSize(true);
                msgList.setLayoutManager(new LinearLayoutManager(new MessagesActivity()));
                recAdapter = new MessageListAdapter(MessagesActivity.this, store);
                recAdapter.setClickListener(MessagesActivity.this);
                msgList.setAdapter(recAdapter);

            }else{
                Toast err = Toast.makeText(MessagesActivity.this, "An error occured.", Toast.LENGTH_SHORT);
                err.show();
            }
        }
    }
}
