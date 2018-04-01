package com.example.vic.pamlab6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DoctorList extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private List<DoctorModel> doctors = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        doctors.add(new DoctorModel(1, "Vasea", "Endocrinolog", "ciocana", "loh", 1.2f, "sas"));
        doctors.add(new DoctorModel(2, "Jojo", "Proctolog", "rishkanovka", "loh", 1.2f, "sas"));
        doctors.add(new DoctorModel(3, "Dio", "Venerolog", "demasia", "loh", 1.2f, "sas"));
        DoctorAdapter adapter = new DoctorAdapter(doctors);

        recyclerView.setAdapter(adapter);
    }
}
