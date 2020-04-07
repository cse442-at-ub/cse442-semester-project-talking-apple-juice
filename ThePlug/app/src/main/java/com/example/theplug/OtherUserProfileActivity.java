package com.example.theplug;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class OtherUserProfileActivity extends AppCompatActivity {

    public TextView otherUser, otherUserRating;
    public ImageView otherUserPFP;
    public String theirUser;
    public String[] scoreList;
    public Bitmap temp;

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

        otherUser = findViewById(R.id.otherUsername);
        otherUserRating = findViewById(R.id.otherUserAvgRating);
        otherUserPFP = findViewById(R.id.otherUserProfile);
        otherUser.setText(theirUser);

        GetProfileDataHelper getpfp = new GetProfileDataHelper();
        getpfp.execute("pfp", theirUser);

        GetProfileDataHelper getrate = new GetProfileDataHelper();
        getrate.execute("score", theirUser);
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
            }else if(strings[0].equals("pfp"))
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
                    scoreAvg = scoreAvg + Integer.parseInt(score);
                }
                scoreAvg = (scoreAvg / scoreList.length);
                otherUserRating.setText(Float.toString(scoreAvg));
            }else if(s.equals("Image Retrieved"))
            {
                otherUserPFP.setImageBitmap(temp);
            }
        }
    }
}
