package me.ktar.utilities.elemental.queue;
/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 *
 * This file is part of Utilities.
 *
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

import java.util.LinkedList;

public class LimitedQueue<E> extends LinkedList<E> {

    protected int limit;

    public LimitedQueue(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(E o) {
        super.addFirst(o);
        while (size() > limit) { super.removeLast(); }
        return true;
    }
}