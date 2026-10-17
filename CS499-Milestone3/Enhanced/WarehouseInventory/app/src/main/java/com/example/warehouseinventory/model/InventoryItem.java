package com.example.warehouseinventory.model;

public class InventoryItem {
    public long id;
    public String name;
    public String sku;
    public int qty;

    public InventoryItem(long id, String name, String sku, int qty) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.qty = qty;
    }
}