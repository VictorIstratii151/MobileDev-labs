package com.example.vic.lab2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by vic on 11/20/17.
 */

public class ListAdapter extends BaseAdapter
{
    String [] result;
    Context context;
    String [] times;
    private static LayoutInflater inflater=null;
    public ListAdapter(RemoveUpdateActivity removeUpdateActivity, String[] nameList, String[] timeList)
    {
        // TODO Auto-generated constructor stub
        result = nameList;
        context = removeUpdateActivity;
        times = timeList;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView notificationNameView;
        TextView notificationTimeView;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_item, null);
        holder.notificationNameView=(TextView) rowView.findViewById(R.id.textViewNotificationName);
        holder.notificationTimeView= (TextView) rowView.findViewById(R.id.textViewNotificationTime);
        holder.notificationNameView.setText(result[position]);
        holder.notificationTimeView.setText(times[position]);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + position, Toast.LENGTH_LONG).show();
                NotificationEvent event = RemoveUpdateActivity.eventList.get(position);
                System.out.println(event.toString());
                Intent modifyEntryIntent = new Intent(context, ModifyEntry.class);
                modifyEntryIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                modifyEntryIntent.putExtra("year", event.getYear());
                modifyEntryIntent.putExtra("month", event.getMonth());
                modifyEntryIntent.putExtra("day", event.getDay());
                modifyEntryIntent.putExtra("hour", event.getHour());
                modifyEntryIntent.putExtra("minute", event.getMinute());
                modifyEntryIntent.putExtra("name", event.getName());
                modifyEntryIntent.putExtra("note", event.getNote());
                modifyEntryIntent.putExtra("id", event.getId());
                ((Activity) context).startActivityForResult(modifyEntryIntent, 1);
            }
        });
        return rowView;
    }
}
