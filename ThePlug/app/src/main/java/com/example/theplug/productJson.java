package com.example.theplug;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.HashMap;

public class productJson extends AppCompatActivity {

    ListView prodList;
    ArrayAdapter<String> adapter;
    String searchScript = "https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/searchProduct.php";
    InputStream is =  null;
    String line = null;
    String result = null;
    String[] data;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //rodList = (ListView) findViewById(R.id.productList);

        getData();

        adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, data);
        prodList.setAdapter(adapter);


    }
    void getData(){
        try{
            URL url = new URL(searchScript);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            is = new BufferedInputStream(con.getInputStream());

        }catch (Exception e){
            e.printStackTrace();
        }
        try{

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while((br.readLine()) != null){

                sb.append(line+"\n");
            }
            is.close();
            result = sb.toString();

        }catch(Exception e){
            e.printStackTrace();

        }

        try {

            JSONArray js = new JSONArray(result);
            JSONObject jo = null;

            data = new String[js.length()];

            for(int i = 0; i <  js.length();  i++){

                jo = js.getJSONObject(i);
                data[i] = jo.getString("Name");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
