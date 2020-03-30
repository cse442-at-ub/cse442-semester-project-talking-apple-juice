package com.example.theplug;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

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

import static com.example.theplug.MainActivity.storedUsername;


public class SettingsActivity extends AppCompatActivity {

    public Button editButton, deleteButton;

    public EditText inputProdName;  // What the user inputs. The product name
    public String prodUser;  //the user of that product
    public String current = storedUsername;   //the current loggedin user

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
        {
            setTheme(R.style.lightTheme);
        }else{
            setTheme(R.style.darkTheme);
        }
        setContentView(R.layout.activity_settings);

        Switch themeSwitch = findViewById(R.id.themeFlip);

        themeSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

                finish();
                startActivity(new Intent(SettingsActivity.this, SettingsActivity.this.getClass()));
            }

        });

        init();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 prodDeleter();
            }
        });
    }

    public void init(){

        inputProdName = (EditText) findViewById(R.id.nameProd);
        editButton = (Button) findViewById(R.id.editProd);
        deleteButton = (Button) findViewById(R.id.deleteProd);

    }

    public void prodDeleter(){
        String pname  = inputProdName.getText().toString();
        getUsername test = new getUsername();
        test.execute("Username", pname);
    }



    class getUsername extends AsyncTask<String, Void, String>{

        String name = inputProdName.getText().toString();
        String prodUser = "";

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            String prod = strings[1];
            if(type.equals("Username")){
                try{
                    String response = "";
                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/getUsername.php?name=" + prod);
                    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                    httpCon.setRequestMethod("GET");

                    InputStream inStr = httpCon.getInputStream();
                    BufferedReader buffR = new BufferedReader(new InputStreamReader(inStr,"iso-8859-1"));
                    String line = "";
                    while((line = buffR.readLine()) != null)
                    {
                        response += line;
                    }
                    buffR.close();
                    inStr.close();
                    httpCon.disconnect();

                    prodUser = response;
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Username Recieved";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            if(s.equals("Username Recieved")){
                if(prodUser.equals(MainActivity.storedUsername)) {
                    NewProductActivity npa = new NewProductActivity(SettingsActivity.this);
                    npa.execute("delete", name);
                    finish();
                    startActivity(getIntent());
                }else{
                    Toast incorrect = Toast.makeText(getApplicationContext(), "Not your product!", Toast.LENGTH_SHORT);
                    incorrect.show();
                }
            }else{
                Toast incorrect = Toast.makeText(getApplicationContext(), "Invalid Product", Toast.LENGTH_SHORT);
                incorrect.show();
            }
            super.onPostExecute(s);
        }
    }






}