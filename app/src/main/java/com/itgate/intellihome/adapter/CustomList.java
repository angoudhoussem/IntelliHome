package com.itgate.intellihome.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itgate.intellihome.R;
import com.itgate.intellihome.model.Piece;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 29/02/16.
 */
public class CustomList extends ArrayAdapter<Piece> {
    Context context;
    List<Piece> drawerItemList;
    int layoutResID;

    Integer[] icons = {

            R.drawable.linvingroom,
            R.drawable.kitchen,
            R.drawable.bedroom,
            R.drawable.bathroom,
            R.drawable.lightbulb,
            R.drawable.tv,
            R.drawable.pcomputer,
            R.drawable.cddvdplayer,
            R.drawable.ac

    };

    public CustomList(Context context, int layoutResourceID,
                         ArrayList<Piece> dataList) {
        super(context, layoutResourceID, dataList);
        this.context = context;
        this.drawerItemList = dataList;
        this.layoutResID = layoutResourceID;

    }


    public View getView(int position, View convertView, ViewGroup parent) {

        DrawerItemHolder drawerHolder;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            drawerHolder = new DrawerItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            drawerHolder.icon = (ImageView) view.findViewById(R.id.imgLeft);
            drawerHolder.titre = (TextView) view.findViewById(R.id.txtName);
           // drawerHolder.description = (TextView) view.findViewById(R.id.title);
            view.setTag(drawerHolder);
        } else {
            drawerHolder = (DrawerItemHolder) view.getTag();

        }


        Piece dItem = (Piece) this.drawerItemList.get(position);
        //drawerHolder.icon.setImageResource(icons[position]);
        drawerHolder.titre.setText(dItem.getTitre());
        //drawerHolder.description.setText(dItem.getDescription());
        Log.d("Getview", "Passed5");

        return view;
    }

    private static class DrawerItemHolder {

        TextView titre;
        ImageView icon;
        TextView description;

    }

}
