package com.michtech.chatroom.sv;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class BroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!isServiceRunning(ServiceMessages.class,context)) {
            System.out.println("Alarm...");
            Intent intent1 = new Intent(context, ServiceMessages.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
        /*if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)){
            Intent intent1 = new Intent(context, ServiceMessages.class);
            context.startActivity(intent1);
        }*/
    }
    private boolean isServiceRunning(Class<?> serviceClass, Context context){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceClass.getName().equals(serviceInfo.service.getClassName())){
                return true;
            }
        }
        return false;
    }
}
