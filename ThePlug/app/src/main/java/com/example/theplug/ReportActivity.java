package com.example.theplug;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.net.URLEncoder;

public class ReportActivity extends AppCompatActivity {

    public Button reason1, reason2, reason3;
    public String currentUser, toUser;
    public String reason;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reportuser);

        init();

        reason1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reason = reason1.getText().toString();
                new BackgroundReportHelper().execute("report", currentUser, toUser, reason);
            }
        });

        reason2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reason = reason2.getText().toString();
                new BackgroundReportHelper().execute("report", currentUser, toUser, reason);
            }
        });

        reason3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reason = reason3.getText().toString();
                new BackgroundReportHelper().execute("report", currentUser, toUser,reason);
            }
        });
    }

    public void init(){

      reason1 = findViewById(R.id.reportReason1);
      reason2 = findViewById(R.id.reportReason2);
      reason3 = findViewById(R.id.reportReason3);
      currentUser = MainActivity.storedUsername;
      toUser = ViewProductActivity.sellUSER;
    }

    class BackgroundReportHelper extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];

            if(type.equals("report")){
                try{
                    String currentUser = strings[1];
                    String toUser = strings[2];
                    String reportReason = strings[3];
                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/sendReportSEC.php");

                    HttpURLConnection httpCon;
                    httpCon = (HttpURLConnection) url.openConnection();
                    httpCon.setRequestMethod("POST");
                    httpCon.setDoOutput(true);
                    httpCon.setDoInput(true);
                    OutputStream outStr = httpCon.getOutputStream();
                    BufferedWriter buffW = new BufferedWriter(new OutputStreamWriter(outStr,"UTF-8"));
                    String req = URLEncoder.encode("frm", "UTF-8") + "=" + URLEncoder.encode(currentUser, "UTF-8")
                            + "&" + URLEncoder.encode("to", "UTF-8") + "=" + URLEncoder.encode(toUser, "UTF-8")
                            + "&" + URLEncoder.encode("rep", "UTF-8") + "=" + URLEncoder.encode(reportReason, "UTF-8");

                    buffW.write(req);
                    buffW.flush();
                    buffW.close();
                    outStr.close();

                    InputStream inStr = httpCon.getInputStream();
                    BufferedReader buffR = new BufferedReader(new InputStreamReader(inStr, "iso-8859-1"));
                    String result = "";
                    String line = "";
                    while((line = buffR.readLine()) != null){
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
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            if(s.equals("Report Submitted")){
                Toast.makeText(getApplicationContext(), "User has be reported", Toast.LENGTH_SHORT).show();
            }

        }
    }
}