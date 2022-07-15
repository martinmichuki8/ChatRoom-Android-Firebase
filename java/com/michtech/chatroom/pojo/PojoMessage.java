package com.michtech.chatroom.pojo;

public class PojoMessage {

    private String message;
    private int type;
    private String UserName;

    public PojoMessage(String message, String UserName, int type){
        this.message = message;
        this.UserName = UserName;
        this.type = type;
    }
    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getUserName(){
        return UserName;
    }
    public void setUserName(String userName) {
        this.UserName = userName;
    }
    public int getType(){
        return type;
    }
    public void setType(int type){
        this.type = type;
    }
}
