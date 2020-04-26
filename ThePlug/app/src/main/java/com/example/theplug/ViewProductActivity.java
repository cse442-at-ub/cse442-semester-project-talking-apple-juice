package com.example.theplug;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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
    public TextView Name , Desc, Price, Comment, SellerUser;
    public String ID = "";
    public String selltype = "";
    public String[] parsedResp;
    public Bitmap temp = null;

    public EditText commentData, bidPrice;
    public Button addComment, contactSeller, status, placeBid, watch;


    public static String sellUSER;
    public static String watchChecker; //check if already in Watch database
    public static String watchSeller; //check to make sure current user is not same user
    public static String watcher;

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

        init();

        new GetProductData().execute("WatchList");

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

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(watcher.equals("nothing found")){
                        if(!MainActivity.storedUsername.equals(sellUSER)){
                            watchLIST();
                        }else {
                            Toast incorrect = Toast.makeText(getApplicationContext(), "You can not watch your own item!", Toast.LENGTH_SHORT);
                            incorrect.show();
                        }

                    }
                    if(MainActivity.storedUsername.equals(watchSeller)){
                        Toast incorrect = Toast.makeText(getApplicationContext(), "You can not watch your own item!", Toast.LENGTH_SHORT);
                        incorrect.show();
                    }else{
                        if(watchChecker.equals("W") && watcher.equals(MainActivity.storedUsername)){
                            Toast incorrect = Toast.makeText(getApplicationContext(), "You are already watching this item", Toast.LENGTH_SHORT);
                            incorrect.show();
                        }else if(watchChecker.equals("W") || watcher.equals(MainActivity.storedUsername)){
                            Toast incorrect = Toast.makeText(getApplicationContext(), "You can not watch this item", Toast.LENGTH_SHORT);
                            incorrect.show();
                        }else{
                            watchLIST();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }




            }
        });


        status.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(!status.getText().toString().equals("Sold")){
                    if(sellUSER.equals(MainActivity.storedUsername)){
                        status.setText("Sold");
                        new GetProductData().execute("Status", ID, status.getText().toString());
                        soldProd();
                        //statusTV.setText("Sold");
                        Toast good = Toast.makeText(getApplicationContext(), "Item has been Sold", Toast.LENGTH_SHORT);
                        good.show();
                    }else{
                        Toast good = Toast.makeText(getApplicationContext(), "You are not the Origial Owner", Toast.LENGTH_SHORT);
                        good.show();
                    }
                }else{ //status is "Active"
                    if(sellUSER.equals(MainActivity.storedUsername)){
                        status.setText("Active");
                        new GetProductData().execute("Status", ID, status.getText().toString());
                        deleteSoldProd();
                       // statusTV.setText("Available");
                        Toast good = Toast.makeText(getApplicationContext(), "Item is Available", Toast.LENGTH_SHORT);
                        good.show();
                    }else{
                        Toast good = Toast.makeText(getApplicationContext(), "You are not the Original Owner", Toast.LENGTH_SHORT);
                        good.show();
                    }
                }
            }
        });

        placeBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bidPrice.getText().toString().equals(""))
                {
                    Toast bad = Toast.makeText(getApplicationContext(), "Please enter an amount up to 5 dollars greater than the current price.", Toast.LENGTH_SHORT);
                    bad.show();
                }else if(Integer.parseInt(bidPrice.getText().toString()) > Integer.parseInt(Price.getText().toString().substring(1)) + 5)
                {
                    Toast bad = Toast.makeText(getApplicationContext(), "Too high! Please enter an amount up to 5 dollars greater than the current price.", Toast.LENGTH_SHORT);
                    bad.show();
                }else if(Integer.parseInt(bidPrice.getText().toString()) <= Integer.parseInt(Price.getText().toString().substring(1)))
                {
                    Toast bad = Toast.makeText(getApplicationContext(), "Too low! Please enter an amount up to 5 dollars greater than the current price.", Toast.LENGTH_SHORT);
                    bad.show();
                } else if(MainActivity.storedUsername.equals(sellUSER)) {
                    Toast bad = Toast.makeText(getApplicationContext(), "Can't bid on your own product!", Toast.LENGTH_SHORT);
                    bad.show();
                }else{
                    new GetProductData().execute("PlaceBid", ID, MainActivity.storedUsername, bidPrice.getText().toString());
                    Toast good = Toast.makeText(getApplicationContext(), "Bid sent.", Toast.LENGTH_SHORT);
                    good.show();
                }
            }
        });

        getData();


    }

    public void init(){
        ProductImg = findViewById(R.id.productImg);
        Name = findViewById(R.id.itemNameTextView8);
        Desc = findViewById(R.id.itemDescTextView7);
        Price = findViewById(R.id.itemPriceTextView);
        Comment = findViewById(R.id.itemCommentTextView);
        SellerUser = findViewById(R.id.soldByUser);
        bidPrice = findViewById(R.id.bidEntry);

        commentData = findViewById(R.id.commentBox);
        addComment = findViewById(R.id.addComButton);
        contactSeller = findViewById(R.id.button2);
        placeBid = findViewById(R.id.bidButton);
        status = findViewById(R.id.statusButton);
        watch = findViewById(R.id.watchButton);

    }

    public void watchLIST(){
        String name = Name.getText().toString();
        String price = Price.getText().toString();
        String desc = Desc.getText().toString();
        String user = SellerUser.getText().toString();
        String watcher = MainActivity.storedUsername;
        String id = ID;


        String watchStatus = "W";

        BitmapDrawable imgView = (BitmapDrawable) ProductImg.getDrawable();
        String img= "";
        try{
            Bitmap itemImg = imgView.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            itemImg.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            img = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new TransactionsActivity.productSold().execute("watch", name, price, desc, id, img, user, watcher, watchStatus);

        Toast err = Toast.makeText(ViewProductActivity.this, "Product Added To Watch List", Toast.LENGTH_SHORT);
        err.show();
    }

    public void soldProd(){
        String name  = Name.getText().toString();
        String price = Price.getText().toString();
        String desc = Desc.getText().toString();
        String com = Comment.getText().toString();
        String id = ID;
        String user = SellerUser.getText().toString();

        BitmapDrawable imgView = (BitmapDrawable) ProductImg.getDrawable();
        String img = "";
        try{
            Bitmap itemImg = imgView.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            itemImg.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            img = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new TransactionsActivity.productSold().execute("sold", name, price, desc, id, img, com, user);

    }

    public void deleteSoldProd(){
        String name = Name.getText().toString();
        NewProductActivity npa = new NewProductActivity(ViewProductActivity.this);
        npa.execute("sold", name);

    }

    public void getData(){
        GetProductData get = new GetProductData();
        get.execute("Data");
    }

    class GetProductData extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            if(type.equals("WatchList")){
                String response = "";
                try {
                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/getWatchInfo.php?id=" + ID);
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
                    if (response.equals("nothing found")) {
                        parsedResp = new String[1];
                        parsedResp[0] = "nothing found";
                        watcher = parsedResp[0];
                    } else {
                        parsedResp = response.split("\\|");
                        watchSeller = parsedResp[3];
                        watcher = parsedResp[4];
                        watchChecker = parsedResp[5];
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Watch List Retrieved";
            }
           else if (type.equals("Data")) {
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
            } else if(type.equals("Status")){
                try {
                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/leaveStatus.php");
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
                return "Status Sent";

            }else if(type.equals("PlaceBid")){
                try {
                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/placeBidSEC.php");
                    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                    httpCon.setRequestMethod("POST");
                    httpCon.setDoOutput(true);
                    httpCon.setDoInput(true);
                    OutputStream outStr = httpCon.getOutputStream();
                    BufferedWriter buffW = new BufferedWriter(new OutputStreamWriter(outStr, "UTF-8"));
                    String req = URLEncoder.encode("id","UTF-8") + "=" +URLEncoder.encode(strings[1], "UTF-8")
                            +"&" +URLEncoder.encode("un","UTF-8") + "=" +URLEncoder.encode(strings[2], "UTF-8")
                            +"&" +URLEncoder.encode("bid","UTF-8") + "=" +URLEncoder.encode(strings[3], "UTF-8");
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
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "error";
            }
            else{
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
                try{
                    status.setText(parsedResp[5]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                selltype = parsedResp[6];
                //code to go to user's profile page
                SellerUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ViewProductActivity.this, OtherUserProfileActivity.class);
                        intent.putExtra("Seller", parsedResp[4]);
                        startActivity(intent);
                    }
                });

                if(selltype.equals("1"))
                {
                    bidPrice.setVisibility(View.VISIBLE);
                    placeBid.setVisibility(View.VISIBLE);
                    //show bid shit
                }

                sellUSER = SellerUser.getText().toString();
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


            }else if(s.equals("Status Sent")){
                finish();
                startActivity(getIntent());
            }else if(s.equals("Bid Updated Successfully"))
            {
                finish();
                startActivity(getIntent());
            }
            else {
                super.onPostExecute(s);
            }
        }
    }
}
