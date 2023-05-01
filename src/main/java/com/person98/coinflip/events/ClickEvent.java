package com.person98.coinflip.events;

import com.person98.coinflip.CoinFlip;
import com.person98.coinflip.util.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ClickEvent implements Listener {
    private boolean animationRunning = false;

    @EventHandler
    public void onClickEvent(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        Inventory open = e.getClickedInventory();
        ItemStack item = e.getCurrentItem();
        Economy econ = CoinFlip.getEconomy();
        CoinManager coins = CoinFlip.getInstance().getCoins();
        InventoryManager menu = CoinFlip.getInstance().getMenuManager();
        if (item == null || !item.hasItemMeta())
            return;

        if (!(open.getHolder() instanceof CoinFlipInventoryHolder)) {
            return;
        }
        if (animationRunning) {
            e.setCancelled(true);
            return;
        }
        e.setCancelled(true);

        if (open != null && (open.getHolder() instanceof CoinFlipInventoryHolder && ((CoinFlipInventoryHolder) open.getHolder()).getTitle().equals(((CoinFlipInventoryHolder) CoinFlip.getInstance().getMenu().getHolder()).getTitle()) || ((CoinFlipInventoryHolder) open.getHolder()).getTitle().equals(Chat.color(CoinFlip.getInstance().getConfig().getString("AnimationGUI.Title"))))) {
            if (item.equals(menu.playerRefresh())) {
                p.closeInventory();
                p.openInventory(menu.getMenu());
                return;
            }
            Player other = Bukkit.getServer().getPlayer(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
            if (item.equals(menu.playerHelp()))
                return;
            if (coins.getEntry().containsKey(p))
                return;
            if (item.getItemMeta().getDisplayName().equals(Chat.color("&0.")))
                return;
            if (item.getType().equals(Material.PLAYER_HEAD)) {
                if (coins.inEntry(other)) {
                    double amount = ((CoinEntry)coins.getEntry().get(other)).getAmount();
                    if (econ.getBalance((OfflinePlayer)p) > amount) {
                        econ.withdrawPlayer((OfflinePlayer)p, amount);
                        p.closeInventory();
                        coins.removeEntry(other);
                        // Pass the betAmount to the getWinner method
                        Player winner = CoinFlip.getInstance().getStats().getWinner(p, other);
                        amount *= 2.0D;
                        if (p.equals(winner)) {
                            CoinFlip.getInstance().getAnimation().setAnimation(p, p, other, amount);
                        } else {
                            CoinFlip.getInstance().getAnimation().setAnimation(p, other, p, amount);
                        }
                        menu.updateInv();
                    } else {
                        p.closeInventory();
                        p.sendMessage(Chat.color(CoinFlip.getInstance().getConfig().getString("Messages.NotEnoughMoney")));
                    }
                }
            } else if (item.getItemMeta().getDisplayName().equals(Chat.color("&f&l" + other.getName()))) {
                return;
            }
        }
    }
}