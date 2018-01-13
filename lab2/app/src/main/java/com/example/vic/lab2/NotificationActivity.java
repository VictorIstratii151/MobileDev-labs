package com.example.vic.lab2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity
{
    private MediaPlayer mMediaPlayer;
    NotificationService mService;
    boolean mBound = false;
    static List<NotificationEvent> eventList = new ArrayList<NotificationEvent>();
    String filename = "serializedData.json";

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
        final Intent alarmIntent = new Intent(NotificationActivity.this, AlarmReceiver.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Intent serviceIntent = new Intent(this, NotificationService.class);
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);

        Bundle extras = getIntent().getExtras();
        final Bundle event = extras.getBundle("event");
        int year = event.getInt("year");
        int month = event.getInt("month");
        int day = event.getInt("day");
        int hour = event.getInt("hour");
        int minute = event.getInt("minute");
        String name = event.getString("name");
        String note = event.getString("note");
        final int id = event.getInt("id");

        TextView timeView = (TextView) findViewById(R.id.textViewTime);
        timeView.setText(timeView.getText().toString() + year + "/" + month + "/" + day + "  " + hour + ":" + minute);
        TextView nameView = (TextView) findViewById(R.id.textViewName);
        nameView.setText("Name of event: " + name);
        TextView noteView = (TextView) findViewById(R.id.textViewNote);
        if(noteView.getText().toString().length() == 0)
        {
            noteView.setText("You don't have any notes for this event.");
        }
        noteView.setText("Note to Yourself: " + note);

        Button dismissAlarm = (Button) findViewById(R.id.buttonDismiss);
        dismissAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int arrayIndex = id - 1;
                eventList.remove(arrayIndex);
                System.out.println("\n" + alarmIntent + "\n");
                mService.serialize(eventList, filename, getApplicationContext(), alarmIntent); //wtf
                mMediaPlayer.stop();
                finish();
            }
        });

        playSound(this, getAlarmUri());


//        System.out.println("\n" +  year + "\n" + month + "\n" + day + "\n" + hour + "\n" + minute + "\n" + name + "\n" + note + "\n");
    }

    private void playSound(Context context, Uri alert) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }

    //Get an alarm sound. Try for an alarm. If none set, try notification,
    //Otherwise, ringtone.
    private Uri getAlarmUri() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unbindService(mConnection);
        mBound = false;
    }
}
