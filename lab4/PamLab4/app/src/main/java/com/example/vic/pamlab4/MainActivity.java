package com.example.vic.pamlab4;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    private ImageView imageView;
    private ImageView loading;
    private ImageView loading_bar;
    private TextView tv;
    private boolean progress;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.textViewProgress);
        progress = false;

        final Runnable runnable = new Runnable()
        {
            public void run()
            {
                tv.setText("DONE");
                progress = true;
                handler.postDelayed(this, 5000);
            }
        };

//        imageView = (ImageView) findViewById(R.id.imageView);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                Drawable d = imageView.getDrawable();
//
//                if(d instanceof Animatable)
//                {
//                    ((Animatable) d).start();
//                }
//            }
//        });

        loading_bar = (ImageView) findViewById(R.id.imgBar);
        loading = (ImageView) findViewById(R.id.imgCircle);


        Button start = (Button) findViewById(R.id.btnStart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Drawable d = loading.getDrawable();
                Drawable d2 = loading_bar.getDrawable();
                if(!progress)
                {
                    loading.setVisibility(View.VISIBLE);
                    loading_bar.setVisibility(View.VISIBLE);
                    if(d instanceof Animatable && d2 instanceof Animatable)
                    {
                        ((Animatable) d).start();
                        ((Animatable) d2).start();
                    }

                    tv.setText("PROCESSING");
                    handler.postDelayed(runnable, 5000);
                }
            }
        });

        Button stop = (Button) findViewById(R.id.btnStop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                progress = false;
                handler.removeCallbacks(runnable);
                loading.setVisibility(View.INVISIBLE);
                loading_bar.setVisibility(View.INVISIBLE);
                Drawable d = loading.getDrawable();
                Drawable d2 = loading_bar.getDrawable();
                tv.setText("STOPPED");
                if(d instanceof Animatable && d2 instanceof Animatable)
                {
                    ((Animatable) d).stop();
                    ((Animatable) d2).stop();
                }
            }
        });
    }
}
