package com.dhruv.covid19tracker.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhruv.covid19tracker.R;
import com.dhruv.covid19tracker.singleRow;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.holder>  {

    ArrayList<singleRow> lst;
    Context context ;

    public RecyclerViewAdapter(ArrayList<singleRow> lst, Context context) {
        this.lst = lst;
        this.context = context;

    }

    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.singlerowl_layout,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        String  name = lst.get(position).getName();
        String  confirmed = lst.get(position).getConfirmed();
        String  Cured = lst.get(position).getCured();
        String  death = lst.get(position).getDeath();


        holder.confimed.setText(confirmed);
        holder.name.setText(name);
        holder.cured.setText(Cured);
        holder.death.setText(death);

    }

    @Override
    public int getItemCount() {
        return lst.size();
    }

    class holder extends  RecyclerView.ViewHolder{
        TextView name ;
        TextView confimed ;
        TextView cured ;
        TextView death ;
        public holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            confimed =itemView.findViewById(R.id.confirmed);
          cured = itemView.findViewById(R.id.cured);
          death = itemView.findViewById(R.id.death);
        }
    }


}