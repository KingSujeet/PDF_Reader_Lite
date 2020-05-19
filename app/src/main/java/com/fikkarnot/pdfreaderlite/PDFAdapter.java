package com.fikkarnot.pdfreaderlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;


import java.io.File;

import java.util.ArrayList;


public class PDFAdapter extends ArrayAdapter<File> {

    // variables or instances of class

    Context context;
    ViewHolder viewHolder;
    ArrayList<File> al_pdf;

    // constructor
    public PDFAdapter(Context context,  ArrayList<File> al_pdf) {
        super(context, R.layout.adapter_pdf_view,al_pdf);

        this.context = context;
        this.al_pdf = al_pdf;
    }
    // it is for searchView. It will sort the listview according to searchView
    public void update(ArrayList<File> results){

        al_pdf = new ArrayList<>();
        al_pdf.addAll(results);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (al_pdf.size()>0){

            return al_pdf.size();
        }
        else  return 1;
    }

    // getting view
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_pdf_view,parent,false);
            viewHolder = new ViewHolder();

            viewHolder.filename = (TextView) convertView.findViewById(R.id.text_name);
            convertView.setTag(viewHolder);
        }
        else {

            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            viewHolder.filename.setText(al_pdf.get(position).getName());
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }
    // viewHolder
    public class ViewHolder{

        TextView filename;
    }


}


