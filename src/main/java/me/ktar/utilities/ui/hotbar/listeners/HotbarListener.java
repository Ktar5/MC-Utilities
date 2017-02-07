package me.ktar.utilities.ui.hotbar.listeners;
/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 * 
 * This file is part of Utilities.
 * 
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

import me.ktar.utilities.Utilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class HotbarListener implements Listener {
    private final Utilities utilities;

    public HotbarListener(Utilities utilities){
        this.utilities = utilities;
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        if (!event.getPlayer().isOp() && utilities.getHotbarCoordinator().hasHotbar(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onTryMove(InventoryClickEvent event) {
        if (!event.getWhoClicked().isOp() && utilities.getHotbarCoordinator().hasHotbar((Player) event.getWhoClicked())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onTryDrag(InventoryDragEvent event) {
        if (!event.getWhoClicked().isOp() && utilities.getHotbarCoordinator().hasHotbar((Player) event.getWhoClicked())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL && utilities.getHotbarCoordinator().hasHotbar(event.getPlayer())) {
            if (event.hasItem() || event.hasBlock()) {
                if (!event.getPlayer().isOp()) {
                    event.setCancelled(true);
                }

                utilities.getHotbarCoordinator().use(event.getPlayer(),
                        event.getPlayer().getInventory().getHeldItemSlot());
            }
        }
    }
}
