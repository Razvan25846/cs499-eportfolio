package com.example.warehouseinventory.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warehouseinventory.R;
import com.example.warehouseinventory.model.InventoryItem;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.VH> {

    public interface Listener {
        void onDeleteClicked(InventoryItem item);
        void onQtyClicked(InventoryItem item);
    }

    private final Listener listener;
    private final List<InventoryItem> items = new ArrayList<>();

    public InventoryAdapter(Listener listener) {
        this.listener = listener;
    }

    public void submit(List<InventoryItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_inventory_item, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        InventoryItem item = items.get(position);

        h.tvItem.setText(item.name);
        h.tvSku.setText(item.sku);
        h.tvQty.setText(String.valueOf(item.qty));
        h.tvStatus.setText(item.qty <= 0 ? "Out" : "In Stock");

        h.btnDelete.setOnClickListener(v -> listener.onDeleteClicked(item));
        h.tvQty.setOnClickListener(v -> listener.onQtyClicked(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvItem, tvSku, tvQty, tvStatus;
        MaterialButton btnDelete;

        VH(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tvRowItem);
            tvSku = itemView.findViewById(R.id.tvRowSku);
            tvQty = itemView.findViewById(R.id.tvRowQty);
            tvStatus = itemView.findViewById(R.id.tvRowStatus);
            btnDelete = itemView.findViewById(R.id.btnRowDelete);
        }
    }
}