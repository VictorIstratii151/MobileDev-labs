package com.example.vic.pamlab3;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by vic on 1/6/18.
 */

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{
    private static List<Novelty> mDataset;
    private static Context mContext;


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tvTitle;
        public TextView tvDescription;
        public WebView wvImage;

        public ViewHolder(View itemView)
        {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            tvDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            wvImage = (WebView) itemView.findViewById(R.id.webViewImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        Novelty nv = mDataset.get(position);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(nv.getLink()));
                        mContext.startActivity(browserIntent);
                    }
                }
            });
        }
    }

    public RecyclerAdapter(Context context, List<Novelty> novelties)
    {
        mContext = context;
        mDataset = novelties;
    }

    private Context getContext()
    {
        return mContext;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View noveltyView = inflater.inflate(R.layout.row_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(noveltyView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder, int position)
    {
        Novelty novelty = mDataset.get(position);

        TextView tvTitle = viewHolder.tvTitle;
        tvTitle.setText(novelty.getTitle());

        TextView tvDescription = viewHolder.tvDescription;
        tvDescription.setText(novelty.getDescription());

        WebView wvImage = viewHolder.wvImage;
        wvImage.loadUrl(novelty.getImageUri());
    }

    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }
}
