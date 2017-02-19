package me.ktar.utilities.misc;
/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 *
 * This file is part of Utilities.
 *
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemFactory {
    private ItemStack itemStack;

    public ItemFactory(Material mat) {
        itemStack = new ItemStack(mat);
    }

    public ItemFactory(ItemStack stack) {
        itemStack = stack;
    }

    public static ItemFactory of(Material material){
        return new ItemFactory(material);
    }

    public static ItemFactory of(ItemStack stack){
        return new ItemFactory(stack);
    }

    public ItemMeta getMeta() {
        return itemStack.getItemMeta();
    }

    public ItemFactory setMeta(ItemMeta meta) {
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemFactory setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    public ItemFactory setLore(List<String> lore) {
        if (lore == null) return this;
        ItemMeta meta = getMeta();
        meta.setLore(StringUtil.colorList(lore));
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemFactory setLore(String lore) {
        ItemMeta meta = getMeta();
        List<String> lores = new ArrayList<>();

        lores.add(StringUtil.colorString(lore));
        meta.setLore(lores);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemFactory addLore(String lore) {
        ItemMeta meta = getMeta();
        List<String> lores = meta.getLore();

        if (lores == null) {
            lores = new ArrayList<>();
        }

        lores.add(lore);
        meta.setLore(lores);
        itemStack.setItemMeta(meta);
        return this;
    }

    public String getDisplayName() {
        return getMeta().getDisplayName();
    }

    public ItemFactory setDisplayName(String displayName) {
        if (displayName == null) {
            return this;
        }

        ItemMeta meta = getMeta();
        meta.setDisplayName(StringUtil.colorString(displayName));
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemFactory setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemFactory setMaterial(Material mat) {
        itemStack.setType(mat);
        return this;
    }

    public ItemFactory setMaterial(int materialId) {
        itemStack.setType(Material.getMaterial(materialId));
        return this;
    }

    /**
     * Sets the glow that you get when you enchant an item without enchanting
     * an item
     */
    public ItemFactory addGlow(boolean boo) {
        if (!boo) return this;
        this.addEnchantment(Enchantment.OXYGEN, 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Adds a map of enchantments to the item
     * (Doesn't have to be safe)
     *
     * @param enchantments The enchantment mapped to the integer level of the enchantment
     */
    public ItemFactory setEnchantments(Map<Enchantment, Integer> enchantments) {
        if (enchantments == null) {
            return this;
        }

        itemStack.addUnsafeEnchantments(enchantments);
        return this;
    }

    /**
     * Adds an unsafe enchantment to the itemstack
     *
     * @param enchant The enchantment to apply
     * @param level   The level of the enchantment
     */
    public ItemFactory addEnchantment(Enchantment enchant, Integer level) {
        itemStack.addUnsafeEnchantment(enchant, level);
        return this;
    }

    @Deprecated
    /**
     * Uses some random spigot method to set unbreakability of the itemstack
     * Note: Hasn't been tested yet, deprecated until testing occurs
     */
    public ItemFactory setUnbreakable() {
        ItemMeta meta = getMeta();
        meta.spigot().setUnbreakable(true);
        itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the durability, itemmeta, damage value, whatever you want to call it
     * of the item, limited to 32767... because minecraft
     *
     * @param durability the durability, with upper bound of 32767
     */
    public ItemFactory setDurability(int durability) {
        itemStack.setDurability((short) (durability > 32767 ? 32767 : durability));
        return this;
    }

    /**
     * @return the amount of players that own minecraft, casted to an item stack
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

}
