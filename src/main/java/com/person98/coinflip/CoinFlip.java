package com.person98.coinflip;

import com.person98.coinflip.Commands.CoinFlipCommand;
import com.person98.coinflip.events.AnimationManagerListener;
import com.person98.coinflip.events.ClickEvent;
import com.person98.coinflip.events.PlayerQuitEvent;
import com.person98.coinflip.util.*;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class CoinFlip extends JavaPlugin {
    private CoinManager coins;

    private StatsManager stats;

    private InventoryManager menu;

    private AnimationManager animation;

    private BroadcastManager broadcast;
    public void onEnable() {
        saveDefaultConfig();
        if (!setupEconomy()) {
            System.out.println(String.format("[CoinFlip] - No Vault Found Please Install Vault", new Object[] { getDescription().getName() }));
            getServer().getPluginManager().disablePlugin((Plugin)this);
            return;
        }
        this.broadcast = new BroadcastManager();
        this.stats = new StatsManager();
        this.coins = new CoinManager();
        this.menu = new InventoryManager();
        this.animation = new AnimationManager();
        getServer().getPluginManager().registerEvents(new AnimationManagerListener(this.animation), this);
        CoinFlipCommand coinFlipCommand = new CoinFlipCommand();
        getCommand("coinflip").setExecutor(coinFlipCommand);
        getCommand("coinflip").setTabCompleter(coinFlipCommand);
        getServer().getPluginManager().registerEvents((Listener)new ClickEvent(), (Plugin)this);
        getServer().getPluginManager().registerEvents((Listener)new PlayerQuitEvent(), (Plugin)this);
    }

    public void onDisable() {}

    public static CoinFlip getInstance() {
        return (CoinFlip)JavaPlugin.getPlugin(CoinFlip.class);
    }

    public BroadcastManager getBroadcast() {
        return this.broadcast;
    }

    public StatsManager getStats() {
        return this.stats;
    }

    public CoinManager getCoins() {
        return this.coins;
    }

    public Inventory getMenu() {
        return this.menu.getMenu();
    }

    public AnimationManager getAnimation() {
        return this.animation;
    }

    public InventoryManager getMenuManager() {
        return this.menu;
    }

    public static Economy getEconomy() {
        return econ;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        econ = (Economy)rsp.getProvider();
        return (econ != null);
    }

    private static Economy econ = null;
}