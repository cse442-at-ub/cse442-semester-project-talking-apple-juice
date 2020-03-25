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
    String un;

    BackgroundActivity(Context c)
    {
        con = c;
    }

    @Override
    protected String doInBackground(String... params) {
        String loginScript = "https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/login.php";
        String signupScript = "https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/signup.php";
        String profileScript = "https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/profilePicture.php";
        String check = params[0];
        if(check.equals("login"))
        {
            try { //PARAMS[2] IS EMAIL, PARAMS[1] IS PASS
                String email = params[2];
                un = email;
                String pass = params[1];
                URL url = new URL(loginScript);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStr = httpCon.getOutputStream();
                BufferedWriter buffW = new BufferedWriter(new OutputStreamWriter(outStr, "UTF-8"));
                String req = URLEncoder.encode("un","UTF-8") + "=" +URLEncoder.encode(email, "UTF-8")
                        +"&" +URLEncoder.encode("pw","UTF-8") + "=" +URLEncoder.encode(pass, "UTF-8");
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
        }else if(check.equals("signup")){
            try {
                String email = params[1];
                String pass = params[2];
                String first  =  params[3];
                String last = params[4];
                URL url = new URL(signupScript);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStr = httpCon.getOutputStream();
                BufferedWriter buffW = new BufferedWriter(new OutputStreamWriter(outStr, "UTF-8"));
                String req = URLEncoder.encode("un","UTF-8") + "=" +URLEncoder.encode(email, "UTF-8")
                        +"&"+  URLEncoder.encode("pw","UTF-8") + "=" +URLEncoder.encode(pass, "UTF-8")
                        +"&"+  URLEncoder.encode("fn","UTF-8") + "=" +URLEncoder.encode(first, "UTF-8")
                        +"&"+  URLEncoder.encode("ln","UTF-8") + "=" +URLEncoder.encode(last, "UTF-8");
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
        }else if(check.equals("profile"))
        {
            try { //PARAMS[1] IS USERNAME, PARAMS[2] IS ENCODED IMAGE
                URL url = new URL(profileScript);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStr = httpCon.getOutputStream();
                BufferedWriter buffW = new BufferedWriter(new OutputStreamWriter(outStr, "UTF-8"));
                String req = URLEncoder.encode("un","UTF-8") + "=" +URLEncoder.encode(params[1], "UTF-8")
                        +"&" +URLEncoder.encode("pic","UTF-8") + "=" +URLEncoder.encode(params[2], "UTF-8");
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
            MainActivity.storedUsername = un;
            Intent intent = new Intent(con, HomeScreen.class);
            con.startActivity(intent);
        }else if(aStr.equals("Registration Successful")) {
            Intent intent = new Intent(con, MainActivity.class);
            con.startActivity(intent);
        }else if(aStr.equals("Profile Picture Uploaded Successfully")){
            Intent intent = new Intent(con, ProfileActivity.class);
            con.startActivity(intent);
        }else{
            Toast incorrect = Toast.makeText(con, "ERROR!!! Please Try Again", Toast.LENGTH_SHORT);
            incorrect.show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
