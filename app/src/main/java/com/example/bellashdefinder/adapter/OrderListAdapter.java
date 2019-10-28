package com.example.bellashdefinder.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.model.Order;
import com.example.bellashdefinder.model.Product;
import com.example.bellashdefinder.util.DataSet;

import java.util.ArrayList;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    private List<Order> dataSet;
    private OnClickListener onClickListener;

    public OrderListAdapter(List<Order> dataSet, OnClickListener onClickListener) {
        this.dataSet = dataSet;
        this.onClickListener = onClickListener;
    }

    public void setDataSet(List<Order> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Order order = dataSet.get(holder.getAdapterPosition());

        if (order == null) {
            return;
        }

        order.setCustomer(DataSet.getCustomerById(order.getCustomerId()));

        if (order.getCustomer() == null) {
            return;
        }

        final List<Product> productList = new ArrayList<>();

        order.setProductList(productList);

        for (String id : order.getProductIdList()) {
            productList.add(DataSet.getProductById(id));
        }

        holder.tvCustomer.setText(order.getCustomer().getName());

        StringBuilder productNameStr = new StringBuilder();
        for (Product product : productList) {
            if (!TextUtils.isEmpty(productNameStr)) {
                productNameStr.append(", ");
            }

            productNameStr.append(product.getName());
        }

        holder.tvProduct.setText(productNameStr);
        holder.tvAddress.setText(order.getCustomer().getAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public interface OnClickListener {
        void onClick(Order order);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView tvCustomer, tvProduct, tvAddress;

        ViewHolder(@NonNull View v) {
            super(v);

            tvCustomer = v.findViewById(R.id.tv_customer);
            tvProduct = v.findViewById(R.id.tv_product);
            tvAddress = v.findViewById(R.id.tv_address);
        }
    }
}
