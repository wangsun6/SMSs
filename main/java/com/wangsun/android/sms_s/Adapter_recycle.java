package com.wangsun.android.sms_s;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by WANGSUN on 15-jan-18.
 */

public class Adapter_recycle extends RecyclerView.Adapter<Adapter_recycle.RecyclerViewHolder> {

    private ArrayList<Class_item> arrayList = new ArrayList<>();
    Context context;

    public Adapter_recycle(ArrayList<Class_item> arrayList, Context context){
        this.arrayList=arrayList;
        this.context=context;


    }

    @Override
    public Adapter_recycle.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        Adapter_recycle.RecyclerViewHolder recyclerViewHolder = new Adapter_recycle.RecyclerViewHolder(view);

        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(Adapter_recycle.RecyclerViewHolder holder, int position) {
        final Class_item class_item = arrayList.get(position);

        String temp_msg=class_item.getMsg();

        if(temp_msg.length()>25)
            holder.tt1.setText(temp_msg.substring(0,25)+"..");
        else
            holder.tt1.setText(temp_msg);

        Date temp_date=new Date(class_item.getDate());
        SimpleDateFormat temp_format=new SimpleDateFormat("d/MM/yyyy\nhh:mm a");

        holder.tt2.setText(temp_format.format(temp_date));
        holder.tt3.setText(class_item.getStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int temp_id=class_item.getId();
                String temp_msg=class_item.getMsg();
                String temp_num=class_item.getNum();
                long temp_date=class_item.getDate();
                String temp_status=class_item.getStatus();

                //Toast.makeText(Main.this,"no.:"+temp_num,Toast.LENGTH_LONG).show();

                Intent intent=new Intent(context,Edit_time.class);
                intent.putExtra("schedule","edit");
                intent.putExtra("id",temp_id);
                intent.putExtra("msg",temp_msg);
                intent.putExtra("num",temp_num);
                intent.putExtra("date",temp_date);
                intent.putExtra("status",temp_status);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tt1,tt2,tt3;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tt1 = (TextView) itemView.findViewById(R.id.id_msg);
            tt2 = (TextView) itemView.findViewById(R.id.id_date);
            tt3 = (TextView) itemView.findViewById(R.id.id_status);

        }
    }
}
