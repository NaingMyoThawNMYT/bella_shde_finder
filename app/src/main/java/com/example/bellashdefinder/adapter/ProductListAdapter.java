package com.example.bellashdefinder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.model.Product;
import com.example.bellashdefinder.util.NumberUtil;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private List<Product> dataSet;
    private OnClickListener onClickListener;

    public ProductListAdapter(List<Product> dataSet, OnClickListener onClickListener) {
        this.dataSet = dataSet;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int p) {
        final Product product = dataSet.get(holder.getAdapterPosition());

        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(String.valueOf(NumberUtil.getOneDigit(product.getPrice())));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(product);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClickListener.onLongClick(product);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public interface OnClickListener {
        void onClick(Product product);

        void onLongClick(Product product);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgView;
        private AppCompatTextView tvName;
        private AppCompatTextView tvPrice;

        ViewHolder(@NonNull View v) {
            super(v);

            imgView = v.findViewById(R.id.img_v_product);
            tvName = v.findViewById(R.id.tv_product_name);
            tvPrice = v.findViewById(R.id.tv_product_price);
        }
    }
}
