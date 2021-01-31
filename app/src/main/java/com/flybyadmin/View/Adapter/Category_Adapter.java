package com.flybyadmin.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flybyadmin.Model.Category_Model;
import com.flybyadmin.appusekotlin.R;

import java.util.ArrayList;
import java.util.Iterator;

public class Category_Adapter extends BaseAdapter {
    ArrayList<Category_Model> data;
    Context context;
    ArrayList<Category_Model> Select_data = new ArrayList<>();
    private static LayoutInflater inflater=null;
    public Category_Adapter(Context context, ArrayList<Category_Model> data) {
        // TODO Auto-generated constructor stub
        this.data=data;
        this.context=context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();

    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView sevice_name;
        ImageView checkbox;
        LinearLayout Click_view;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.child_all_service, null);
        holder.sevice_name=rowView.findViewById(R.id.sevice_name);
        holder.Click_view=rowView.findViewById(R.id.Click_view);
        holder.checkbox=rowView.findViewById(R.id.checkbox);

        if ( data.get(position).isSelect())
        {
            holder.checkbox.setBackground(context.getResources().getDrawable(R.drawable.ic_selected));
        }else
        {
            holder.checkbox.setBackground(context.getResources().getDrawable(R.drawable.ic_unselected));
        }

        holder.sevice_name.setText(data.get(position).getName());
        holder.Click_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.get(position).isSelect())
                {
                    data.get(position).setSelect(false);
                    removeProcess();
                    notifyDataSetChanged();
                }else
                {
                    data.get(position).setSelect(true);
                    notifyDataSetChanged();
                    Select_data.add(data.get(position));
                }
            }
            private void removeProcess() {
                if (Select_data.size() > 0) {
                    for (Iterator<Category_Model> iterator = Select_data.iterator(); iterator.hasNext(); ) {
                        Category_Model category_model = iterator.next();
                        if (category_model.getID().equalsIgnoreCase(data.get(position).getID())) {
                            iterator.remove();
                        }
                    }
                }
            }
        });
        return rowView;
    }

    public  ArrayList<Category_Model> get_selected_item()
    {
        return Select_data;
    }


}