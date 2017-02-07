package me.ktar.utilities.ui.hotbar;
/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 * 
 * This file is part of Utilities.
 * 
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public abstract class HotbarItem {

    public static final HotbarItem AIR = new HotbarItem(new ItemStack(Material.AIR)) {
        @Override
        public void click(Player player) {
        }
    };
    final ItemStack stack;

    abstract public void click(Player player);

    public final class Icon extends HotbarItem {
        public Icon(Material material) {
            super(new ItemStack(material));
        }

        @Override
        public void click(Player player) {
        }
    }

}
