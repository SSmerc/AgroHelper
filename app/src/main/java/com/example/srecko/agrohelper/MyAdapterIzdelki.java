package com.example.srecko.agrohelper;

import android.app.Activity;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Srečko on 17. 05. 2016.
 */
public class MyAdapterIzdelki extends RecyclerView.Adapter<MyAdapterIzdelki.ViewHolder> {
    private DataAll mDataset;
    private  Activity ac;
    private int pos;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textIme,textDatum;
        public ImageView imageTip;
       // public int pos;
        public ViewHolder(View v) {
            super(v);
            textIme=(TextView) v.findViewById(R.id.textIme);
            textDatum=(TextView) v.findViewById(R.id.textDatum);
            imageTip=(ImageView) v.findViewById(R.id.imageView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapterIzdelki(DataAll myDataset, Activity ac,int index) {
        mDataset = myDataset;
        pos=index;
        this.ac=ac;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapterIzdelki.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recview_izdelki_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder,final  int position) {
        //pos=position;
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        List<Izdelek> tmpIzd= mDataset.vrniParcelo(pos).getIzdelki();
        holder.textIme.setText(tmpIzd.get(position).getNaziv());
        holder.textDatum.setText(tmpIzd.get(position).getDatum());
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.vrniParcelo(pos).sizeIzdelki();

    }
}
