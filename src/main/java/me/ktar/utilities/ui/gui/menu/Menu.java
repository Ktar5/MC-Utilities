package me.ktar.utilities.ui.gui.menu;
/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 * 
 * This file is part of Utilities.
 * 
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.ktar.utilities.ui.gui.item.MenuItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Getter
@Accessors(fluent = true, chain = true)
public abstract class Menu<T extends Menu<T>> {

    /**
     * The inventory that will be shown to the player
     */
    protected final Inventory inventory;
    /**
     * The MenuItems and their corresponding slot numbers (Starting at 0)
     * Note: empty slots in an inventory are not in the map
     */
    private final Map<Integer, MenuItem> items;
    private final Set<MenuEvent> listeners;
    /**
     * The name of the menu displayed as the title
     * on the inventory, when opened
     */
    protected String name;
    /**
     * The menu that serves as the parent
     * Aka this menu is opened when the current menu is closed
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected Function<Player, Menu> parent;

    protected Menu(String name, Size size) {
        this.items = new HashMap<>();
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        this.inventory = Bukkit.createInventory(new MenuHolder<>(getThis()), (size.ordinal() + 1) * 9, this.name);
        this.listeners = new HashSet<>();
    }

    protected Menu(String name, int size) {
        this.items = new HashMap<>();
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        this.inventory = Bukkit.createInventory(new MenuHolder<>(getThis()), size * 9, this.name);
        this.listeners = new HashSet<>();
    }

    public T setName(String name){
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        return getThis();
    }

    public static int toIndex(int x, int y) {
        y -= 1;
        x -= 1;
        return (y * 9) + x;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public T setParent(Function<Player, Menu> function) {
        this.parent = function;
        return getThis();
    }

    public Menu getParent(Player player) {
        return parent.apply(player);
    }

    /**
     * Returns the item at specified index
     *
     * @param index (starts at 0)
     * @return Found item
     */
    public MenuItem itemAt(int index) {
        return items.get(index);
    }

    /**
     * Returns the item at specified coordinates, where x is on the horizontal axis and z is on the vertical axis.
     *
     * @param x The x coordinate (starts at 0)
     * @param y The y coordinate (starts at 0)
     * @return Found item
     */
    public MenuItem itemAt(int x, int y) {
        return items.get(toIndex(x, y));
    }

    /**
     * Sets the item at the specified index
     *
     * @param index Index of the item you wish to set
     * @param item  The item you wish to set the index as
     */
    public T setItem(int index, MenuItem item) {
        if (item == null) {
            inventory.setItem(index, null);
        } else {
            inventory.setItem(index, item.stack());
        }
        items.put(index, item);
        return getThis();
    }

    public boolean use(int slot, Player player, ClickType type) {
        MenuItem item = items.get(slot);
        if (item == null) {
            return true;
        }
        return item.checkAct(player, type);
    }

    /**
     * Sets the item at the specified coordinates, where x is on the horizontal axis and z is on the vertical axis.
     *
     * @param x    The x coordinate (starts at 0)
     * @param y    The y coordinate (starts at 0)
     * @param item The item you wish to set the index as
     */
    public T setItem(int x, int y, MenuItem item) {
        return setItem(toIndex(x, y), item);
    }

    public boolean call(Event event) {
        if (listeners.isEmpty()) {
            return true;
        }
        for (MenuEvent listener : listeners) {
            if (event instanceof InventoryClickEvent) {
                return listener.onMenuClick(this, (InventoryClickEvent) event);
            } else if (event instanceof InventoryDragEvent) {
                return listener.onMenuDrag(this, (InventoryDragEvent) event);
            } else if (event instanceof InventoryCloseEvent) {
                return listener.onMenuClose(this, (InventoryCloseEvent) event);
            }
        }
        return false;
    }

    /**
     * Adds a listener to this menu.
     *
     * @param listener
     */
    public void addListener(MenuEvent listener) {
        listeners.add(listener);
    }

    /**
     * Show the menu to the inputted players
     *
     * @param player The players you wish to show the menu to
     */

    public abstract void show(Player player);

    public abstract T getThis();

    public enum Size {
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
    }

    public static class MenuEvent {
        public boolean onMenuClick(Menu menu, InventoryClickEvent event) {
            return true;
        }

        public boolean onMenuDrag(Menu menu, InventoryDragEvent event) {
            return true;
        }

        public boolean onMenuClose(Menu menu, InventoryCloseEvent event) {
            return true;
        }
    }
}