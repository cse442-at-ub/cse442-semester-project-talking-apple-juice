package com.example.theplug;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ViewMessagesActivity extends AppCompatActivity {

    public ImageView senderPFP;
    public TextView senderUser;
    public TextView msgTitle;
    public TextView msgBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
        {
            setTheme(R.style.lightTheme);
        }else{
            setTheme(R.style.darkTheme);
        }
        setContentView(R.layout.activity_view_message);

        senderPFP = findViewById(R.id.SenderProfile);
        senderUser = findViewById(R.id.SenderUser);
        msgTitle = findViewById(R.id.msgTitle);
        msgBody = findViewById(R.id.msgBody);

        Bundle extras = getIntent().getExtras();
        senderUser.setText(extras.getString("Sender"));
        msgTitle.setText(extras.getString("Title"));
        msgBody.setText(extras.getString("Body"));

        GetPFPData gpfp = new GetPFPData();
        gpfp.execute("Image", extras.getString("Sender"));
    }

    class GetPFPData extends AsyncTask<String, Void, String> {

        Bitmap temp;

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
                if (type.equals("Image")) {
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
            }else{
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            if(s.equals("Retrieved"))
            {
                senderPFP.setImageBitmap(temp);
            }
        }
    }
}
