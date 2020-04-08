package com.example.theplug;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Filter;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    public SearchView searchProd;

    public RecyclerView searchRec;
    static HomeScreenAdapter mAdapter;

    public ArrayList productList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

         init();

         searchProdInfo prodList = new searchProdInfo();
         prodList.execute("prodList");




         searchProd.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String query) {
                 return false;
             }

             @Override
             public boolean onQueryTextChange(String newText) {
                 mAdapter.getFilter().filter(newText);
                 return false;
             }
         });
    }

    public void init(){

        searchProd = findViewById(R.id.prodSearch);
        searchRec = findViewById(R.id.searchResult);
        productList = new ArrayList();

    }


    class searchProdInfo extends AsyncTask<String, Void, String> {

        String[] parsedResp;

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            if (type.equals("prodList")) {
                String response = "";

                try {
                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/getQueryProd.php");

                    HttpURLConnection httpCon;
                    httpCon = (HttpURLConnection) url.openConnection();
                    httpCon.setRequestMethod("GET");

                    InputStream inStr = httpCon.getInputStream();
                    BufferedReader buffr = new BufferedReader(new InputStreamReader(inStr, "iso-8859-1"));
                    String line = "";
                    while ((line = buffr.readLine()) != null) {
                        response += line;
                    }
                    buffr.close();
                    inStr.close();
                    httpCon.disconnect();
                    parsedResp = response.split("\\*");

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Products Recieved";
            }
            return "error";
        }


        @Override
        protected  void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("Products Recieved")) {
                    for (String product : parsedResp) {
                        String[] prod = product.split("\\|");
                            String prodName = prod[1];
                            productList.add(prodName);

                    }

                    searchRec.setHasFixedSize(true);
                    searchRec.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                    mAdapter = new HomeScreenAdapter(productList);
                    searchRec.setAdapter(mAdapter);

                }

                }


        }



}
