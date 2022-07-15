package com.michtech.chatroom;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.michtech.chatroom.adapter.MessageAdapter;
import com.michtech.chatroom.background.Messages;
import com.michtech.chatroom.database.DatabaseManager;
import com.michtech.chatroom.database.DbHelper;
import com.michtech.chatroom.pojo.PojoMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Chat extends AppCompatActivity {

    ListView listView;
    ImageButton Send;
    TextView Message;

    List<PojoMessage> list;
    MessageAdapter messageAdapter;

    List<DbHelper> dbHelperList;
    DatabaseManager db;

    Firebase ref;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        db = new DatabaseManager(Chat.this);

        Firebase.setAndroidContext(Chat.this);
        ref=new Firebase("https://chat-room-39f79-default-rtdb.firebaseio.com/");

        listView = findViewById(R.id.listView);
        Send = findViewById(R.id.send);
        Message = findViewById(R.id.message);

        getMessages();


        /* databaseReference = FirebaseDatabase.getInstance().getReference().child("ChatRoom").child("Chat").child("Chat Room");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String message = snapshot.child("Message").getValue(String.class);
                    String from = snapshot.child("From").getValue(String.class);

                    if(!from.equals(db.GetUserName())){
                        db.saveMessage(from, message, 1, getTime());
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                getMessages();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        */

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message;
                if(!Message.getText().toString().isEmpty()){
                    message = Message.getText().toString();

                    db.saveMessage(db.GetUserName(), message, 2, getTime());
                    saveOnline(message, db.GetUserName(), getTime());
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            getMessages();
                        }
                    });

                    Message.setText("");
                }
            }
        });
    }
    public void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(Chat.this).registerReceiver(messageReceiver, new IntentFilter("New"));
    }
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getMessages();
        }
    };
    private void getMessages(){
        listShow();

        messageAdapter = new MessageAdapter(Chat.this, list);
        listView.setAdapter(messageAdapter);
    }
    private void listShow(){
        dbHelperList = new ArrayList<>();
        dbHelperList = db.getMessage();
        list = new ArrayList<>();

        for(DbHelper helper: dbHelperList){
            list.add(new PojoMessage(helper.getMessage(), helper.getFFrom(), helper.getType()));
        }
    }
    private String getTime(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentDateTime = sdf.format(new Date());
        return currentDateTime;
    }
    private void saveOnline(String message, String from, String date){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Messages messages = new Messages(message, from, date);
        databaseReference.child("ChatRoom").child("Chat").child("Chat Room").setValue(messages);
    }
}
