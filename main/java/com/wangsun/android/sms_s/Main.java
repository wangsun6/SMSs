package com.wangsun.android.sms_s;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class Main extends AppCompatActivity{

    private static Main obj;

    ArrayList<Class_item> arrayList;
    TextView txt_add;

    SQLiteDatabase db;
    Time_dbHelper helperDb;
    Cursor cursor;

    RecyclerView recyclerView2;
    RecyclerView.Adapter adapter2;
    RecyclerView.LayoutManager layoutManager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView2 =(RecyclerView)findViewById(R.id.recycler_id);
        txt_add=(TextView)findViewById(R.id.id_add);

        layoutManager2 = new LinearLayoutManager(this);

        obj=this;

        txt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main.this,Edit_time.class);
                intent.putExtra("schedule","add");
                startActivity(intent);
            }
        });

    }

    public void get_list(){
        arrayList=new ArrayList<>();

        helperDb=new Time_dbHelper(this);
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

                arrayList.add(new Class_item(id,msg,num,date,status));
                //Toast.makeText(Main.this,"id:"+id+"\nmsg:"+msg,Toast.LENGTH_LONG).show();

            }while(cursor.moveToNext());
        }

        helperDb.close();
        db.close();

        if(arrayList.size()==0)
            txt_add.setVisibility(View.VISIBLE);
        else
            txt_add.setVisibility(View.GONE);

        adapter2=new Adapter_recycle(arrayList,Main.this);

        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setAdapter(adapter2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_list();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        /*if(sp.getString("walk_through_broadcast","no").equals("no")){
            temp_item=menu.findItem(R.id.action_send_id);
            temp_item.setEnabled(false);
            showSideNavigationPrompt();
        }*/

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
            case R.id.action_add_id:
                Intent intent=new Intent(Main.this,Edit_time.class);
                intent.putExtra("schedule","add");
                startActivity(intent);
                break;
        }
        return true;
    }

    public static Main getInstance(){
        return obj;
    }

    public void refresh(){
        get_list();
    }
}
