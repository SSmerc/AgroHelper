package com.example.srecko.agrohelper;

/**
 * Created by Heavy on 7. 12. 2016.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter{
    private DataAll mDataset;
    private Activity ac;
    private Context _context;
    public TextView textIme;
    public Button btnMore;
    public ImageView imageTip;
    private ArrayList<ParcelInfo> pi;
    private ArrayList<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ParcelInfo> _listDataChild;


    public ExpandableListAdapter(Context context,DataAll myDataset, Activity ac){
        _listDataHeader= new ArrayList<>();
        _listDataChild= new HashMap<>();
        mDataset = myDataset;
        this.ac=ac;
        this._context = context;
        for (int i=0;i<mDataset.steviloParcel();i++) {
            _listDataHeader.add(mDataset.vrniParcelo(i).getIme_parcele());
            _listDataChild.put(mDataset.vrniParcelo(i).getIme_parcele(),mDataset.vrniParcelo(i).getParcelInfo());
        }
    }

    public void delete(int position) {
        _listDataHeader.remove(mDataset.vrniParcelo(position).getIme_parcele());
        _listDataChild.remove(mDataset.vrniParcelo(position).getIme_parcele());
    }
    /*public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }*/

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return _listDataChild.get(_listDataHeader.get(groupPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, final ViewGroup parent) {
        final ParcelInfo child = (ParcelInfo) getChild(groupPosition, 0);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtPovrsina = (TextView) convertView
                .findViewById(R.id.txtPovrsina);
        btnMore=(Button) convertView.findViewById(R.id.btnMore);
        //txtPovrsina.setTypeface(null, Typeface.BOLD);
        txtPovrsina.setText("Povrsina: "+String.format("%.2f",child.getPovrsina())+" m2");
        TextView txtSt = (TextView) convertView
                .findViewById(R.id.txtStparcele);
        //txtSt.setTypeface(null, Typeface.BOLD);
       txtSt.setText("Stevilka: "+child.getStevilka());
       btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dva = new Intent(_context,ParcelInfoActivity.class)
                .putExtra("Parcela ime",mDataset.vrniParcelo(groupPosition).getIme_parcele())
                .putExtra("Parcela tip",mDataset.vrniParcelo(groupPosition).getTip().toString())
                .putExtra("Index",groupPosition)
                .putExtra("Stevilka",mDataset.vrniParcelo(groupPosition).getParcelInfo().getStevilka())
                .putExtra("Intent","Info");
                _context.startActivity(dva);
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return _listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return _listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
           // LayoutInflater.from(parent.getContext());
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
        textIme=(TextView) convertView.findViewById(R.id.textIme);
        imageTip=(ImageView) convertView.findViewById(R.id.imageView);
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
        return false;
    }
    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

}
