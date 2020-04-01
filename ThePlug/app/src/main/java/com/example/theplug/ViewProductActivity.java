package com.example.theplug;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ViewProductActivity extends AppCompatActivity {

    public ImageView ProductImg;
    public TextView Name;
    public TextView Desc;
    public TextView Price;
    public TextView Comment;
    public TextView SellerUser;
    public String ID = "";
    public String[] parsedResp;
    public Bitmap temp = null;

    public EditText commentData;
    public Button addComment;
    public Button contactSeller;
    public Button reviewSeller;

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
        Comment = findViewById(R.id.itemCommentTextView);
        SellerUser = findViewById(R.id.soldByUser);

        commentData = findViewById(R.id.commentBox);
        addComment = findViewById(R.id.addComButton);
        contactSeller = findViewById(R.id.button2);
        reviewSeller  = findViewById(R.id.buttonReview);

        addComment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String cd = commentData.getText().toString();
                if(cd.equals(null) || cd.trim().equals(""))
                {
                    Toast incorrect = Toast.makeText(getApplicationContext(), "Can't leave an empty comment!", Toast.LENGTH_SHORT);
                    incorrect.show();
                }else{
                    GetProductData sendC = new GetProductData();
                    sendC.execute("Com", ID, commentData.getText().toString());
                }
            }
        });

        getData();
    }

    public void getData(){
        GetProductData get = new GetProductData();
        get.execute("Data");
    }

    class GetProductData extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            if (type.equals("Data")) {
                String response = "";
                try {
                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/getProductInfo.php?id=" + ID);
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
            } else if (type.equals("Image")) {
                Bitmap img = null;
                try {
                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/retrieveImage.php?id=" + ID);
                    img = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                temp = img;
                return "Image Retrieved";
            }else if(type.equals("Com")){
                try {
                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/leaveComment.php");
                    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                    httpCon.setRequestMethod("POST");
                    httpCon.setDoOutput(true);
                    httpCon.setDoInput(true);
                    OutputStream outStr = httpCon.getOutputStream();
                    BufferedWriter buffW = new BufferedWriter(new OutputStreamWriter(outStr, "UTF-8"));
                    String req = URLEncoder.encode("id","UTF-8") + "=" +URLEncoder.encode(strings[1], "UTF-8")
                            +"&" +URLEncoder.encode("com","UTF-8") + "=" +URLEncoder.encode(strings[2], "UTF-8");
                    buffW.write(req);
                    buffW.flush();
                    buffW.close();
                    outStr.close();

                    InputStream inStr = httpCon.getInputStream();
                    BufferedReader buffR = new BufferedReader(new InputStreamReader(inStr, "iso-8859-1"));
                    String result = "";
                    String line = "";
                    while((line = buffR.readLine()) != null)
                    {
                        result += line;
                    }
                    buffR.close();
                    inStr.close();
                    httpCon.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Comment Sent";
            }else{
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            if(s.equals("Data Retrieved")) {
                new GetProductData().execute("Image");
                Name.setText(parsedResp[0]);
                Desc.setText(parsedResp[1]);
                Price.setText("$" + parsedResp[2]);
                Comment.setText(parsedResp[3]);
                SellerUser.setText(parsedResp[4]);
            }else if(s.equals("Comment Sent")){
                finish();
                startActivity(getIntent());
            }else if(s.equals("Image Retrieved")){
                ProductImg.setImageBitmap(temp);
                contactSeller.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ViewProductActivity.this, ViewMessagesActivity.class);
                        intent.putExtra("Sender", SellerUser.getText().toString());
                        intent.putExtra("Title", "Inquiry about " +Name.getText().toString());
                        intent.putExtra("Body", "Ask any questions about the item, or discuss a transaction!");
                        startActivity(intent);
                    }
                });
                reviewSeller.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ViewProductActivity.this, ReviewsActivity.class);
                        startActivity(intent);
                    }
                });

            }else {
                super.onPostExecute(s);
            }
        }
    }
}
