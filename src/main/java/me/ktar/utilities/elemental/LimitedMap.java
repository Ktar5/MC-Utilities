package me.ktar.utilities.elemental;
/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 *
 * This file is part of Utilities.
 *
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

import java.util.LinkedHashMap;
import java.util.Map;

public class LimitedMap<K, V> extends LinkedHashMap<K, V> {

    private int limit;

    public LimitedMap(int limit){
        this.limit = limit;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest){
        return size() > limit;
    }

}
