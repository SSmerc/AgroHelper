package com.example.srecko.agrohelper;

/**
 * Created by Heavy on 7. 12. 2016.
 */

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private DataAll mDataset;
    private Activity ac;
    private Context _context;
    public TextView textIme;
    public ImageView imageTip;
    private List<ParcelInfo> pi;
    //private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    //private HashMap<String, List<String>> _listDataChild;


    public ExpandableListAdapter(Context context,DataAll myDataset, Activity ac) {
        mDataset = myDataset;
        this.ac=ac;
        this._context = context;
    }

    /*public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }*/

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return  mDataset.vrniParcelo(groupPosition).getParcelInfo();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtPovrsina = (TextView) convertView
                .findViewById(R.id.txtPovrsina);
        txtPovrsina.setTypeface(null, Typeface.BOLD);
        //txtPovrsina.setText("Povrsina: "+Double.toString(mDataset.vrniParcelo(groupPosition).getParcelInfo().getPovrsina()));
        TextView txtSt = (TextView) convertView
                .findViewById(R.id.txtStparcele);
        txtSt.setTypeface(null, Typeface.BOLD);
      //  txtSt.setText("Stevilka: "+mDataset.vrniParcelo(groupPosition).getParcelInfo().getStevilka().toString());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDataset.vrniParcelo(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mDataset.steviloParcel();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        textIme=(TextView) convertView.findViewById(R.id.textIme);
        imageTip=(ImageView) convertView.findViewById(R.id.imageView);
        if (convertView == null) {
           // LayoutInflater.from(parent.getContext());
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
        textIme.setText(mDataset.vrniParcelo(groupPosition).getIme_parcele());
        switch (mDataset.vrniParcelo(groupPosition).getTip())
        {
            case GOZD:
                imageTip.setImageDrawable(ResourcesCompat.getDrawable(this.ac.getResources(),R.drawable.gozd,null));
                break;
            case POLJE:
               imageTip.setImageDrawable(ResourcesCompat.getDrawable(this.ac.getResources(),R.drawable.polje,null));
                break;
            case TRAVNIK:
                imageTip.setImageDrawable(ResourcesCompat.getDrawable(this.ac.getResources(),R.drawable.travnik,null));
                break;
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
