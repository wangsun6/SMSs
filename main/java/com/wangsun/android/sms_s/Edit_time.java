package com.wangsun.android.sms_s;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class Edit_time extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,View.OnClickListener{

    Switch swt;
    EditText edt_num,edt_msg;
    TextView txt_date,txt_time;//txt_delete,txt_save;

    String from; //getIntent
    String g_num,g_msg,g_status; //g=global variable
    
    int g_id;
    long g_date;

    //SharedPreferences sp;
    //SharedPreferences.Editor editor;

    private static Edit_time ins;

    int yr,mon,day;
    int hr,min;

    SQLiteDatabase db;
    Time_dbHelper helperDb;
    Cursor cursor;

    Calendar calendar_main = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_time);

        //sp = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        //editor = sp.edit();

        ins = this;

        swt=(Switch)findViewById(R.id.switchId);

        edt_num=(EditText)findViewById(R.id.id_num);
        txt_date=(TextView)findViewById(R.id.id_date);
        txt_time=(TextView)findViewById(R.id.id_time);
        edt_msg=(EditText)findViewById(R.id.id_msg);
        //txt_delete=(TextView)findViewById(R.id.id_delete);
        //txt_save=(TextView)findViewById(R.id.id_save);

        txt_date.setOnClickListener(this);
        txt_time.setOnClickListener(this);
        swt.setOnClickListener(this);

        ins = this;

        swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(from.equals("add")){
                        check_error(0);
                    }
                    else {
                        check_error(1);
                    }
                }
                else {
                    enable_disable_views(1);
                    off_status();
                    cancelMessage();
                }
            }
        });

        from=getIntent().getStringExtra("schedule");
        
        if(from.equals("add"))
            initialize_create();
        else{
            g_id=getIntent().getIntExtra("id",0);
            g_msg=getIntent().getStringExtra("msg");
            g_num=getIntent().getStringExtra("num");
            g_date=getIntent().getLongExtra("date",0);
            g_status=getIntent().getStringExtra("status");
            initialize_edit();
        }
        
    }

    @Override
    public void onClick(View v) {
        if(v==txt_date){
            Calendar calendar_set=Calendar.getInstance();
            yr=calendar_set.get(Calendar.YEAR);
            mon=calendar_set.get(Calendar.MONTH);
            day=calendar_set.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker=new DatePickerDialog(Edit_time.this,this,yr,mon,day);

            datePicker.show();
        }
        else if(v==txt_time){

            Calendar calendar_set=Calendar.getInstance();
            hr=calendar_set.get(Calendar.HOUR_OF_DAY);
            min=calendar_set.get(Calendar.MINUTE);
            TimePickerDialog timePicker=new TimePickerDialog(Edit_time.this,this,hr,min,
                    DateFormat.is24HourFormat(Edit_time.this));
            timePicker.show();

        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        calendar_main.set(Calendar.YEAR,year);
        calendar_main.set(Calendar.MONTH,month);
        calendar_main.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        Date temp_date=calendar_main.getTime();

        SimpleDateFormat temp_formate=new SimpleDateFormat("d/MM/yyyy");

        txt_date.setText(temp_formate.format(temp_date));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        calendar_main.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar_main.set(Calendar.MINUTE,minute);
        SimpleDateFormat formatConvert = new SimpleDateFormat("hh:mm a");
        Date dateConvert=calendar_main.getTime();

        txt_time.setText(formatConvert.format(dateConvert));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View view =getCurrentFocus();
        if(view!=null){
            InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }

        switch (item.getItemId()) {
            case R.id.action_delete_id:
                if(from.equals("add")){
                    finish();
                }
                else {
                    delete_row();
                }
                break;
        }
        return true;
    }

    public static Edit_time  getInstance(){
        return ins;
    }

    public void initialize_create(){

        Date date_date=calendar_main.getTime();

        SimpleDateFormat only_time = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat only_date = new SimpleDateFormat("d/MM/yyyy");

        edt_num.setText("");
        txt_date.setText(only_date.format(date_date));
        txt_time.setText(only_time.format(date_date));
        edt_msg.setText("");
    }

    public void initialize_edit(){

        Date temp_date=new Date(g_date);
        calendar_main.setTime(temp_date); //set main calender date/time

        SimpleDateFormat only_time = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat only_date = new SimpleDateFormat("d/MM/yyyy");

        edt_num.setText(g_num);
        txt_date.setText(only_date.format(temp_date));
        txt_time.setText(only_time.format(temp_date));
        edt_msg.setText(g_msg);

        if(g_status.equals("ON"))
            swt.setChecked(true);
        else
            swt.setChecked(false);
    }

    public void check_error(int a){
        Calendar calendar_now=Calendar.getInstance();
        Date date_now=calendar_now.getTime();

        Date date_set=calendar_main.getTime();

        long temp_date=date_set.getTime();

        g_num=edt_num.getText().toString();
        g_num=g_num.trim();
        g_msg=edt_msg.getText().toString();
        g_date=temp_date;
        g_status="ON";

        if(g_num.length()==10){
            if(g_msg.length()>0){
                if(g_msg.length()<145){
                    if(date_set.after(date_now)){
                        if(a==0)
                            add_data();
                        else
                            edit_data();
                    }
                    else {
                        swt.setChecked(false);
                        Toast.makeText(Edit_time.this, "Check date and time.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    swt.setChecked(false);
                    Toast.makeText(Edit_time.this,"Message too long (145 max.)",Toast.LENGTH_LONG).show();
                }

            }
            else {
                swt.setChecked(false);
                Toast.makeText(Edit_time.this,"Enter message.",Toast.LENGTH_LONG).show();
            }
        }
        else {
            swt.setChecked(false);
            Toast.makeText(Edit_time.this,"Invalid phone number..",Toast.LENGTH_SHORT).show();
        }

    }

    public void add_data(){

        ArrayList<Integer> arrayList=new ArrayList<>();

        helperDb=new Time_dbHelper(Edit_time.this);
        db=helperDb.getWritableDatabase();
        helperDb.create_table(db);
        helperDb.addvalues(g_msg,g_num,g_date,g_status,db);

        cursor=helperDb.getId(db);
        if(cursor.moveToFirst()){
            do{
                int id;
                id=cursor.getInt(0);

                arrayList.add(id);

            }while(cursor.moveToNext());
        }

        g_id=Collections.max(arrayList);

        helperDb.close();
        db.close();

        sendMessage();

        finish();
    }

    public void edit_data(){
        helperDb=new Time_dbHelper(Edit_time.this);
        db=helperDb.getWritableDatabase();
        helperDb.updateValues(g_id,g_msg,g_num,g_date,g_status,db);

        helperDb.close();
        db.close();

        sendMessage();

        enable_disable_views(0);

    }

    public void sendMessage(){

        Intent intentBoot = new Intent(Edit_time.this, Alarm_broadcast.class);
        intentBoot.putExtra("id",g_id);
        intentBoot.putExtra("msg",g_msg);
        intentBoot.putExtra("num",g_num);

        intentBoot.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP,calendar_main.getTimeInMillis(),
                PendingIntent.getBroadcast(Edit_time.this,g_id, intentBoot, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    public void cancelMessage(){
        Intent intent = new Intent(this,Alarm_broadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, g_id, intent,PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        //Toast.makeText(this, ".", Toast.LENGTH_LONG).show();
    }

    public void delete_row(){

        if(g_status.equals("OFF")){
            helperDb=new Time_dbHelper(Edit_time.this);
            db=helperDb.getWritableDatabase();
            helperDb.deleteRow(g_id,db);
            Toast.makeText(Edit_time.this, "Deleted.", Toast.LENGTH_SHORT).show();
            helperDb.close();
            db.close();
            finish();
        }
        else
            Toast.makeText(Edit_time.this, "Disable the schedule first.", Toast.LENGTH_SHORT).show();

    }

    public void off_status(){
        g_status="OFF";

        helperDb=new Time_dbHelper(Edit_time.this);
        db=helperDb.getWritableDatabase();
        helperDb.updateStatus(g_id,g_status,db);
        helperDb.close();
        db.close();
    }

    public void enable_disable_views(int c){
        if(c==1){
            edt_num.setEnabled(true);
            edt_msg.setEnabled(true);
            txt_time.setEnabled(true);
            txt_date.setEnabled(true);
            //.setEnabled(true);
        }
        else {
            //edt_num.setTextColor(Color.parseColor("#9C27B0"));
            edt_num.setEnabled(false);
            edt_msg.setEnabled(false);
            txt_time.setEnabled(false);
            txt_date.setEnabled(false);
        }
    }

    public void pickNumber(View view){
        Intent pick_intent = new Intent(Intent.ACTION_PICK);
        // BoD con't: CONTENT_TYPE instead of CONTENT_ITEM_TYPE
        pick_intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pick_intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri uri = data.getData();

            if (uri != null) {
                Cursor c = null;
                try {
                    c = getContentResolver().query(uri, new String[]{
                                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                                    ContactsContract.CommonDataKinds.Phone.TYPE },
                            null, null, null);

                    if (c != null && c.moveToFirst()) {
                        String number = c.getString(0);
                        int type = c.getInt(1);
                        showSelectedNumber(type, number);
                    }
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
            }
        }
    }

    public void showSelectedNumber(int type, String number) {
        edt_num.setText(number.replaceAll("\\s",""));
    }

}
