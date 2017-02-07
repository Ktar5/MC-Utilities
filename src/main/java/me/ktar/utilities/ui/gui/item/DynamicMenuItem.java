package me.ktar.utilities.ui.gui.item;
/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 * 
 * This file is part of Utilities.
 * 
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

import me.ktar.utilities.misc.ItemFactory;

public abstract class DynamicMenuItem extends MenuItem {

    protected DynamicMenuItem(ItemFactory def) {
        super(def.getItemStack());
    }

}
