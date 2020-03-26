package com.example.theplug;

import android.app.Activity;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class NewProductActivity extends AsyncTask<String, Void, String> {

    Context con;
    Activity a;

    NewProductActivity(Activity ac)
    {
        a = ac;
        con = a.getApplicationContext();
    }

    @Override
    protected String doInBackground(String... params) {
        String deleteScript = "https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/deleteProduct.php";
        String uploadScript = "https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/uploadProdInfo.php";
        String check = params[0];
        if(check.equals("upload"))
        {
            try {
                String name = params[1];
                String type = params[2];
                String price = params[3];
                String desc = params[4];
                String id = params[5];
                String selltype = params[6];
                String encImg = params[7];
                URL url = new URL(uploadScript);
                HttpURLConnection httpCon;
                httpCon = (HttpURLConnection) url.openConnection();
                httpCon.setRequestMethod("POST");
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                OutputStream outStr = httpCon.getOutputStream();
                BufferedWriter buffW = new BufferedWriter(new OutputStreamWriter(outStr, "UTF-8"));
                String req = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8")
                        + "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")
                        + "&" + URLEncoder.encode("price", "UTF-8") + "=" + URLEncoder.encode(price, "UTF-8")
                        + "&" + URLEncoder.encode("desc", "UTF-8") + "=" + URLEncoder.encode(desc, "UTF-8")
                        + "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8")
                        + "&" + URLEncoder.encode("sb", "UTF-8") + "=" + URLEncoder.encode(selltype, "UTF-8")
                        + "&" + URLEncoder.encode("ei", "UTF-8") + "=" + URLEncoder.encode(encImg, "UTF-8")
                        + "&" + URLEncoder.encode("co", "UTF-8") + "=" + URLEncoder.encode("No comments yet.", "UTF-8");
                buffW.write(req);
                buffW.flush();
                buffW.close();
                outStr.close();

                InputStream inStr = httpCon.getInputStream();
                BufferedReader buffR = new BufferedReader(new InputStreamReader(inStr, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = buffR.readLine()) != null) {
                    result += line;
                }
                buffR.close();
                inStr.close();
                httpCon.disconnect();
                return result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (check.equals("delete"))
            {
                try {
                    String name = params[1];
                    URL url = new URL(deleteScript);
                    HttpURLConnection httpCon;
                    httpCon = (HttpURLConnection) url.openConnection();
                    httpCon.setRequestMethod("POST");
                    httpCon.setDoOutput(true);
                    httpCon.setDoInput(true);
                    OutputStream outStr = httpCon.getOutputStream();
                    BufferedWriter buffW = new BufferedWriter(new OutputStreamWriter(outStr, "UTF-8"));
                    String req = URLEncoder.encode("name","UTF-8") + "=" +URLEncoder.encode(name, "UTF-8");
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
        if(aStr.equals("Product Upload Successful")) {
            Toast success = Toast.makeText(con, "Item uploaded successfully.", Toast.LENGTH_SHORT);
            success.show();
            a.finish();
        }else if(aStr.equals("Product Deletion Successful")) {
            Toast success = Toast.makeText(con, "Deleted successfully.", Toast.LENGTH_SHORT);
            success.show();
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
