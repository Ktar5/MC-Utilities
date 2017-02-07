package me.ktar.utilities.ui.gui.menu;
/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 * 
 * This file is part of Utilities.
 * 
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

import org.bukkit.entity.Player;

public class StaticMenu extends Menu<StaticMenu> {
    public StaticMenu(String name, Size size) {
        super(name, size);
    }

    public StaticMenu(String name, int size) {
        super(name, size);
    }

    @Override
    public void show(Player player) {
        player.openInventory(this.inventory);
    }

    @Override
    public StaticMenu getThis() {
        return this;
    }
}
