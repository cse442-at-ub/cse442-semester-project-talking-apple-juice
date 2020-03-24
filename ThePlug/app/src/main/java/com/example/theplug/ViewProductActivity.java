package com.example.theplug;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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

public class ViewProductActivity extends AppCompatActivity {

    public ImageView ProductImg;
    public TextView Name;
    public TextView Desc;
    public TextView Price;
    public String ID = "";
    public String[] parsedResp;
    public Bitmap temp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
        {
            setTheme(R.style.lightTheme);
        }else{
            setTheme(R.style.darkTheme);
        }
        setContentView(R.layout.activity_view_product);

        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");

        ProductImg = findViewById(R.id.productImg);
        Name = findViewById(R.id.itemNameTextView8);
        Desc = findViewById(R.id.itemDescTextView7);
        Price = findViewById(R.id.itemPriceTextView);

        getData();

        //GET IMAGE FROM DB

        //GET TEXT FROM DB, PARSE THE VALUES BY '|' CHAR
    }

    public void getData(){

        class GetProductData extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... strings) {
                String type = strings[0];
                if(type.equals("Data"))
                {
                    String response = "";
                    try {
                        URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/getProductInfo.php?id=" +ID);
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
                        parsedResp = response.split("\\|");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "Data Retrieved";
                }else if(type.equals("Image")){
                    Bitmap img = null;
                    try {
                        URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/retrieveImage.php?id=" +ID);
                        img = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    temp = img;
                    return "Image Retrieved";
                }else{
                    return "error";
                }
            }

            @Override
            protected void onPostExecute(String s)
            {
                if(s.equals("Data Retrieved"))
                {
                    new GetProductData().execute("Image");
                    Name.setText(parsedResp[0]);
                    Desc.setText(parsedResp[1]);
                    Price.setText("$" +parsedResp[2]);
                }else{
                    ProductImg.setImageBitmap(temp);
                }
                super.onPostExecute(s);
            }
        }

        GetProductData get = new GetProductData();
        get.execute("Data");

    }
}
