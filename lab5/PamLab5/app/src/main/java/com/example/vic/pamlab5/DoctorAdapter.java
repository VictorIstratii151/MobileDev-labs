package com.example.vic.pamlab5;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
    private static List<DoctorModel> doctors;
    private static Context mContext;

    public DoctorAdapter(List<DoctorModel> doctors, Context context)
    {
        this.doctors = doctors;
        this.mContext = context;
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


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        DoctorModel doc = doctors.get(position);
                        Intent detailsIntent = new Intent(mContext, DoctorDetails.class);
                        detailsIntent.putExtra("name", doc.getName());
                        detailsIntent.putExtra("speciality", doc.getSpeciality());
                        detailsIntent.putExtra("address", doc.getAddress());
                        detailsIntent.putExtra("description", doc.getAbout());

                        mContext.startActivity(detailsIntent);
                    }
                }
            });
        }
    }

    private Context getContext()
    {
        return mContext;
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
