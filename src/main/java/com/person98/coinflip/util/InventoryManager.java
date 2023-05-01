package com.person98.coinflip.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.person98.coinflip.CoinFlip;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class InventoryManager {
    private FileConfiguration config = CoinFlip.getInstance().getConfig();

    private int size = this.config.getInt("GUI.Size");

    private String title = this.config.getString("GUI.Title");

    private HashMap<Player, CoinEntry> coins = CoinFlip.getInstance().getCoins().getEntry();

    private Inventory menu = Bukkit.createInventory(new CoinFlipInventoryHolder(Chat.color(this.title)), this.size, Chat.color(this.title));

    private Material material;

    public InventoryManager() {
        for (int i = 0; i < this.size; i++) {
            if (i != this.size - 5 && i != this.size - 4 && i != this.size - 6) {
                this.menu.setItem(i, grayGlassPane());
            }
        }
        this.menu.setItem(this.size - 5, playerRefresh());
        this.menu.setItem(this.size - 4, playerHelp());
        this.menu.setItem(this.size - 6, playerStats());
    }

    public Inventory getMenu() {
        return this.menu;
    }

    public int getSize() {
        return this.size;
    }

    public void updateInv() {
        int index = 0;
        // Set the inventory holder when creating a new inventory instance
        this.menu = Bukkit.createInventory(new CoinFlipInventoryHolder(Chat.color(this.title)), this.size, Chat.color(this.title));
        for (int i = 0; i < this.size; i++) {
            if (i != this.size - 5 && i != this.size - 4 && i != this.size - 6) {
                this.menu.setItem(i, grayGlassPane());
            }
        }
        for (Player p : this.coins.keySet()) {
            this.menu.setItem(index, playerBet(p));
            index++;
        }
        this.menu.setItem(this.size - 5, playerRefresh());
        this.menu.setItem(this.size - 4, playerHelp());
        this.menu.setItem(this.size - 6, playerStats());
    }

    public ItemStack playerBet(Player p) {
        DecimalFormat df = new DecimalFormat("#,###");
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short)3);
        SkullMeta meta = (SkullMeta)Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
        List<String> desc = this.config.getStringList("GUI.SkullItem.lore");
        List<String> lore = new ArrayList<>();
        for (String text : desc)
            lore.add(Chat.color(text.replaceAll("%money%", df.format(((CoinEntry)this.coins.get(p)).getAmount())).replaceAll("%side%", CoinFlip.getInstance().getCoins().getSideConverted(p))));
        meta.setDisplayName(Chat.color(this.config.getString("GUI.SkullItem.name").replaceAll("%name%", p.getName())));
        meta.setOwner(p.getName());
        meta.setLore(lore);
        item.setItemMeta((ItemMeta)meta);
        return item;
    }
    public ItemStack grayGlassPane() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§7");
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack playerHelp() {
        String type = this.config.getString("GUI.BookInfo.type");
        this.material = Material.getMaterial(type);
        ItemStack item = new ItemStack(this.material, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> desc = this.config.getStringList("GUI.BookInfo.lore");
        List<String> lore = new ArrayList<>();
        for (String text : desc)
            lore.add(Chat.color(text));
        meta.setDisplayName(Chat.color(this.config.getString("GUI.BookInfo.name")));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack playerStats() {
        String type = this.config.getString("GUI.StatsItem.type");
        this.material = Material.getMaterial(type);
        ItemStack item = new ItemStack(this.material, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> desc = this.config.getStringList("GUI.StatsItem.lore");
        List<String> lore = new ArrayList<>();
        for (String text : desc)
            lore.add(Chat.color(text));
        meta.setDisplayName(Chat.color(this.config.getString("GUI.StatsItem.name")));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack playerRefresh() {
        String type = this.config.getString("GUI.RefreshItem.type");
        this.material = Material.getMaterial(type);
        ItemStack item = new ItemStack(this.material, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> desc = this.config.getStringList("GUI.RefreshItem.lore");
        List<String> lore = new ArrayList<>();
        for (String text : desc)
            lore.add(Chat.color(text));
        meta.setDisplayName(Chat.color(this.config.getString("GUI.RefreshItem.name")));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}