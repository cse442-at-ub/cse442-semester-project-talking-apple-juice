package com.example.theplug;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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

    //initialize our inbox elements
    public Button msg1;
    public ImageView pfp1;
    public TextView sender1;
    public String storedBody1;

    public Button msg2;
    public ImageView pfp2;
    public TextView sender2;
    public String storedBody2;

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

        //get interactible versions of our inbox elements
        msg1 = findViewById(R.id.openmessage1);
        pfp1 = findViewById(R.id.User1PFP);
        sender1 = findViewById(R.id.username1);

        msg2 = findViewById(R.id.openmessage2);
        pfp2 = findViewById(R.id.user2PFP);
        sender2 = findViewById(R.id.username2);

        //grab ALL messages sent to whoever is logged in and using this process.
        GetMessageData gmd = new GetMessageData();
        gmd.execute("Msg");
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

    //INNER CLASS: will collect data of messages meant for the user, and store them so we dont need to run any more unnecessary php scripts
    class GetMessageData extends AsyncTask<String, Void, String> {

        //initialize our variable holders
        String[] parsedResp;
        Bitmap temp;

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0]; //get the type of activity we're going to do. this is the first parameter passed in gmd.execute()
            if (type.equals("Msg")) {
                String response = "";
                try {
                    //initialize the URL. since this php script uses GET requests to get stored data, we need to set the un to be equal to the logged in user's username.
                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/getMessages.php?un=" +MainActivity.storedUsername);
                    //initialize http connection and access the script.
                    HttpURLConnection httpCon;
                    httpCon = (HttpURLConnection) url.openConnection();
                    httpCon.setRequestMethod("GET");

                    //read the response from the script, which will be EACH MESSAGE separated by a '*' character. EACH ELEMENT in A SINGLE MESSAGE is separated by a '|' character.
                    //TODO: prevent users from sending messages containing '|' or '*' characters. EASY
                    InputStream inStr = httpCon.getInputStream();
                    BufferedReader buffR = new BufferedReader(new InputStreamReader(inStr, "iso-8859-1"));
                    String line = ""; //build our response with the bufferedreader.
                    while ((line = buffR.readLine()) != null) {
                        response += line;
                    }
                    buffR.close();
                    inStr.close();
                    httpCon.disconnect();
                    parsedResp = response.split("\\*"); //split EACH MESSAGE and store EACH INDIVIDUAL MESSAGE in an array index.
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Msgs Retrieved"; //send this string to OnPostExecute. Note: this will still execute even if an error occurs, beware/fix later. Shouldn't be an issue RN.
            } else if (type.equals("Image")) {
                Bitmap img = null;
                try {
                    //lets get the profile picture of whoever sent messages! this is a little tricky, since we have multiple messages in the inbox.
                    //its a GET php, so we pass in the username of whoevers PFP we need.
                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/retrievePFP.php?un=" + strings[1]);
                    img = BitmapFactory.decodeStream(url.openConnection().getInputStream()); //since our images are stored in the DB encoded as Base64, we decode it.
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                temp = img; //store the image in a temporary variable, local to the entire inner class as initialized on creation.
                if(strings[2].equals("1"))
                {
                    return "Image Retrieved 1"; //retrieved the image of the first sender
                }
                if(strings[2].equals("2")) {
                    return "Image Retrieved 2"; //retrieved the image of the second sender
                }
                return "Retrieved"; //pass string on to onPostExecute
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
                    //only 1 message retrieved. update the inbox contents to show the data, and STORE THE MESSAGE TOO for later so we don't need to call another script.
                    String[] messageContent = parsedResp[0].split("\\|");
                    sender1.setText(messageContent[0]);
                    msg1.setText(messageContent[1]);
                    storedBody1 = messageContent[2];
                    new GetMessageData().execute("Image", messageContent[0], "1"); //we have the sender's username, so get their profile pic too.
                }else{
                    //2 more more msgs retrieved. update the inbox contents to show the data, and STORE THE MESSAGES TOO for later so we don't need to call another script.
                    String[] messageContent = parsedResp[0].split("\\|");
                    sender1.setText(messageContent[0]);
                    msg1.setText(messageContent[1]);
                    storedBody1 = messageContent[2];
                    new GetMessageData().execute("Image", messageContent[0], "1"); //we have sender 1's username, so get their profile pic
                    messageContent = parsedResp[1].split("\\|");
                    sender2.setText(messageContent[0]);
                    msg2.setText(messageContent[1]);
                    storedBody2 = messageContent[2];
                    new GetMessageData().execute("Image", messageContent[0], "2"); //we have the sender 2's username, so get their profile pic. TODO: optimization- if users are the same, just set both PFPs to be the same.
                }
            }else if(s.equals("Image Retrieved 1")){ //we got the image, and we have all the data for message 1. now, set a new on click listener to go to VIEWMESSAGEACTIVITY.
                pfp1.setImageBitmap(temp); //set the first pfp image view to hold the retrieved image.
                msg1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MessagesActivity.this, ViewMessagesActivity.class);
                        intent.putExtra("Sender", sender1.getText().toString()); //put extra key-value data in the intent, so we don't need to call the PHP script for data we already have.
                        intent.putExtra("Title", msg1.getText().toString());
                        intent.putExtra("Body", storedBody1);
                        startActivity(intent); //the onclicklistener is WAY down here, since we don't want to go to the VIEWMESSAGE page when we don't have all our message data!
                    }
                });
            }else if(s.equals("Image Retrieved 2")) { //same as image retrived 1, just for message 2.
                pfp2.setImageBitmap(temp);
                msg2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(MessagesActivity.this, ViewMessagesActivity.class);
                        intent.putExtra("Sender", sender2.getText().toString());
                        intent.putExtra("Title", msg2.getText().toString());
                        intent.putExtra("Body", storedBody2);
                        startActivity(intent);
                    }
                });

            }else{
                Toast err = Toast.makeText(MessagesActivity.this, "An error occured.", Toast.LENGTH_SHORT);
                err.show();
            }
        }
    }
}
