package com.michtech.chatroom.sv;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.michtech.chatroom.Chat;
import com.michtech.chatroom.R;
import com.michtech.chatroom.database.DatabaseManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ServiceMessages extends Service {
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    Firebase ref;
    DatabaseReference databaseReference;

    private int CHANNEL_ID =-1;
    private final int ID = 1;

    Context context;
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
    }
    public void handleMessage(){
        Firebase.setAndroidContext(this);
        ref=new Firebase("https://chat-room-39f79-default-rtdb.firebaseio.com/");

        DatabaseManager db = new DatabaseManager(this);
        context = this;

        databaseReference = FirebaseDatabase.getInstance().getReference().child("ChatRoom").child("Chat").child("Chat Room");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String message = snapshot.child("Message").getValue(String.class);
                    String from = snapshot.child("From").getValue(String.class);

                    if(!from.equals(db.GetUserName())){
                        if(!message.equals(db.getLastMessage())){
                            Intent intent = new Intent("New");
                            intent.putExtra("Message", "msg");
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            db.saveMessage(from, message, 1, getTime());
                            CreateNotification(message,from);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onCreate(){
        HandlerThread thread = new HandlerThread("ServiceStart", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);
        handleMessage();
        return START_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private String getTime(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentDateTime = sdf.format(new Date());
        return currentDateTime;
    }
    private void CreateNotification(String msg , String from){
        CreateNotificationChannel(msg, from);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "ChatRoom");
        builder.setSmallIcon(R.drawable.chat);
        builder.setContentTitle(from);
        builder.setContentText(msg);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, builder.build());
    }
    private void CreateNotificationChannel(String msg, String ffrom){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence from = ffrom;
            String message = msg;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel("ChatRoom", from, importance);
            notificationChannel.setDescription(message);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
