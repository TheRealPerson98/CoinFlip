package com.person98.coinflip.events;

import com.person98.coinflip.util.AnimationManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class AnimationManagerListener implements Listener {

    private AnimationManager animationManager;

    public AnimationManagerListener(AnimationManager animationManager) {
        this.animationManager = animationManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if ((animationManager.isAnimationRunning() || animationManager.isAnimationStopped()) && event.getView().getTitle().equals(animationManager.getTitle())) {
            event.setCancelled(true);
        }
    }
}
