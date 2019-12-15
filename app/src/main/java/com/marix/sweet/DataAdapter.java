package com.marix.sweet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DoskHolder> {

    private Context context;
    private List<Dosk> cakeList = new ArrayList<>();
    final String TAG = "Mes";

    public DataAdapter(Context c){
        context = c;
    }

    @Override
    public DoskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Создаем экземпляр view
        View v;
        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.dosk_view, parent, false);
        return new DoskHolder(v);
    }

    @Override
    public void onBindViewHolder(DoskHolder holder, int position) {
        final int p = position;
        Picasso.get().load(cakeList.get(position).photo).into(holder.photo);
        holder.name.setText(cakeList.get(position).name);
        holder.cakeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DoskDiscriptionActivity.class);
                Dosk c = cakeList.get(p);
                i.putExtra("name", c.name);
                i.putExtra("discription", c.discription);
                i.putExtra("weight", c.weight);
                i.putExtra("price", c.price);
                i.putExtra("photo", c.photo);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cakeList.size();
    }


    public void add(ArrayList<Dosk> c){
        cakeList = c;
        notifyDataSetChanged();
    }

    public static class DoskHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView photo;
        LinearLayout cakeLayout;
        public DoskHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nameDosk);
            photo = (ImageView) itemView.findViewById(R.id.doskImage);
            cakeLayout = (LinearLayout) itemView.findViewById(R.id.doskLayout);
        }
    }
}
