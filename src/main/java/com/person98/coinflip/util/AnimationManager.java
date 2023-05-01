package com.person98.coinflip.util;


import com.person98.coinflip.CoinFlip;
import java.text.DecimalFormat;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AnimationManager {
    private FileConfiguration config = CoinFlip.getInstance().getConfig();

    private String title = this.config.getString("AnimationGUI.Title");
    private boolean animationRunning = false;
    private boolean isAnimationStopped = false;

    public boolean isAnimationStopped() {
        return isAnimationStopped;
    }

    public void setAnimationStopped(boolean animationStopped) {
        isAnimationStopped = animationStopped;
    }
    public boolean isAnimationRunning() {
        return animationRunning;
    }

    public String getTitle() {
        return Chat.color(this.title);
    }
    public ItemStack players(Player winner) {
        ItemStack p1 = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
        meta.setDisplayName(Chat.color("&f&l") + winner.getName());
        meta.setOwner(winner.getName());
        p1.setItemMeta((ItemMeta) meta);
        return p1;
    }

    public void setAnimation(final Player p, final Player winner, final Player loser, final double amount) {
        animationRunning = true;
        isAnimationStopped = false;

        final DecimalFormat df = new DecimalFormat("#,###");
        final Inventory animation = Bukkit.createInventory((InventoryHolder) null, 27, Chat.color(this.title));

        // Add colored glass panes to inventory
        for (int i = 0; i < 27; i++) {
            Material[] glassTypes = {Material.RED_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE,
                    Material.GREEN_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE, Material.LIGHT_BLUE_STAINED_GLASS_PANE,
                    Material.BLUE_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS_PANE};
            Random random = new Random();
            Material glassPane = glassTypes[random.nextInt(glassTypes.length)];
            short color = (short) random.nextInt(16);
            ItemStack glass = new ItemStack(glassPane, 1, color);
            ItemMeta meta = glass.getItemMeta();
            meta.setDisplayName(" ");
            glass.setItemMeta(meta);
            animation.setItem(i, glass);
        }

        // Add player heads
        animation.setItem(13, players(loser));
        p.openInventory(animation);

        (new BukkitRunnable() {
            int counter = 0;
            int counter2 = 1;
            int counter3 = 1;

            public void run() {
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
                if (this.counter % this.counter2 == 0) {
                    this.counter = 0;
                    this.counter2++;
                }
                if (this.counter3 == 1) {
                    animation.setItem(13, AnimationManager.this.players(loser));
                    p.updateInventory();
                    this.counter3--;
                } else {
                    animation.setItem(13, AnimationManager.this.players(winner));
                    p.updateInventory();
                    this.counter3++;
                }

                // Change the color of the glass panes very fast
                if (counter % 2 == 0) {
                    for (int i = 0; i < 27; i++) {
                        if (animation.getItem(i).getType().name().contains("STAINED_GLASS_PANE")) {
                            Material[] glassTypes = {Material.RED_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE,
                                    Material.GREEN_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE, Material.LIGHT_BLUE_STAINED_GLASS_PANE,
                                    Material.BLUE_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS_PANE};
                            Random random = new Random();
                            Material glassPane = glassTypes[random.nextInt(glassTypes.length)];
                            short color = (short) random.nextInt(16);
                            ItemStack glass = new ItemStack(glassPane, 1, color);
                            ItemMeta meta = glass.getItemMeta();
                            meta.setDisplayName(" ");
                            glass.setItemMeta(meta);
                            animation.setItem(i, glass);
                        }
                    }
                }

                this.counter++;
                if (this.counter2 > 6) {
                    isAnimationStopped = true;

                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                    animation.setItem(13, AnimationManager.this.players(winner));
                    p.updateInventory();
                    CoinFlip.getEconomy().depositPlayer((OfflinePlayer) winner, amount);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!CoinFlip.getInstance().getBroadcast().inEntry(p))
                            p.sendMessage(Chat.color(CoinFlip.getInstance().getConfig().getString("Messages.WinBroadcast").replaceAll("%winner%", winner.getName()).replaceAll("%loser%", loser.getName()).replaceAll("%amount%", df.format(amount))));
                        animationRunning = false;

                    }

                    // Close the inventory after the animation ends
                    new BukkitRunnable() {
                        public void run() {
                            p.closeInventory();
                        }
                    }.runTaskLater(CoinFlip.getInstance(), 40L);

                    cancel();
                }
            }
        }).runTaskTimer((Plugin) CoinFlip.getInstance(), 0L, 1L);
    }
}
