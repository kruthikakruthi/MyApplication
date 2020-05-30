package com.example.myapplication.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Interface.ItemClickListener;
import com.example.myapplication.R;

public class TripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtName,txtDescription,price;
    public ImageView imageView;
    public ItemClickListener listener;





    public TripViewHolder(@NonNull View itemView)
    {
        super(itemView);

        imageView=(ImageView) itemView.findViewById(R.id.featured_image);
        txtName=(TextView) itemView.findViewById(R.id.featured_title);

      price=(TextView)itemView.findViewById(R.id.featured_price);

    }
    public void  setItemClickListener(ItemClickListener listener)
    {

        this.listener=listener;

    }

    @Override
    public void onClick(View view) {

        listener.onclick(view,getAdapterPosition(),false);
    }
}
