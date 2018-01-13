package com.example.vic.lab2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.app.TimePickerDialog;
import android.content.Intent;
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

public class ModifyEntry extends AppCompatActivity
{
    private int setYear;
    private int setMonth;
    private int setDay;
    private int setHour;
    private int setMinute;
    private String setName;
    private String setNote;
    private int setId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_entry);

        Bundle extras = getIntent().getExtras();

        final int year = extras.getInt("year");
        final int month = extras.getInt("month");
        final int day = extras.getInt("day");
        final int hour = extras.getInt("hour");
        final int minute = extras.getInt("minute");
        String name = extras.getString("name");
        String note = extras.getString("note");
        int id = extras.getInt("id");


        setYear = year;
        setMonth = month;
        setDay = day;
        setHour = hour;
        setMinute = minute;
        setName = name;
        setNote = note;
        setId = id;

        final EditText editedName = (EditText) findViewById(R.id.editTextEditedName);
        final EditText editedNote = (EditText) findViewById(R.id.editTextEditedNote);
        editedName.setText(name);
        editedNote.setText(note);

        final TextView editedDate = (TextView) findViewById(R.id.TextViewEditedDate);
        final TextView editedTime = (TextView) findViewById(R.id.TextViewEditedTime);
        editedDate.setText("Date set to: " + setYear + "/" + setMonth + "/" + setDay);
        editedTime.setText("Time set to: " + setHour + ":" + setMinute);

        final TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                editedTime.setText("Time set to: " +  hourOfDay + ":" + minuteOfDay);
                setHour = hourOfDay;
                setMinute = minuteOfDay;
                Toast.makeText(ModifyEntry.this, "Time reset!", Toast.LENGTH_SHORT).show();
            }
        };

        final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog
                .OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int newYear, int newMonth, int newDayOfMonth)
            {
                editedDate.setText("Date set to: " + newYear + "/" + newMonth + "/" + newDayOfMonth);
                setYear = newYear;
                setMonth = newMonth;
                setDay = newDayOfMonth;
                Toast.makeText(ModifyEntry.this, "Date reset!", Toast.LENGTH_SHORT).show();
            }
        };

        Button resetTimeButton = (Button) findViewById(R.id.buttonResetTime);
        resetTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V)
            {
                new TimePickerDialog(ModifyEntry.this, onTimeSetListener, setHour, setMinute, true).show();
            }
        });

        Button resetDateButton = (Button) findViewById(R.id.buttonResetDate);
        resetDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new DatePickerDialog(ModifyEntry.this, onDateSetListener, setYear, setMonth - 1, setDay).show();
            }
        });

        Button btnDeleteEvent = (Button) findViewById(R.id.buttonRemoveEvent);
        btnDeleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ModifyEntry.this);
                builder.setTitle("Confirm deletion");
                builder.setMessage("Do You really want to delete the event?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        boolean deletionFlag = true;
                        int arrayIndex = setId - 1;

                        Intent returnIntent = new Intent(ModifyEntry.this, RemoveUpdateActivity.class);
                        returnIntent.putExtra("resultFlag", true);
                        returnIntent.putExtra("arrayIndex", arrayIndex);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        Button btnReturn = (Button) findViewById(R.id.buttonSubmitChanges);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(editedName.getText().length() == 0)
                {
                    AlertDialog alert = new AlertDialog.Builder(ModifyEntry.this).create();
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
                    boolean deletionFlag = false;
                    setName = editedName.getText().toString();
                    setNote = editedNote.getText().toString();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("resultFlag", deletionFlag);
                    returnIntent.putExtra("year", setYear);
                    returnIntent.putExtra("month", setMonth);
                    returnIntent.putExtra("day", setDay);
                    returnIntent.putExtra("hour", setHour);
                    returnIntent.putExtra("minute", setMinute);
                    returnIntent.putExtra("name", setName);
                    returnIntent.putExtra("note", setNote);
                    returnIntent.putExtra("id", setId);
                    returnIntent.putExtra("arrayIndex", setId - 1);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }
}
