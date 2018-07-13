package com.wangsun.android.sms_s;

/**
 * Created by WANGSUN on 30-01-18.
 */

public class Class_item {

    int id;
    String msg,status,num; //since num can have more than 1 number in string form like, 1234567899,789456123
    long date;
    //public static String Time="time";

    public Class_item(int id, String msg, String num, long date, String status) {
        this.id = id;
        this.msg = msg;
        this.num = num;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }

    public String getNum() {
        return num;
    }

    public long getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
