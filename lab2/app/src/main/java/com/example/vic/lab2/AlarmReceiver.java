package com.example.vic.lab2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PowerManager;
import android.view.Window;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by vic on 11/13/17.
 */

public class AlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle extras = intent.getExtras();
        Intent i = new Intent(context, NotificationActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//        int year = extras.getInt("year");
//        int month = extras.getInt("month");
//        int day = extras.getInt("day");
//        int hour = extras.getInt("hour");
//        int minute = extras.getInt("minute");
//        String name = extras.getString("name");
//        String note = extras.getString("note");
        i.putExtra("event", extras);

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = pm.newWakeLock((PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "NoificationService");
        mWakeLock.acquire();
        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        context.startActivity(i);
        System.out.println("\n" + "SAFSAKFJ:ASSAD" + "\n");
        System.out.println("\n" + new Date(System.currentTimeMillis()) + "\n");
        mWakeLock.release();
    }
}
