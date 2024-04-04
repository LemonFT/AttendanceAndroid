package com.example.certainlyhereiamfinal.store;

import android.content.Context;

public class DataLocalManager {
    private static final String TOKENS = "TOKENS";
    private static final String USER = "USER";
    private static final String USERID = "USERID";
    private static final String CLASSID = "CLASSID";
    private static final String CLASSNAME = "CLASSNAME";
    private static DataLocalManager instance;
    private MySharedPreferences mySharedPreferences;
    public static void init(Context context){
        instance = new DataLocalManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    public static DataLocalManager getInstance(){
        if(instance == null){
            instance = new DataLocalManager();
        }
        return instance;
    }

    public static void setTokens(String tokens){
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(TOKENS, tokens);
    }

    public static String getTokens(){
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(TOKENS);
    }

    public static void setUser(String user){
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(USER, user);
    }

    public static String getUser(){
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(USER);
    }

    public static void setUserId(Long userId){
        DataLocalManager.getInstance().mySharedPreferences.putLongValue(USERID, userId);
    }

    public static Long getUserId(){
        return DataLocalManager.getInstance().mySharedPreferences.getLongValue(USERID);
    }

    public static void setClassId(Long userId){
        DataLocalManager.getInstance().mySharedPreferences.putLongValue(CLASSID, userId);
    }

    public static Long getClassId(){
        return DataLocalManager.getInstance().mySharedPreferences.getLongValue(CLASSID);
    }

    public static void setClassname(String classname){
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(CLASSNAME, classname);
    }

    public static String getClassname(){
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(CLASSNAME);
    }

}
