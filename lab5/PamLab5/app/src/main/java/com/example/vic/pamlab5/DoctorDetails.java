package com.example.vic.pamlab5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DoctorDetails extends AppCompatActivity
{
    private TextView tvName;
    private TextView tvSpec;
    private TextView tvDesc;
    private TextView tvLoc;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        String spec = extras.getString("speciality");
        String address = extras.getString("address");
        String description = extras.getString("description");

        tvName = (TextView) findViewById(R.id.textViewDocName);
        tvDesc = (TextView) findViewById(R.id.textViewDetails2);
        tvLoc = (TextView) findViewById(R.id.textViewDetails4);
        tvSpec = (TextView) findViewById(R.id.textViewDocSpec);
        tvName.setText(name);
        tvDesc.setText(description);
        tvSpec.setText(spec);
        tvLoc.setText(address);
    }
}
