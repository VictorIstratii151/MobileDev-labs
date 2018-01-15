package com.example.vic.pamlab5;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by vic on 1/14/18.
 */

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DocViewHolder>
{
    List<DoctorModel> doctors;

    public DoctorAdapter(List<DoctorModel> doctors)
    {
        this.doctors = doctors;
    }

    public static class DocViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        TextView docName;
        TextView docSpec;
        TextView docLoc;
        ImageView docPic;

        public DocViewHolder(View itemView)
        {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            docName = (TextView) itemView.findViewById(R.id.doc_name);
            docSpec = (TextView) itemView.findViewById(R.id.doc_spec);
            docLoc = (TextView) itemView.findViewById(R.id.doc_loc);
            docPic = (ImageView) itemView.findViewById(R.id.doc_pic);
        }
    }

    @Override
    public int getItemCount()
    {
        return doctors.size();
    }

    @Override
    public DocViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.doc_row_item, viewGroup, false);
        DocViewHolder holder = new DocViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(DocViewHolder docViewHolder, int i)
    {
        docViewHolder.docName.setText(doctors.get(i).getName());
        docViewHolder.docSpec.setText(doctors.get(i).getSpeciality());
        docViewHolder.docLoc.setText(doctors.get(i).getAddress());
        docViewHolder.docPic.setImageResource(R.drawable.doc1);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
