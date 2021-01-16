package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListener;
import com.example.ecommerce.R;


public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView txtProductName, txtProductDescription, txtProductPrice , txtProductStatus;
    public ImageView imageView;
    public ItemClickListener listener;




    public ItemViewHolder(View itemView)
    {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.product_seller_image);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_seller_description);
        txtProductName = (TextView) itemView.findViewById(R.id.product_seller_name);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_seller_price);
        txtProductStatus = (TextView) itemView.findViewById(R.id.product_state);

    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;

    }


    @Override
    public void onClick(View view)
    {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
