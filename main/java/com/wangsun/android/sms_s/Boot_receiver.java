package com.wangsun.android.sms_s;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by WANGSUN on 24-Dec-16.
 */

public class Boot_receiver extends BroadcastReceiver {
    String channel_id="sms_check";

    SQLiteDatabase db;
    Time_dbHelper helperDb;
    Cursor cursor;

    Context context;

    String title="Failed to send message.";
    String summary="";

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context=context;

        //sp = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);

        helperDb=new Time_dbHelper(context);
        db=helperDb.getReadableDatabase();
        cursor=helperDb.getInformation(db);

        if(cursor.moveToFirst()){
            do{
                String msg,status;
                int id;
                String num;
                long date;

                id=cursor.getInt(0);
                msg=cursor.getString(1);
                num=cursor.getString(2);
                date=cursor.getLong(3);
                status=cursor.getString(4);

                if(status.equals("ON")){
                    check(id,msg,num,date);
                }

            }while(cursor.moveToNext());
        }

        helperDb.close();
        db.close();

    }


    public void check(int id,String msg,String num,long date){

        Date date_stored=new Date(date);

        Calendar currentC = Calendar.getInstance();
        Date date_now = currentC.getTime();  //current date

        if(date_stored.before(date_now)){
            notifyFailure(id,msg);
        }
        else {
            sendMessage(id,msg,num,date);
        }
    }


    public void sendMessage(int id,String msg,String num,long date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        Intent intentBoot = new Intent(context, Alarm_broadcast.class);
        intentBoot.putExtra("id",id);
        intentBoot.putExtra("msg",msg);
        intentBoot.putExtra("num",num);

        intentBoot.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                PendingIntent.getBroadcast(context, id, intentBoot, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    public void notifyFailure(int id,String msg){

        if(msg.length()>25)
            msg=msg.substring(0,25)+"...";

        //"Service Destroyed."
        NotificationCompat.BigTextStyle noti_message=new NotificationCompat.BigTextStyle();
        noti_message.bigText(msg);
        noti_message.setBigContentTitle(title);
        noti_message.setSummaryText(summary);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channel_id)
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setTicker("BEC SUPPORT")
                .setContentTitle(title)
                .setContentText(msg)
                .setStyle(noti_message)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setDefaults(Notification.DEFAULT_ALL);
        Intent notificationIntent = new Intent(context, Main.class);
        //notificationIntent.putExtra("noti",message);
        PendingIntent contentIntent = PendingIntent.getActivity(context, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(id, builder.build());

        //helperDb=new Time_dbHelper(context);
        //db=helperDb.getWritableDatabase();
        helperDb.updateStatus(id,"OFF",db);
        //helperDb.close();
        //db.close();

    }
}