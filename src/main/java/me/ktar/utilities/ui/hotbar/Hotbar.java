package me.ktar.utilities.ui.hotbar;
/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 * 
 * This file is part of Utilities.
 * 
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

import org.bukkit.entity.Player;

public abstract class Hotbar {

    /**
     * Represents the name of this Hotbar for use in the coordinator
     */
    final String name = this.getClass().getSimpleName().toLowerCase();
    private final HotbarItem[] items;

    /**
     * Represents a Hotbar shown to a player (the 9 slots that a player sees
     * while they are outside their inventory)
     *
     * @param items an array of HotbarItem(s) with a length of 9
     */
    protected Hotbar(HotbarItem... items) {
        if (items.length != 9) throw new ArrayIndexOutOfBoundsException();
        this.items = items;
    }

    /**
     * Player uses the item in Slot by doing an Action
     *
     * @param player The player who used the item
     * @param slot   The slot in the hotbar that was acted upon
     */
    public void use(Player player, int slot) {
        if (slot <= 8 && slot >= 0) {
            items[slot].click(player);
        }
    }

    /**
     * Update the player's inventory to have these items in it
     *
     * @param player The player whose inventory will be updated
     */
    public void send(Player player) {
        for (int i = 0; i < items.length; i++) {
            player.getInventory().setItem(i, items[i].stack.clone());
        }
        player.updateInventory();
    }


}

