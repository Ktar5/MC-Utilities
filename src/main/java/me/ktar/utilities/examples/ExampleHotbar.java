package me.ktar.utilities.examples;
/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 * 
 * This file is part of Utilities.
 * 
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

import me.ktar.utilities.misc.ItemFactory;
import me.ktar.utilities.misc.StringUtil;
import me.ktar.utilities.ui.gui.item.MenuItem;
import me.ktar.utilities.ui.gui.menu.Menu;
import me.ktar.utilities.ui.gui.menu.StaticMenu;
import me.ktar.utilities.ui.hotbar.Hotbar;
import me.ktar.utilities.ui.hotbar.HotbarItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

//@StaticHotbar
public final class ExampleHotbar extends Hotbar {

    static final HotbarItem COSMETICS = new HotbarItem(
            new ItemFactory(Material.ENDER_CHEST).setDisplayName("&cCosmetics -- Coming Soon!").getItemStack()) {
        @Override
        public void click(Player player) {
            player.sendMessage(StringUtil.colorString("&cComing Soon!!"));
        }
    };

    static final HotbarItem SERVER_SELECTOR = new HotbarItem(
            new ItemFactory(Material.COMPASS).setDisplayName("&fServer Selector").getItemStack()) {
        StaticMenu menu = (StaticMenu) new StaticMenu("Server Selector", Menu.Size.ONE)
                .setItem(5, new MenuItem(new ItemStack(Material.GRASS)) {
                    @Override
                    public void act(Player player, ClickType clickType) {
                        //TODO
                        //ServerSelector.sendPlayer("skyblock", player, ThreadLocalRandom.current().nextInt(0, Bukkit.getOnlinePlayers().size() / 5));
                    }
                });

        @Override
        public void click(Player player) {
            menu.show(player);
        }
    };

    static final HotbarItem VOTE = new HotbarItem(
            new ItemFactory(Material.BOOK_AND_QUILL).setDisplayName("&fVoting Info").getItemStack()) {
        @Override
        public void click(Player player) {
            player.sendMessage(StringUtil.colorString("&cComing Soon!!"));
        }
    };

    public ExampleHotbar() {
        super(SERVER_SELECTOR, HotbarItem.AIR, HotbarItem.AIR, COSMETICS, HotbarItem.AIR, HotbarItem.AIR, VOTE, HotbarItem.AIR, HotbarItem.AIR);
    }

}
