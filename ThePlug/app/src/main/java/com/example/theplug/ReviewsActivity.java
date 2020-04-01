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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class ReviewsActivity extends AppCompatActivity {

    public ImageView senderPFP;
    public TextView senderUser, ratingUser, msg;
    public Button submitReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
        {
            setTheme(R.style.lightTheme);
        }else{
            setTheme(R.style.darkTheme);
        }
        setContentView(R.layout.activity_reviews);

        init();
//
//        final Bundle extras = getIntent().getExtras();
//        senderUser.setText(extras.getString("Sender"));
//        ratingUser.setText(extras.getString("Rating"));
//
//        submitReview.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                if(msg.getText().toString() == "")
//                {
//                    //error toast: cant send empty message!
//                    Toast err = Toast.makeText(getApplicationContext(), "Invalid: Input a proper review.", Toast.LENGTH_SHORT);
//                    err.show();
//                }else{
//                    BackgroundReviewHelper bgr = new BackgroundReviewHelper();
//                    bgr.execute("Send", MainActivity.storedUsername, extras.getString("Sender"), extras.getString("Rating"), msg.getText().toString());
//
//                }
//            }
//        });

    }

    public void init(){
        senderPFP = findViewById(R.id.imageView);
        senderUser = findViewById(R.id.textView6);
        ratingUser = findViewById(R.id.textView7);
        msg = findViewById(R.id.textView8);
        submitReview = findViewById(R.id.sendReview);

    }

//    class BackgroundReviewHelper extends AsyncTask<String,  Void, String>{
//
//        Bitmap temp;
//
//        @Override
//        protected String doInBackground(String... strings) {
//            String type = strings[0];
//            if(type.equals("Image")){
//                Bitmap img = null;
//                try{
//                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/retrievePFP.php?un=" + strings[1]);
//                    img = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                temp = img;
//                return "Retrieved";
//            }else if(type.equals("Send")){
//                try{
//                    String sender = strings[1];
//                    String recip = strings[2];
//                    String rating = strings[3];
//                    String msg = strings[4];
//                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/sendReview.php");
//
//                    HttpURLConnection httpCon;
//                    httpCon = (HttpURLConnection) url.openConnection();
//                    httpCon.setRequestMethod("POST");
//                    httpCon.setDoOutput(true);
//                    httpCon.setDoInput(true);
//                    OutputStream outStr = httpCon.getOutputStream();
//                    BufferedWriter buffW = new BufferedWriter(new OutputStreamWriter(outStr,"UTF-8"));
//                    String req = URLEncoder.encode("frm", "UTF-8") + "=" + URLEncoder.encode(sender, "UTF-8")
//                            + "&" + URLEncoder.encode("to", "UTF-8") + "=" + URLEncoder.encode(recip, "UTF-8")
//                            + "&" + URLEncoder.encode("rating", "UTF-8") + "=" + URLEncoder.encode(rating, "UTF-8")
//                            + "&" + URLEncoder.encode("msg", "UTF-8") + "=" + URLEncoder.encode(msg, "UTF-8");
//                    buffW.write(req);
//                    buffW.flush();
//                    buffW.close();
//                    outStr.close();
//
//                    InputStream inStr = httpCon.getInputStream();
//                    BufferedReader buffR = new BufferedReader(new InputStreamReader(inStr, "iso-8859-1"));
//                    String result = "";
//                    String line = "";
//                    while((line = buffR.readLine()) != null){
//                        result += line;
//                    }
//                    buffR.close();
//                    inStr.close();
//                    httpCon.disconnect();
//                    return result;
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                return "error";
//            }
//            return "error";
//        }
//
//        @Override
//        protected void onPostExecute(String s)
//        {
//            super.onPostExecute(s);
//            if(s.equals("Retrieved"))
//            {
//                senderPFP.setImageBitmap(temp);
//            }else if(s.equals("Review Submitted"))
//            {
//                Toast good = Toast.makeText(getApplicationContext(), "Review Submitted", Toast.LENGTH_SHORT);
//                good.show();
//                finish();
//            }
//        }
//    }
}
