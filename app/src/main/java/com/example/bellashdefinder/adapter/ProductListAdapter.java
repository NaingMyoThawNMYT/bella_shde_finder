package com.example.bellashdefinder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bellashdefinder.R;
import com.example.bellashdefinder.model.Product;
import com.example.bellashdefinder.util.BitmapUtil;
import com.example.bellashdefinder.util.DataSet;
import com.example.bellashdefinder.util.NumberUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> implements Filterable {
    private List<Product> dataSet;
    private List<Product> filteredDataSet;
    private OnClickListener onClickListener;
    private StorageReference storageRef;

    public ProductListAdapter(List<Product> dataSet, OnClickListener onClickListener) {
        this.dataSet = dataSet;
        this.filteredDataSet = dataSet;
        this.onClickListener = onClickListener;

        storageRef = FirebaseStorage.getInstance().getReference();
    }

    public void setDataSet(List<Product> dataSet) {
        this.dataSet = dataSet;
        this.filteredDataSet = dataSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int p) {
        final Product product = filteredDataSet.get(holder.getAdapterPosition());

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

        holder.imgView.setImageBitmap(null);

        if (DataSet.photos.get(product.getId()) == null) {
            storageRef.child(product.getId()).getBytes(Long.MAX_VALUE).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                @Override
                public void onComplete(@NonNull Task<byte[]> task) {
                    if (task.isSuccessful()) {
                        DataSet.photos.put(product.getId(), BitmapUtil.byteArrayToBitmap(task.getResult()));
                        holder.imgView.setImageBitmap(DataSet.photos.get(product.getId()));
                    }
                }
            });
        } else {
            holder.imgView.setImageBitmap(DataSet.photos.get(product.getId()));
        }
    }

    @Override
    public int getItemCount() {
        return filteredDataSet == null ? 0 : filteredDataSet.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchTerm = charSequence.toString().trim().toLowerCase();

                final List<Product> filteredList = new ArrayList<>();
                if (searchTerm.isEmpty()) {
                    filteredList.addAll(new ArrayList<>(dataSet));
                } else {
                    for (Product product : dataSet) {
                        if (product.getCategory().toLowerCase().contains(searchTerm.toLowerCase())) {
                            filteredList.add(product);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredDataSet = (List<Product>) filterResults.values;

                notifyDataSetChanged();
            }
        };
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
