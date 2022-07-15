package com.michtech.chatroom.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.michtech.chatroom.R;
import com.michtech.chatroom.database.DatabaseManager;
import com.michtech.chatroom.pojo.PojoMessage;

import java.util.List;

public class MessageAdapter extends BaseAdapter {

    Context context;
    List<PojoMessage> list;

    public MessageAdapter(Context context, List<PojoMessage> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.message, viewGroup, false);

        DatabaseManager db = new DatabaseManager(context);

        TextView Message = view.findViewById(R.id.textView);
        TextView FromUserName = view.findViewById(R.id.userName);

        ConstraintLayout From = view.findViewById(R.id.from);

        Message.setText(list.get(position).getMessage());
        if(list.get(position).getUserName().equals(db.GetUserName())){
            FromUserName.setText("");
        }else{
            FromUserName.setText(list.get(position).getUserName());
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)From.getLayoutParams();

        switch(list.get(position).getType()){
            case 1:
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                From.setLayoutParams(params);
                if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
                    From.setBackgroundResource(R.drawable.message_bg2);
                }else{
                    From.setBackgroundResource(R.drawable.message_background2);
                }
                break;
            case 2:
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                From.setLayoutParams(params);
                if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
                    From.setBackgroundResource(R.drawable.message_bg);
                }else{
                    From.setBackgroundResource(R.drawable.message_background);
                }
                break;
        }

        return view;
    }
}
