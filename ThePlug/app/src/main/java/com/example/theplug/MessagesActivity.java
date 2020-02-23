package com.example.theplug;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MessagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem page){
        if(page.getItemId() == R.id.accountButton)
        {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        if(page.getItemId() == R.id.messageButton)
        {
            Intent intent = new Intent(this, MessagesActivity.class);
            startActivity(intent);
            return true;
        }
        if(page.getItemId() == R.id.transaction_historyButton)
        {
            Intent intent = new Intent(this, TransactionsActivity.class);
            startActivity(intent);
            return true;
        }
        if(page.getItemId() == R.id.settingsButton)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
