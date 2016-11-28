package com.example.srecko.agrohelper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by Srečko on 17. 05. 2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private DataAll mDataset;
    private  Activity ac;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textIme;
        public ImageView imageTip;
        public ViewHolder(View v) {
            super(v);
            textIme=(TextView) v.findViewById(R.id.textIme);
            imageTip=(ImageView) v.findViewById(R.id.imageView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(DataAll myDataset, Activity ac) {
        mDataset = myDataset;
        this.ac=ac;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recview_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder,final  int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textIme.setText(mDataset.vrniParcelo(position).getIme_parcele());
        switch (mDataset.vrniParcelo(position).getTip())
        {
            case GOZD:
               holder.imageTip.setImageDrawable(ResourcesCompat.getDrawable(this.ac.getResources(),R.drawable.gozd,null));
                break;
            case POLJE:
                holder.imageTip.setImageDrawable(ResourcesCompat.getDrawable(this.ac.getResources(),R.drawable.polje,null));
                break;
            case TRAVNIK:
                holder.imageTip.setImageDrawable(ResourcesCompat.getDrawable(this.ac.getResources(),R.drawable.travnik,null));
                break;
        }
        /*holder.textIme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String[] parcelInfo= new String[]{mDataset.vrniParcelo(position).getIme_parcele(),mDataset.vrniParcelo(position).getTip().toString()};
                Intent dva = new Intent(ac, ParcelInfoActivity.class);
                dva.putExtra("Parcela ime",mDataset.vrniParcelo(position).getIme_parcele());
                dva.putExtra("Parcela tip",mDataset.vrniParcelo(position).getTip().toString());
                dva.putExtra("Index",position);
                dva.putExtra("Intent","Info");
                ac.startActivity(dva);
            }
        });*/
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.steviloParcel();
    }
}
