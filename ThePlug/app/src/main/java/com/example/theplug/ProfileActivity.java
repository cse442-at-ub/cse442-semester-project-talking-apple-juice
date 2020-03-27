package com.example.theplug;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.*;

import android.os.Bundle;

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

public class ProfileActivity extends AppCompatActivity {
    private EditText oldPass;
    private EditText newPass1;
    private EditText newPass2;
    private EditText curEmail;
    private EditText newEmail;
    private EditText verEmailF;
    private TextView yourUser;

    public Bitmap temp;
    public ImageView profilePic;

    public String storedPass;
    public String storedUser = MainActivity.storedUsername;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            setTheme(R.style.lightTheme);
        } else {
            setTheme(R.style.darkTheme);
        }
        setContentView(R.layout.activity_profile);

        Button confirmPassChange = findViewById(R.id.confirmPassButton);
        confirmPassChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePass();
            }
        });

        Button confirmEmailChange = findViewById(R.id.confirmEmailButton);
        confirmEmailChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmail();
            }
        });

        curEmail = (EditText) findViewById(R.id.currentEmail);
        newEmail = (EditText) findViewById(R.id.changeEmail);
        verEmailF = (EditText) findViewById(R.id.confirmEmail);
        oldPass = (EditText) findViewById(R.id.currentPass);
        newPass1 = (EditText) findViewById(R.id.newPass);
        newPass2 = (EditText) findViewById(R.id.newPassVerify);

        profilePic = findViewById(R.id.yourProfilePic);
        yourUser = findViewById(R.id.usernameView);
        yourUser.setText(MainActivity.storedUsername);

        GetProfileData gp = new GetProfileData();
        gp.execute("image", MainActivity.storedUsername);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateEmail() {
        String oldE = curEmail.getText().toString();
        String newE = newEmail.getText().toString();
        String verE = verEmailF.getText().toString();
        if (oldE.equals("") || newE.equals("") || verE.equals("")) {
            Toast err = Toast.makeText(getApplicationContext(), "Please fill out all boxes.", Toast.LENGTH_SHORT);
            err.show();
        } else if (!(oldE.equals(storedUser))) {
            Toast err = Toast.makeText(getApplicationContext(), "Incorrect current email/user.", Toast.LENGTH_SHORT);
            err.show();
        } else if (!(newE.equals(verE))) {
            Toast err = Toast.makeText(getApplicationContext(), "New email/users don't match.", Toast.LENGTH_SHORT);
            err.show();
        } else {
            GetProfileData gp = new GetProfileData();
            gp.execute("updateUN", newE);
            curEmail.getText().clear();
            newEmail.getText().clear();
            verEmailF.getText().clear();
            yourUser.setText(newE);
        }
    }

    private void updatePass() {
        String oldP = oldPass.getText().toString();
        String newP = newPass1.getText().toString();
        String verP = newPass2.getText().toString();

        if (oldP.equals("") || newP.equals("") || verP.equals("")) {
            Toast err = Toast.makeText(getApplicationContext(), "Please fill out all boxes.", Toast.LENGTH_SHORT);
            err.show();
        } else if (!(oldP.equals(storedPass))) {
            Toast err = Toast.makeText(getApplicationContext(), "Incorrect current password.", Toast.LENGTH_SHORT);
            err.show();
        } else if (!(newP.equals(verP))) {
            Toast err = Toast.makeText(getApplicationContext(), "New passwords don't match.", Toast.LENGTH_SHORT);
            err.show();
        } else {
            GetProfileData gp = new GetProfileData();
            gp.execute("updatePW", newP);
            oldPass.getText().clear();
            newPass1.getText().clear();
            newPass2.getText().clear();
        }
    }

    public void goToPicChange(View view) {
        Intent intent = new Intent(this, ProfilePicChangeActivity.class);
        startActivity(intent);
    }


    class GetProfileData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("Image Retrieved")) {
                profilePic.setImageBitmap(temp);
                new GetProfileData().execute("data", MainActivity.storedUsername);
            } else if (s.equals("Pass Updated Successfully")) {
                Toast suc = Toast.makeText(getApplicationContext(), "Password updated successfully.", Toast.LENGTH_SHORT);
                suc.show();
            } else if (s.equals("Email Updated Successfully")){
                Toast suc = Toast.makeText(getApplicationContext(), "User updated successfully.", Toast.LENGTH_SHORT);
                suc.show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            if (type.equals("image")) {
                String user = strings[1];
                String imgScript = "https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/retrievePFP.php?un=" + user;
                Bitmap img = null;
                try {
                    URL url = new URL(imgScript);
                    img = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                temp = img;
                return "Image Retrieved";
            } else if (type.equals("data")) {
                String user = strings[1];
                String imgScript = "https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/getYourPassword.php?un=" + user;
                Bitmap img = null;
                try {
                    URL url = new URL(imgScript);
                    HttpURLConnection httpCon;
                    httpCon = (HttpURLConnection) url.openConnection();
                    httpCon.setRequestMethod("GET");

                    InputStream inStr = httpCon.getInputStream();
                    BufferedReader buffR = new BufferedReader(new InputStreamReader(inStr, "iso-8859-1"));
                    String line = "";
                    String response = "";
                    while ((line = buffR.readLine()) != null) {
                        response += line;
                    }
                    buffR.close();
                    inStr.close();
                    httpCon.disconnect();
                    storedPass = response;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Data Retrieved";
            } else if (type.equals("updatePW")) {
                try {
                    String newPW = strings[1];
                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/changePassword.php");
                    HttpURLConnection httpCon;
                    httpCon = (HttpURLConnection) url.openConnection();
                    httpCon.setRequestMethod("POST");
                    httpCon.setDoOutput(true);
                    httpCon.setDoInput(true);
                    OutputStream outStr = httpCon.getOutputStream();
                    BufferedWriter buffW = new BufferedWriter(new OutputStreamWriter(outStr, "UTF-8"));
                    String req = URLEncoder.encode("un", "UTF-8") + "=" + URLEncoder.encode(storedUser, "UTF-8")
                            + "&" + URLEncoder.encode("newpw", "UTF-8") + "=" + URLEncoder.encode(newPW, "UTF-8");
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
                    storedPass = newPW;
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
                return "caught err";
            } else if (type.equals("updateUN")) {
                try {
                    String newUN = strings[1];
                    URL url = new URL("https://www-student.cse.buffalo.edu/CSE442-542/2020-spring/cse-442ac/changeUsername.php");
                    HttpURLConnection httpCon;
                    httpCon = (HttpURLConnection) url.openConnection();
                    httpCon.setRequestMethod("POST");
                    httpCon.setDoOutput(true);
                    httpCon.setDoInput(true);
                    OutputStream outStr = httpCon.getOutputStream();
                    BufferedWriter buffW = new BufferedWriter(new OutputStreamWriter(outStr, "UTF-8"));
                    String req = URLEncoder.encode("un", "UTF-8") + "=" + URLEncoder.encode(storedUser, "UTF-8")
                            + "&" + URLEncoder.encode("newun", "UTF-8") + "=" + URLEncoder.encode(newUN, "UTF-8");
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
                    MainActivity.storedUsername = newUN;
                    storedUser = newUN;

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
                return "caught err";
            } else {
                return "error";
            }
        }
    }
}

