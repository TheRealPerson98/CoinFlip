package com.person98.coinflip.events;

import com.person98.coinflip.CoinFlip;
import com.person98.coinflip.util.CoinEntry;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerQuitEvent implements Listener {
    @EventHandler
    public void onQuitEvent(org.bukkit.event.player.PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (CoinFlip.getInstance().getCoins().inEntry(p)) {
            CoinFlip.getEconomy().depositPlayer((OfflinePlayer)p, ((CoinEntry)CoinFlip.getInstance().getCoins().getEntry().get(p)).getAmount());
            CoinFlip.getInstance().getCoins().removeEntry(p);
            CoinFlip.getInstance().getMenuManager().updateInv();
        }
    }
}
