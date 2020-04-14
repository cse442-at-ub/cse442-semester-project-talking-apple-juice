package com.example.theplug;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ViewMessagesActivity extends AppCompatActivity {
    //EVERY 5-10 SECONDS, CALL BGACTIVITY("GET") FOR "UPDATES"

    //initialize our user aspects
    public ImageView senderPFP;
    public TextView senderUser;
    public RecyclerView prevMsgs;
    public RecyclerView.Adapter prevMsgsAdapter;
    public String msgContent = "";

    public ArrayList msgList;

    public TextView msgReply;
    public Button sendMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set dark or light theme
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
        {
            setTheme(R.style.lightTheme);
        }else{
            setTheme(R.style.darkTheme);
        }
        setContentView(R.layout.activity_view_message);

        //get interactible versions of our user aspects/inputs
        senderPFP = findViewById(R.id.SenderProfile);
        senderUser = findViewById(R.id.SenderUser);
        prevMsgs = findViewById(R.id.messageHistory);

        msgReply = findViewById(R.id.replyBody);
        sendMsg = findViewById(R.id.replyButton);

        //this is a collection of all the key-value pairs passed along with the intent in the GETMESSAGEDATA inner class in MESSAGESACTIVITY.
        final Bundle extras = getIntent().getExtras();
        //set relevant info inside the viewable aspects, so we can read the message and whatnot.
        senderUser.setText(extras.getString("Sender"));

        //creating an onclicklistener for the sendMsg button.
        //NOTE: I do this intead of setting the "android:OnClick" in the xml, since I usually need to pass data through on a button press, and I want to make sure the data is initialized/set first.
        sendMsg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                msgContent = msgReply.getText().toString();
                if(msgContent.trim().length() == 0)
                {
                    //error toast: cant send empty message!
                    Toast err = Toast.makeText(getApplicationContext(), "Message cannot be empty.", Toast.LENGTH_SHORT);
                    err.show();
                }else if(msgContent.contains("|") || msgContent.contains("*")) {
                    Toast err = Toast.makeText(getApplicationContext(), "Message cannot contain '|' or '*'.", Toast.LENGTH_SHORT);
                    err.show();
                }else if(msgContent.length() > 198) {
                    Toast err = Toast.makeText(getApplicationContext(), "Message is too long. Max 198 chars.", Toast.LENGTH_SHORT);
                    err.show();
                }else if(MainActivity.storedUsername.equals(extras.getString("Sender"))) {
                    Toast err = Toast.makeText(getApplicationContext(), "Can't send a message to yourself!", Toast.LENGTH_SHORT);
                    err.show();
                }else{
                    BackgroundMessagerHelper bgms = new BackgroundMessagerHelper(); //new asynctask inner class instance to send the message
                    //execute it with passed parameters: "Send", app user's username, recipient user, pre-generated title of message, message response body text
                    bgms.execute("Send", MainActivity.storedUsername, extras.getString("Sender"), msgContent);

                }
            }
        });

        BackgroundMessagerHelper bgmi = new BackgroundMessagerHelper();
        bgmi.execute("Image", extras.getString("Sender"));

        BackgroundMessagerHelper bgmd = new BackgroundMessagerHelper();
        bgmd.execute("Get", extras.getString("Sender"));
    }

    class BackgroundMessagerHelper extends AsyncTask<String, Void, String> {

        Bitmap temp;
        String response = "";
        String[] parsedResp;

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            if (type.equals("Image")) {
                //we do need to get the profilepic again, since I couldn't find a way to pass it through intents! :( i tried a lot, so just leave it for now.
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
                return "Retrieved";
            } else if (type.equals("Get")) {
                //lets send our message. STRINGS[1] is our other user. We're MainActivity.storedUsername
                //remember, we pass many parameters when calling BMH.execute to send a message.
                try {
                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/getDMSEC.php?u=" + MainActivity.storedUsername + "&p=" + strings[1]);
                    HttpURLConnection httpCon;
                    httpCon = (HttpURLConnection) url.openConnection();
                    httpCon.setRequestMethod("GET");

                    //read the response from the script, which will be EACH SENDER WITH THEIR MOST RECENT TIME separated by a '*' character. EACH USER/TIME COMBO SEPARATED BY "|"
                    //TODO: prevent users from creating usernames containing '|' or '*' characters. EASY
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
                return "Msgs Retrieved"; //send this string to OnPostExecute. Note: this will still execute even if an error occurs, beware/fix later. Shouldn't be an issue RN.
            } else if (type.equals("Send")) {
                //lets send our message.
                try {
                    String sender = strings[1]; //we passed in our stored username for whoever is using this process.
                    String recip = strings[2]; //we passed in the sender of the message, who is now the recipient of the reply.
                    String body = strings[3]; //passed in the body
                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/sendMessageSEC.php");
                    //THIS IS A POST REQUEST, NOT A GET. We dont append a "?un=" at the end of it. Instead, we send our data through an output stream, encoded to UTF-8.
                    //it is weird but it's how it works.
                    HttpURLConnection httpCon;
                    httpCon = (HttpURLConnection) url.openConnection();
                    httpCon.setRequestMethod("POST");
                    httpCon.setDoOutput(true);
                    httpCon.setDoInput(true);
                    OutputStream outStr = httpCon.getOutputStream();
                    BufferedWriter buffW = new BufferedWriter(new OutputStreamWriter(outStr, "UTF-8"));
                    String req = URLEncoder.encode("frm", "UTF-8") + "=" + URLEncoder.encode(sender, "UTF-8")
                            + "&" + URLEncoder.encode("to", "UTF-8") + "=" + URLEncoder.encode(recip, "UTF-8")
                            + "&" + URLEncoder.encode("msg", "UTF-8") + "=" + URLEncoder.encode(body, "UTF-8");
                    buffW.write(req);
                    buffW.flush();
                    buffW.close();
                    outStr.close();

                    //now, make an input stream to read the response of the PHP script. on success, we should get "Message Sent!", since that's what sendMessage.php echoes.
                    InputStream inStr = httpCon.getInputStream();
                    BufferedReader buffR = new BufferedReader(new InputStreamReader(inStr, "iso-8859-1"));
                    String line = "";
                    while ((line = buffR.readLine()) != null) {
                        response += line;
                    }
                    buffR.close();
                    inStr.close();
                    httpCon.disconnect();
                    return response;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "error";
            } else {
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            if(s.equals("Retrieved"))
            {
                //we have an image to set the imageview to now.
                senderPFP.setImageBitmap(temp);
            }else if(s.equals("Msgs Retrieved")){
                //update recyclerview to show the messages
                msgList = new ArrayList();

                for (String message : parsedResp) {
                    String[] contents = message.split("\\*");
                    msgList.add("From: " +contents[3] +" at " +contents[1] +"\n" +contents[0]);    //Arraylist that stores all those values
                }
                prevMsgs.setHasFixedSize(true);
                prevMsgs.setLayoutManager(new LinearLayoutManager(new TransactionsActivity()));
                prevMsgsAdapter = new ViewProductAdapter(msgList);
                prevMsgs.setAdapter(prevMsgsAdapter);

                //message sent successfully! let the user know, then FINISH THIS INSTANCE OF VIEWMESSAGESACTIVITY.
               // Toast good = Toast.makeText(getApplicationContext(), "Message sent.", Toast.LENGTH_SHORT);
                //good.show();
                //finish();
                //!!!CALLING FINISH WHEN YOU'RE DONE USING AN ACTIVITY, INSTEAD OF JUST MAKING A NEW INTENT, IS EXTREMELY IMPORTANT!!!
                //MAKING AND GOING TO A NEW INTENT JUST STACKS A NEW ACTIVITY ON TOP OF WHATEVER ACTIVITIES EXIST BEFORE IT, IT DOES NOT CLOSE THE PREVIOUS ACTIVITY.
                //FINISH CLOSES THE CURRENT ACTIVITY AND GOES BACK TO THE PREVIOUS ONE ON THE STACK. THIS MAKES THE APP FLOW BETTER, AND KEEPS IT RUNNING SMOOTHLY/AVOIDS CRASHES.
            }else if(s.equals("Sent!")){
                Toast suc = Toast.makeText(getApplicationContext(), "Message sent.", Toast.LENGTH_SHORT);
                suc.show();
                msgReply.setText("");
                BackgroundMessagerHelper bgmd = new BackgroundMessagerHelper();
                bgmd.execute("Get", senderUser.getText().toString());
            }
        }
    }
}
