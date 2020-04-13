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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static com.example.theplug.ViewProductActivity.sellUSER;

public class OtherUserProfileActivity extends AppCompatActivity {

    public TextView otherUser, otherUserRating, buyerRating, buyerRatingValue;
    public ImageView otherUserPFP;
    public String theirUser;
    public String[] scoreList;
    public String[] buyerScoreList;
    public Bitmap temp;
    public Button leaveBuyerRating, leaveSellerRating;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            setTheme(R.style.lightTheme);
        } else {
            setTheme(R.style.darkTheme);
        }
        setContentView(R.layout.activity_view_others_profile);

        theirUser = getIntent().getStringExtra("Seller");

        init();
        otherUser.setText(theirUser);

        GetProfileDataHelper getpfp = new GetProfileDataHelper();
        getpfp.execute("pfp", theirUser);

        GetProfileDataHelper getrate = new GetProfileDataHelper();
        getrate.execute("score", theirUser);

        GetProfileDataHelper getBuyrate = new GetProfileDataHelper();
        getBuyrate.execute("buyerScore", theirUser);


        leaveSellerRating.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtherUserProfileActivity.this, ReviewsActivity.class);
                intent.putExtra("Sender", sellUSER);
                startActivity(intent);
            }
        });

        leaveBuyerRating.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtherUserProfileActivity.this, ReviewsBuyerActivity.class);
                intent.putExtra("Sender", sellUSER);
                startActivity(intent);
            }
        });



    }

    public void init(){

        otherUser = findViewById(R.id.otherUsername);
        otherUserRating = findViewById(R.id.otherUserAvgRating);
        otherUserPFP = findViewById(R.id.otherUserProfile);

        buyerRating = findViewById(R.id.buyerRating);
        buyerRatingValue = findViewById(R.id.buyerRatingValue);
        leaveBuyerRating = findViewById(R.id.leaveBuyerRating);
        leaveSellerRating = findViewById(R.id.leaveSellerRating);


    }


    class GetProfileDataHelper extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings) {
            if(strings[0].equals("score")) {
                String response = "";
                try{
                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/getUserReviewScores.php?un=" +strings[1]);

                    HttpURLConnection httpCon;
                    httpCon = (HttpURLConnection) url.openConnection();
                    httpCon.setRequestMethod("GET");

                    InputStream inStr = httpCon.getInputStream();
                    BufferedReader buffR = new BufferedReader(new InputStreamReader(inStr, "iso-8859-1"));
                    String line = "";
                    while((line = buffR.readLine()) != null){
                        response += line;
                    }
                    buffR.close();
                    inStr.close();
                    httpCon.disconnect();
                    scoreList = response.split("\\|");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Scores Received";
            }else if(strings[0].equals("buyerScore")){
                String response = "";
                try{
                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/getUserReviewBuyerScores.php?un=" +strings[1]);

                    HttpURLConnection httpCon;
                    httpCon = (HttpURLConnection) url.openConnection();
                    httpCon.setRequestMethod("GET");

                    InputStream inStr = httpCon.getInputStream();
                    BufferedReader buffR = new BufferedReader(new InputStreamReader(inStr, "iso-8859-1"));
                    String line = "";
                    while((line = buffR.readLine()) != null){
                        response += line;
                    }
                    buffR.close();
                    inStr.close();
                    httpCon.disconnect();
                    buyerScoreList = response.split("\\|");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Buyer Scores Received";

            } else if(strings[0].equals("pfp"))
            {
                Bitmap img = null;
                try{
                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/retrievePFP.php?un=" + strings[1]);
                    img = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                temp = img;
                return "Image Retrieved";
            }
            return "error";
        }

        @Override
        protected void onPostExecute(String s)
        {
            if(s.equals("Scores Received")) {
                float scoreAvg = 0;
                for (String score : scoreList) {
                    if(!score.equals("")){
                        scoreAvg += Integer.parseInt(score);
                    }else{

                    }
                }
                    scoreAvg = (scoreAvg / scoreList.length);
                    otherUserRating.setText(Float.toString(scoreAvg));

            }else if(s.equals("Buyer Scores Received")){
                float scoreAvg = 0;
                for (String score : buyerScoreList) {
                    if(!score.equals("")){
                        scoreAvg += Integer.parseInt(score);
                    }else{

                    }
                }
                scoreAvg = (scoreAvg / buyerScoreList.length);
                buyerRatingValue.setText(Float.toString(scoreAvg));
            }
            else if(s.equals("Image Retrieved"))
            {
                otherUserPFP.setImageBitmap(temp);
            }
        }
    }
}
