package com.person98.coinflip.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CoinFlipInventoryHolder implements InventoryHolder {

    private String title;

    public CoinFlipInventoryHolder(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
