package com.person98.coinflip.util;

import java.util.HashMap;

import com.person98.coinflip.CoinFlip;
import org.bukkit.entity.Player;

public class BroadcastManager {
    private HashMap<Player, Boolean> broadcast = new HashMap<>();

    public boolean inEntry(Player p) {
        return this.broadcast.containsKey(p);
    }

    public void createEntry(Player p) {
        this.broadcast.put(p, Boolean.valueOf(true));
    }

    public void removeEntry(Player p) {
        this.broadcast.remove(p);
    }

    public String toString(Player p) {
        if (inEntry(p))
            return CoinFlip.getInstance().getConfig().getString("Messages.ToggleON");
        return CoinFlip.getInstance().getConfig().getString("Messages.ToggleOFF");
    }
}
