package com.example.warehouseinventory;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warehouseinventory.data.ItemRepository;
import com.example.warehouseinventory.model.InventoryItem;
import com.example.warehouseinventory.ui.InventoryAdapter;
import com.google.android.material.button.MaterialButton;

public class InventoryActivity extends AppCompatActivity implements InventoryAdapter.Listener {

    private static final int REQ_SMS = 2001;

    private ItemRepository itemRepo;
    private InventoryAdapter adapter;

    private EditText etItemName;
    private EditText etSku;
    private EditText etQty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_inventory);

        itemRepo = new ItemRepository(this);

        RecyclerView rv = findViewById(R.id.rvInventory);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new InventoryAdapter(this);
        rv.setAdapter(adapter);

        etItemName = findViewById(R.id.etItemName);
        etSku = findViewById(R.id.etSku);
        etQty = findViewById(R.id.etQty);

        MaterialButton btnAdd = findViewById(R.id.btnAddItem);
        btnAdd.setOnClickListener(v -> addItem());

        MaterialButton btnSmsSettings = findViewById(R.id.btnSmsSettings);
        btnSmsSettings.setOnClickListener(v ->
                startActivity(new Intent(InventoryActivity.this, SmsActivity.class)));

        refreshGrid();
    }

    private void refreshGrid() {
        adapter.submit(itemRepo.getAllItems());
    }

    private void addItem() {
        String name = etItemName.getText().toString().trim();
        String sku = etSku.getText().toString().trim();
        String qtyStr = etQty.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(sku) || TextUtils.isEmpty(qtyStr)) {
            Toast.makeText(this, "Enter item name, SKU, and quantity.", Toast.LENGTH_SHORT).show();
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(qtyStr);
        } catch (NumberFormatException ex) {
            Toast.makeText(this, "Quantity must be a number.", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = itemRepo.addItem(name, sku, qty);
        if (id == -1) {
            Toast.makeText(this, "Could not add item.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (qty <= 0) {
            maybeSendLowStockSms(name, sku);
        }

        etItemName.setText("");
        etSku.setText("");
        etQty.setText("");

        refreshGrid();
    }

    @Override
    public void onDeleteClicked(InventoryItem item) {
        boolean ok = itemRepo.deleteItem(item.id);
        if (ok) {
            Toast.makeText(this, "Deleted.", Toast.LENGTH_SHORT).show();
            refreshGrid();
        } else {
            Toast.makeText(this, "Delete failed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onQtyClicked(InventoryItem item) {
        final EditText input = new EditText(this);
        input.setHint("New quantity");
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        new AlertDialog.Builder(this)
                .setTitle("Update Quantity")
                .setMessage("Set a new quantity for: " + item.name)
                .setView(input)
                .setPositiveButton("Update", (d, which) -> {
                    String txt = input.getText().toString().trim();
                    if (TextUtils.isEmpty(txt)) {
                        Toast.makeText(this, "Quantity required.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int newQty;
                    try {
                        newQty = Integer.parseInt(txt);
                    } catch (NumberFormatException ex) {
                        Toast.makeText(this, "Quantity must be a number.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    boolean ok = itemRepo.updateQty(item.id, newQty);
                    if (ok) {
                        if (newQty <= 0) {
                            maybeSendLowStockSms(item.name, item.sku);
                        }
                        refreshGrid();
                    } else {
                        Toast.makeText(this, "Update failed.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void maybeSendLowStockSms(String itemName, String sku) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        String destination = "5554";

        String msg = "Low Inventory: " + itemName + " (" + sku + ") is out of stock.";
        try {
            android.telephony.SmsManager.getDefault().sendTextMessage(destination, null, msg, null, null);
            Toast.makeText(this, "SMS alert sent.", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(this, "SMS failed: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}