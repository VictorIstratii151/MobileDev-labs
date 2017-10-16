package com.example.vic.pamlab1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import static com.example.vic.pamlab1.R.id.textView;


public class MainActivity extends AppCompatActivity
{
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private boolean checkCameraFront(Context context)
    {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT))
        {
            // this device has a camera
            return true;
        }
        else
        {
            // no camera on this device
            return false;
        }
    }

    private boolean checkCameraBack(Context context)
    {
        for(int i=0;i<Camera.getNumberOfCameras();i++)
        {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i,cameraInfo);
            if(cameraInfo.facing== Camera.CameraInfo.CAMERA_FACING_BACK)
            {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Intent pictureIntent = new Intent(this, PhotoActivity.class);
            pictureIntent.putExtra("TakenPhoto", imageBitmap);
            startActivity(pictureIntent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b1 = (Button)findViewById(R.id.buttonNotify);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final android.support.v4.app.NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.notification_icon_2)
                                .setContentTitle("My notification")
                                .setContentText("Hello World!");

                // Creates an explicit intent for an Activity in your app
                Intent resultIntent = new Intent(getApplicationContext(), SampleActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addParentStack(SampleActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                mBuilder.setContentIntent(resultPendingIntent);
                final NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                final TextView timerView = (TextView) findViewById(R.id.timerNotification);
                new CountDownTimer(10000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        timerView.setText("Time until notification: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        timerView.setText("done!");

                        // mNotificationId is a unique integer your app uses to identify the
                        // notification. For example, to cancel the notification, you can pass its ID
                        // number to NotificationManager.cancel().
                        mNotificationManager.notify(12345, mBuilder.build());
                    }
                }.start();
            }
        });

        Button b2 = (Button)findViewById(R.id.buttonSearch);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View W)
            {
                TextView searchInput = (TextView)findViewById(R.id.searchField);
                String query = searchInput.getText().toString().trim();
                Uri uri = Uri.parse("https://www.google.com/search?q="+query);
                Intent gSearchIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(gSearchIntent);
            }
        });


        final RadioGroup group = (RadioGroup)findViewById(R.id.radioGroup);
        group.check(R.id.radioButtonFront);

        final RadioButton rabuFront = (RadioButton)findViewById(R.id.radioButtonFront);
        final RadioButton rabuRear = (RadioButton)findViewById(R.id.radioButtonRear);


        if(!checkCameraFront(this))
        {
            rabuFront.setEnabled(false);
        }

        if(!checkCameraBack(this))
        {
            rabuRear.setEnabled(false);
        }


        Button b3 = (Button)findViewById(R.id.buttonCamera);

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View W)
            {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                int radioButtonID = group.getCheckedRadioButtonId();
                View selectedRadioButton = group.findViewById(radioButtonID);

                if(selectedRadioButton == rabuFront)
                {
                    cameraIntent.putExtra("android.intent.extras.CAMERA_FACING_FRONT", 1);
                }
                else
                {
                    cameraIntent.putExtra("android.intent.extras.CAMERA_FACING_BACK", 1);
                }

                cameraIntent.putExtra ("camerasensortype", 2);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        });
    }
}
