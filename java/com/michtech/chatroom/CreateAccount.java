package com.michtech.chatroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.michtech.chatroom.database.DatabaseManager;

public class CreateAccount extends AppCompatActivity {
    TextInputLayout UserName, Email;
    Button Create;
    DatabaseManager db;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        db = new DatabaseManager(CreateAccount.this);

        UserName = findViewById(R.id.UserName);
        Email = findViewById(R.id.Email);
        Create = findViewById(R.id.Create);

        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName, email;
                if(UserName.getEditText().getText().toString().isEmpty() || Email.getEditText().getText().toString().isEmpty()){
                    Snackbar.make(view, "Fill all the fields", Snackbar.LENGTH_LONG).setAction(null, null).show();
                }else{
                    userName = UserName.getEditText().getText().toString();
                    email = Email.getEditText().getText().toString();

                    db.addUser(userName, email);

                    Intent intent = new Intent(CreateAccount.this, Chat.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
