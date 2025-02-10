package com.example.myapplication.controle_de_gastos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import com.example.myapplication.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import com.example.myapplication.R;

public class GastosAdapter extends BaseAdapter {
    private List<Gasto> gastoList;
    private Context context;

    public GastosAdapter(Context context, List<Gasto> gastoList) {
        this.context = context;
        this.gastoList = gastoList;
    }

    @Override
    public int getCount() {
        return gastoList != null ? gastoList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return gastoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, R.layout.activity_item_gasto);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, R.layout.item_gasto_dropdown);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent, int layoutResource) {
        View rootView;
        ViewHolder holder;

        if (convertView == null) {
            rootView = LayoutInflater.from(context).inflate(layoutResource, parent, false);
            holder = new ViewHolder();
            holder.txtName = rootView.findViewById(R.id.name);
            holder.imageView = rootView.findViewById(R.id.image);
            rootView.setTag(holder);
        } else {
            rootView = convertView;
            holder = (ViewHolder) rootView.getTag();
        }

        Gasto gasto = gastoList.get(position);
        holder.txtName.setText(gasto.getName());
        holder.imageView.setImageResource(gasto.getImage());

        return rootView;
    }

    static class ViewHolder {
        TextView txtName;
        ImageView imageView;
    }
}


