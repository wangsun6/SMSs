package com.wangsun.android.sms_s;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;

/**
 * Created by WANGSUN on 21-Dec-16.
 */

public class Alarm_broadcast extends BroadcastReceiver {
    String channel_id="sms_fire";

    SQLiteDatabase db;
    Time_dbHelper helperDb;

    Context context;

    String msg,phone_no;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        createNotification(context,intent);
    }

    public void createNotification(Context context,Intent intent) {

        String title="SMS sent successfully.";
        String summary="";

        int id=intent.getIntExtra("id",0);
        msg = intent.getStringExtra("msg");
        phone_no = intent.getStringExtra("num");

        try {

            String temp_msg="";

            if(msg.length()>25)
                temp_msg=msg.substring(0,25)+"...";

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone_no, null, msg, null, null);

            NotificationCompat.BigTextStyle noti_message=new NotificationCompat.BigTextStyle();
            noti_message.bigText(temp_msg);
            noti_message.setBigContentTitle(title);
            noti_message.setSummaryText(summary);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channel_id)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(title)
                    .setContentTitle(title)
                    .setContentText(temp_msg)
                    .setAutoCancel(true)
                    .setStyle(noti_message)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setDefaults(Notification.DEFAULT_LIGHTS)
                    .setDefaults(Notification.DEFAULT_ALL);
            Intent notificationIntent = new Intent(context, Main.class);
            //notificationIntent.putExtra("noti",message);
            notificationIntent.setAction(Intent.ACTION_MAIN);
            notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            PendingIntent contentIntent = PendingIntent.getActivity(context, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(id, builder.build());

        } catch (Exception e) {
            notifyFailure(id,msg);

            e.printStackTrace();
        }

        helperDb=new Time_dbHelper(context);
        db=helperDb.getWritableDatabase();
        helperDb.updateStatus(id,"OFF",db);
        helperDb.close();
        db.close();

        if(Main.getInstance()!=null){
            Main.getInstance().refresh();
        }

        if(Edit_time.getInstance()!=null){
            if(id==Edit_time.getInstance().g_id)
                Edit_time.getInstance().swt.setChecked(false);
        }
    }

    public void notifyFailure(int id,String msg){
        String title="Failed to send message";
        String summary="";

        String temp_msg="";

        if(msg.length()>25)
            temp_msg=msg.substring(0,25)+"...";

        //"Service Destroyed."
        NotificationCompat.BigTextStyle noti_message=new NotificationCompat.BigTextStyle();
        noti_message.bigText(msg);
        noti_message.setBigContentTitle(title);
        noti_message.setSummaryText(summary);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channel_id)
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setTicker("BEC SUPPORT")
                .setContentTitle(title)
                .setContentText(temp_msg)
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
    }


}