package me.ktar.utilities.ui.hotbar;
/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 * 
 * This file is part of Utilities.
 * 
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class HotbarCoordinator {
    private final Map<UUID, Hotbar> activeHotbars;
    private final Map<String, Hotbar> registeredHotbars;

    public HotbarCoordinator() {
        activeHotbars = new HashMap<>();
        registeredHotbars = new HashMap<>();
    }

    public void register(Hotbar hotbar) {
        if (this.registeredHotbars.containsKey(hotbar.getClass().getCanonicalName())) {
            throw new IllegalArgumentException();
        } else {
            registeredHotbars.put(hotbar.getClass().getCanonicalName(), hotbar);
            //System.out.println("Registered hotbar " + hotbar.getClass().getCanonicalName() + " successfully");
        }
    }

    public boolean hasHotbar(Player player){
        return activeHotbars.containsKey(player.getUniqueId());
    }

    public void unapply(Player player) {
        if (activeHotbars.containsKey(player.getUniqueId())) {
            for (int i = 0; i < 9; i++) {
                player.getInventory().setItem(i, new ItemStack(Material.AIR));
            }
            activeHotbars.remove(player.getUniqueId());
            player.updateInventory();
        }
    }

    public void apply(Player player, Class<? extends Hotbar> hotbar) {
        @Nonnull Hotbar bar = registeredHotbars.get(hotbar.getCanonicalName());
        activeHotbars.put(player.getUniqueId(), bar);
        bar.send(player);
    }

    public void use(Player player, int slot) {
        @Nonnull Hotbar bar = activeHotbars.get(player.getUniqueId());
        bar.use(player, slot);
    }

}
