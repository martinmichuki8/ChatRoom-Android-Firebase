package com.michtech.chatroom.database;

public class DbHelper {
    private int Id;
    private String Message;
    private String FFrom;
    private int Type;
    private String DDate;

    public DbHelper(){}

    public DbHelper(int Id, String Message, String FFrom, int Type, String DDate){
        this.Id = Id;
        this.Message = Message;
        this.FFrom = FFrom;
        this.Type = Type;
        this.DDate = DDate;
    }
    public int getId(){
        return Id;
    }
    public void setId(int Id){
        this.Id = Id;
    }
    public String getMessage(){
        return Message;
    }
    public void setMessage(String Message){
        this.Message = Message;
    }
    public String getFFrom(){
        return FFrom;
    }
    public void setFFrom(String FFrom){
        this.FFrom = FFrom;
    }
    public int getType(){
        return Type;
    }
    public void setType(int Type){
        this.Type = Type;
    }
    public String getDDate(){
        return DDate;
    }
    public void setDDate(String DDate){
        this.DDate = DDate;
    }
}
