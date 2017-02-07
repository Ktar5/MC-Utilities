package me.ktar.utilities.ui.gui.menu;
/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 * 
 * This file is part of Utilities.
 * 
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public final class MenuHolder<T extends Menu<T>> implements InventoryHolder {

    @Getter
    private final T menu;

    MenuHolder(T menu) {
        this.menu = menu;
    }

    @Override
    public Inventory getInventory() {
        throw new UnsupportedOperationException("This method is not implemented" +
                "\n(Because you should use MenuHolder#getMenu)" +
                "\nSorry-not-sorry ¯\\_(ツ)_/¯");
    }
}
