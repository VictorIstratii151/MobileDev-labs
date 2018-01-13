package com.example.vic.lab2;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveUpdateActivity extends AppCompatActivity
{
    NotificationService mService;
    boolean mBound = false;
    static List<NotificationEvent> eventList = new ArrayList<NotificationEvent>();
    private ArrayList<String> sas = new ArrayList<>();
    private ArrayList<String> sos = new ArrayList<>();
    private ArrayAdapter<String> myAdapter;
    String filename = "serializedData.json";
    String[] sas1;
    String[] sos1;
    private ListView lv;


    private ServiceConnection mConnection  = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            NotificationService.LocalBinder binder = (NotificationService.LocalBinder) service;
            mService = binder.getService();
            if(mService != null)
            {
                eventList = mService.deserialize(NotificationEvent.class, filename, getApplicationContext());
            }
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            mBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_update);

        lv = (ListView) findViewById(R.id.listViewAvailableEvents);

        Intent serviceIntent = new Intent(this, NotificationService.class);
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);

        final TextView tvIndications = (TextView) findViewById(R.id.textViewIndications);
        tvIndications.setText("Click the button to fetch the available events.");

        Button btnFetchEvents = (Button) findViewById(R.id.buttonFetchEvents);
        btnFetchEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                eventList = mService.deserialize(NotificationEvent.class, filename, getApplicationContext());
                System.out.println("\n" + eventList.size() + "\n");
                if(eventList.size() != 0)
                {
                    for(NotificationEvent e : eventList)
                    {
                        System.out.println(e.getName());
                        sas.add(e.getName());
                        sos.add(e.getYear() + "/" + e.getMonth() + "/" + e.getDay() + "  " + e.getHour() + ":" + e.getMinute());
                    }

                    sas1 = new String[sas.size()];
                    sas.toArray(sas1);
                    sos1 = new String[sos.size()];
                    sos.toArray(sos1);

                    tvIndications.setText("Click on one event to edit or delete it.");
                }
                if(eventList.size() == 0)
                {
                    sas1 = new String[0];
                    sos1 = new String[0];
                    tvIndications.setText("Looks like you don't have any upcoming events.");
                }
                System.out.println("\n" + sas1 + "\n");
                System.out.println("\n" + sos1 + "\n");

                lv.setAdapter(new ListAdapter(RemoveUpdateActivity.this, sas1, sos1));
                ((BaseAdapter)lv.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        final Intent alarmIntent = new Intent(RemoveUpdateActivity.this, AlarmReceiver.class);
        System.out.println("\n" + alarmIntent + "\n");
        if(requestCode == 1)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                System.out.println("\n NIBAAAAAA \n");
                Bundle extras = data.getExtras();
                boolean resultFlag = extras.getBoolean("resultFlag");
                System.out.println("\n" + extras + "\n");
                if(resultFlag)
                {
                    System.out.println("\n DELETING THE ENTRY \n");
                    int arrayIndex = extras.getInt("arrayIndex");
                    eventList.remove(arrayIndex);
                    eventList = mService.serialize(eventList, filename, getApplicationContext(), alarmIntent);
                }
                else
                {
                    System.out.println("\n EDITING THE ENTRY \n");
                    int arrayIndex = extras.getInt("arrayIndex");
                    final int year = extras.getInt("year");
                    final int month = extras.getInt("month");
                    final int day = extras.getInt("day");
                    final int hour = extras.getInt("hour");
                    final int minute = extras.getInt("minute");
                    String name = extras.getString("name");
                    String note = extras.getString("note");
                    int id = extras.getInt("id");

                    NotificationEvent replacingEvent = new NotificationEvent(id, name, note, year, month, day, hour, minute);
                    eventList.set(arrayIndex, replacingEvent);
                    System.out.println("\n" + alarmIntent + "\n");
                    eventList = mService.serialize(eventList, filename, getApplicationContext(), alarmIntent);
                }
            }
            if(resultCode == Activity.RESULT_CANCELED)
            {
                System.out.println("\nBAAAAAAAAAAAAAAAAAAAAAAAAAD\n");
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unbindService(mConnection);
        mBound = false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        lv = (ListView) findViewById(R.id.listViewAvailableEvents);
        String[] dummy = new String[] {""};
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(Arrays.asList(dummy));

        myAdapter = new ArrayAdapter<String>(RemoveUpdateActivity.this, R.layout.simple_row, list);
        lv.setAdapter(myAdapter);
    }
}
