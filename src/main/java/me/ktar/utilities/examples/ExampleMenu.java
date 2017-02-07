package me.ktar.utilities.examples;
/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 * 
 * This file is part of Utilities.
 * 
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

import me.ktar.utilities.ui.gui.item.MenuIcon;
import me.ktar.utilities.ui.gui.item.MenuItem;
import me.ktar.utilities.ui.gui.menu.DynamicMenu;
import me.ktar.utilities.ui.gui.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ExampleMenu {

    public static void example() throws InterruptedException {
        DynamicMenu menu = new DynamicMenu("YoloSwag2k{year}", Menu.Size.FIVE)
                .setItem(0, 1,
                        MenuItem.create(new ItemStack(Material.APPLE), (player, clickType) -> {
                            if (player.getName().equalsIgnoreCase("ktar5") && clickType == ClickType.LEFT) {
                                System.out.println("Ktar5 did a left click");
                            }
                        })
                ).setItem(0, 2, new MenuIcon(Material.WOOD))
                .setItem(0, 0, new MenuItem(new ItemStack(Material.EYE_OF_ENDER)) {
                    @Override
                    public void act(Player player, ClickType clickType) {
                        System.out.println("You have touched the enderchest, congratulations");
                    }
                });
        menu.show(Bukkit.getPlayer("Ktar5"));
        Thread.sleep(2000);
        menu.modify().change(0, 1, item -> item.setDisplayName("I have been changed"));
    }

}
