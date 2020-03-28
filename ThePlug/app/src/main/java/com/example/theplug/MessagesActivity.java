package com.example.theplug;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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

public class MessagesActivity extends AppCompatActivity {

    public Button msg1;
    public ImageView pfp1;
    public TextView sender1;

    public Button msg2;
    public ImageView pfp2;
    public TextView sender2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
        {
            setTheme(R.style.lightTheme);
        }else{
            setTheme(R.style.darkTheme);
        }
        setContentView(R.layout.activity_messages);

        msg1 = findViewById(R.id.openmessage1);
        pfp1 = findViewById(R.id.User1PFP);
        sender1 = findViewById(R.id.username1);

        msg2 = findViewById(R.id.openmessage2);
        pfp2 = findViewById(R.id.user2PFP);
        sender2 = findViewById(R.id.username2);

        msg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessagesActivity.this, ViewMessagesActivity.class);
                startActivity(intent);
            }
        });

        msg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MessagesActivity.this, ViewMessagesActivity.class);
                startActivity(intent);
            }
        });

        GetMessageData gmd = new GetMessageData();
        gmd.execute("Msg");
        //TODO: php script to get most 2 most recent messages from database


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

    class GetMessageData extends AsyncTask<String, Void, String> {

        String[] parsedResp;
        Bitmap temp;

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            if (type.equals("Msg")) {
                String response = "";
                try {
                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/getMessages.php?un=" +MainActivity.storedUsername);
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
                    parsedResp = response.split("\\*");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Msgs Retrieved";
            } else if (type.equals("Image")) {
                Bitmap img = null;
                try {
                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/retrievePFP.php?un=" + strings[1]);
                    img = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                temp = img;
                if(strings[2].equals("1"))
                {
                    return "Image Retrieved 1";
                }
                return "Image Retrieved 2";
            }else{
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            if(s.equals("Msgs Retrieved")) {
                if(parsedResp.length == 0)
                {
                    //0 msgs retrieved
                    Toast noMsg = Toast.makeText(MessagesActivity.this, "No messages.", Toast.LENGTH_SHORT);
                    noMsg.show();
                }else if(parsedResp.length == 1)
                {
                    //only 1 message retrieved
                    String[] messageContent = parsedResp[0].split("\\|");
                    sender1.setText(messageContent[0]);
                    msg1.setText(messageContent[1]);
                    new GetMessageData().execute("Image", messageContent[0], "1");
                }else{
                    //2 more more msgs retrieved
                    String[] messageContent = parsedResp[0].split("\\|");
                    sender1.setText(messageContent[0]);
                    msg1.setText(messageContent[1]);
                    new GetMessageData().execute("Image", messageContent[0], "1");
                    messageContent = parsedResp[1].split("\\|");
                    sender2.setText(messageContent[0]);
                    msg2.setText(messageContent[1]);
                    new GetMessageData().execute("Image", messageContent[0], "2");
                }
            }else if(s.equals("Image Retrieved 1")){
                pfp1.setImageBitmap(temp);
            }else if(s.equals("Image Retrieved 2")) {
                pfp2.setImageBitmap(temp);
            }else{
                Toast err = Toast.makeText(MessagesActivity.this, "An error occured.", Toast.LENGTH_SHORT);
                err.show();
            }
        }
    }
}
