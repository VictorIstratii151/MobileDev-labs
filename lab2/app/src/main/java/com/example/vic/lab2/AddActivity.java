package com.example.vic.lab2;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AddActivity extends AppCompatActivity
{
    NotificationService mService;
    boolean mBound = false;
    private AlarmManager mgrAlarm;
    private ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
    private List<NotificationEvent> eventList = new ArrayList<NotificationEvent>();
    private int alarmCounter = 0;
    private int setYear;
    private int setMonth;
    private int setDay;
    private int setHour;
    private int setMinute;
    String filename = "serializedData.json";

//    Intent alarmIntent = new Intent(AddActivity.this, AlarmReceiver.class);



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
        setContentView(R.layout.activity_add);

        final Calendar c = Calendar.getInstance();
        final TextView timeView = (TextView) findViewById(R.id.textViewSetTime);
        final TextView dateView = (TextView) findViewById(R.id.textViewSetDate);
        Bundle extras = getIntent().getExtras();
        String myDate = extras.getString("sdfDate");
        final String[] dateNumbers = myDate.split("/");
        final int newYear = Integer.parseInt(dateNumbers[2]);
        final int newMonth = Integer.parseInt(dateNumbers[1]);
        final int newDay = Integer.parseInt(dateNumbers[0]);
        final int newHour = c.get(Calendar.HOUR_OF_DAY);
        final int newMinute = c.get(Calendar.MINUTE);

        setYear = newYear;
        setMonth = newMonth;
        setDay = newDay;
        setHour = newHour;
        setMinute = newMinute;
        timeView.setText("Time set to: " + newHour + ":" + newMinute);
        dateView.setText("Date set to: " + newYear + "/" + newMonth + "/" + newDay);

        Intent serviceIntent = new Intent(this, NotificationService.class);
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);

        final Intent alarmIntent = new Intent(AddActivity.this, AlarmReceiver.class);
//        final  Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);

        mgrAlarm = (AlarmManager) this.getSystemService(ALARM_SERVICE);


        final TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeView.setText("Time set to: " +  hourOfDay + ":" + minute);
                setHour = hourOfDay;
                setMinute = minute;
                Toast toast = Toast.makeText(AddActivity.this, "Time set!", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog
                .OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                dateView.setText("Date set to:" + year + "/" + month + "/" + dayOfMonth);
                setYear = year;
                setMonth = month;
                setDay = dayOfMonth;
                Toast toast = Toast.makeText(AddActivity.this, "Date set!", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        Button setTimeButton = (Button) findViewById(R.id.buttonSetTime);
        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V)
            {
                new TimePickerDialog(AddActivity.this, onTimeSetListener, newHour, newMinute, true).show();
            }
        });

        Button setDateButton = (Button) findViewById(R.id.buttonSetDate);
        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                System.out.println("\n" + Integer.parseInt(dateNumbers[1]) + "\n");
                new DatePickerDialog(AddActivity.this, onDateSetListener, newYear, newMonth - 1, newDay).show();
            }
        });

        Button fileContents = (Button) findViewById(R.id.buttonFileContents);
        fileContents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                List<NotificationEvent> sas = mService.deserialize(NotificationEvent.class, filename, getApplicationContext());

                for(NotificationEvent e : sas)
                {
                    System.out.println("\n" + e.toString() + "\n");
                }
            }
        });

        Button clearEvents = (Button) findViewById(R.id.buttonClearEvents);
        clearEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                mService.clearFile(getApplicationContext(), filename);
                mService.clearNotifications(eventList.size(), alarmIntent, getApplicationContext());
                eventList.clear();
                eventList = mService.serialize(eventList, filename, getApplicationContext(), alarmIntent);
            }
        });
        Button submitReminder = (Button) findViewById(R.id.buttonSubmitReminder);
        submitReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                EditText editTextNote = (EditText) findViewById(R.id.editTextNote);
                EditText editTextName = (EditText) findViewById(R.id.editTextEventName);

                String note = editTextNote.getText().toString();
                String name = editTextName.getText().toString();

                if(name.length() == 0)
                {
                    AlertDialog alert = new AlertDialog.Builder(AddActivity.this).create();
                    alert.setTitle("Wait a minute...");
                    alert.setMessage("You must give a title to the event");
                    alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alert.show();
                }
                else
                {
                    alarmCounter = eventList.size() + 1;
                    NotificationEvent event = new NotificationEvent(alarmCounter, name, note, setYear, setMonth, setDay, setHour, setMinute);
                    eventList.add(event);
                    eventList = mService.serialize(eventList, filename, getApplicationContext(), alarmIntent);

                    System.out.println("\n" + eventList.size() + "\n");

                    alarmIntent.putExtra("year", event.getYear());
                    alarmIntent.putExtra("month", event.getMonth());
                    alarmIntent.putExtra("day", event.getDay());
                    alarmIntent.putExtra("hour", event.getHour());
                    alarmIntent.putExtra("minute", event.getMinute());
                    alarmIntent.putExtra("name", event.getName());
                    alarmIntent.putExtra("note", event.getNote());
                    alarmIntent.putExtra("id", event.getId());

                    Calendar myCalendar = Calendar.getInstance();
                    myCalendar.set(Calendar.YEAR, setYear);
                    myCalendar.set(Calendar.MONTH, setMonth - 1);
                    myCalendar.set(Calendar.DAY_OF_MONTH, setDay);
                    myCalendar.set(Calendar.HOUR_OF_DAY, setHour);
                    myCalendar.set(Calendar.MINUTE, setMinute);
                    mService.registerAlarm(alarmCounter, alarmIntent, myCalendar.getTimeInMillis(), getBaseContext());

                    Toast.makeText(getBaseContext(), "Reminder Set!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
//    @Override
//    protected void onStop()
//    {
//        super.onStop();
//        unbindService(mConnection);
//        mBound = false;
//    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unbindService(mConnection);
        mBound = false;
    }
}
