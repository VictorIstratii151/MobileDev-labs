package com.example.vic.lab2;

import android.content.Intent;
import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
{
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAdd = (Button)findViewById(R.id.buttonAddEvent);
        Button btnRemoveUpdate = (Button) findViewById(R.id.buttonRemoveUpdate);

        lv = (ListView) findViewById(R.id.listViewEntries);
//        lv.setAdapter(new ListAdapter(this, sas, sos));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String selectedDate = sdf.format(new Date(calendar.getDate()));
                System.out.println('\n' + selectedDate + '\n');

//                long dateInMs = calendar.getDate();
//                System.out.println('\n' + calendar.getDate() + '\n');

                Intent myIntent = new Intent(MainActivity.this, AddActivity.class);
//                myIntent.putExtra("DATE_IN_MS", dateInMs);
                myIntent.putExtra("sdfDate", selectedDate);
                MainActivity.this.startActivity(myIntent);
            }
        });

        btnRemoveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent editIntent = new Intent(MainActivity.this, RemoveUpdateActivity.class);
                MainActivity.this.startActivity(editIntent);
            }
        });

//        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
//            {
//                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//                String selectedDate = sdf.format(new Date(calendar.getDate()));
//                System.out.println('\n' + selectedDate + '\n');
//            }
//        });


    }
}
