package com.michtech.chatroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.michtech.chatroom.database.DatabaseManager;
import com.michtech.chatroom.sv.BroadCast;
import com.michtech.chatroom.sv.ServiceMessages;

public class MainActivity extends AppCompatActivity {

    DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseManager(MainActivity.this);

        Intent intent = new Intent(MainActivity.this, ServiceMessages.class);
        startService(intent);
        startAlert();
    }
    public void onStart(){
        super.onStart();
        CountDownTimer timer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) {
                //
            }

            @Override
            public void onFinish() {
                db.CheckTables();

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                Intent intent = null;
                if(db.CheckUser()){
                    intent = new Intent(MainActivity.this, Chat.class);
                }else{
                    intent = new Intent(MainActivity.this, CreateAccount.class);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        }.start();
    }
    public void startAlert() {
        int i = 5;
        Intent intent = new Intent(MainActivity.this, BroadCast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(),
                1000 * 5, pendingIntent);
    }
}