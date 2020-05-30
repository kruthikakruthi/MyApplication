package com.example.myapplication.Holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Interface.ItemClickListener;
import com.example.myapplication.R;

public class TripListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView txttriplistname, txttriplistaddress, txttriplistpeoples,txttriplistprice;
    private ItemClickListener itemClickListener;

    public TripListViewHolder(@NonNull View itemView) {
        super(itemView);
        txttriplistname = itemView.findViewById(R.id.triplist_name);
        txttriplistaddress = itemView.findViewById(R.id.triplist_address_design);
        txttriplistpeoples = itemView.findViewById(R.id.triplist_no_of_peoples);
        txttriplistprice=itemView.findViewById(R.id.triplist_price_design);
    }

    @Override
    public void onClick(View view) {

itemClickListener.onclick(view,getAdapterPosition(),false);
    }

    public  void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener=itemClickListener;
    }
}
