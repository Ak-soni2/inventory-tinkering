package com.example.tinkering;

public class Item {
    private String name;
    private int quantity;
    private String itemuid;

    public Item() {
        // Default constructor required for Firebase
    }

    public Item(String name, int quantity, String itemuid) {
        this.name = name;
        this.quantity = quantity;
        this.itemuid = itemuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemuid() {
        return itemuid;
    }

    public void setItemuid(String itemuid) {
        this.itemuid = itemuid;
    }
}
