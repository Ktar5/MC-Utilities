package me.ktar.utilities.ui.gui.item;
/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 *
 * This file is part of Utilities.
 *
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public abstract class
MenuItem {
    @Getter
    @Setter
    @Accessors(fluent = true)
    ItemStack stack;

    protected MenuItem(ItemStack stack) {
        this.stack = stack;
    }

    protected MenuItem(Material material) {
        this(new ItemStack(material));
    }


    public static MenuItem create(ItemStack item, BiConsumer<Player, ClickType> onclick) {
        return new MenuItem(item) {
            @Override
            public void act(Player player, ClickType clickType) {
                onclick.accept(player, clickType);
            }
        };
    }

    public static MenuItem create(ItemStack item, BiConsumer<Player, ClickType> onclick, BiPredicate<Player, ClickType> canUse) {
        return new MenuItem(item) {
            @Override
            public boolean canUse(Player player, ClickType clickType) {
                return canUse.test(player, clickType);
            }

            @Override
            public void act(Player player, ClickType clickType) {
                onclick.accept(player, clickType);
            }
        };
    }

    public final boolean checkAct(Player player, ClickType type) {
        boolean used = canUse(player, type);
        if (used) {
            act(player, type);
        }
        return used;
    }

    public boolean canUse(Player player, ClickType type) {
        return true;
    }

    public abstract void act(Player player, ClickType clickType);
}
