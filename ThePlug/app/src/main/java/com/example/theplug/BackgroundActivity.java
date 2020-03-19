package com.example.theplug;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

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
import java.net.URLConnection;
import java.net.URLEncoder;

public class BackgroundActivity extends AsyncTask<String, Void, String> {

    Context con;
    AlertDialog alert;

    BackgroundActivity(Context c)
    {
        con = c;
    }

    @Override
    protected String doInBackground(String... params) {
        if(params[0].equals("login"))
        {
            String loginScript = "https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/login.php";
            try {
                URL url = new URL(loginScript);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStr = httpCon.getOutputStream();
                BufferedWriter buffW = new BufferedWriter(new OutputStreamWriter(outStr, "UTF-8"));
                String req = URLEncoder.encode("un","UTF-8") + "=" +URLEncoder.encode((String)params[2], "UTF-8") //params[2] is email, params[1] is pass
                        +"&" +URLEncoder.encode("pw","UTF-8") + "=" +URLEncoder.encode((String)params[1], "UTF-8");
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
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String aStr) {
        if(aStr.equals("Login Successful")) {
            Intent intent = new Intent(con, HomeScreen.class);
            con.startActivity(intent);
        }else{
            Toast incorrectPass = Toast.makeText(con, "Wrong credentials!", Toast.LENGTH_SHORT);
            incorrectPass.show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
