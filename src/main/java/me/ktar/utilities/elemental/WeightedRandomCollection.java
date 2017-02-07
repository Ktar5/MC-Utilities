package me.ktar.utilities.elemental;
/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 *
 * This file is part of Utilities.
 *
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

import com.google.common.collect.Lists;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A utility collection created in order to simplify the selection of a random
 * element based on its corresponding relative weight
 *
 * "Relative weight" means that, for example, each "weight" is a lottery ticket thrown
 * into the "total weight". We pick a random ticket, and then return that element. Thus,
 * the higher the weight, the higher the chance it will be picked.
 *
 * @param <E> the type of element to be placed into this collection
 */
public class WeightedRandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<>();
    private E last;
    private double total = 0;

    /**
     * Add an item to the random collection with the
     * specified weight
     *
     * @param weight the weight (relative to the others)
     * @param result the item that corresponds to this weight
     */
    public WeightedRandomCollection<E> add(double weight, E result) {
        if (weight <= 0) throw new NumberFormatException("Weights cannot be less than zero. Idiot.");
        total += weight;
        map.put(total, result);
        last = result;
        return this;
    }

    public E last(){
        return last;
    }

    /**
     * Return a set of all the values
     *
     * @return a set of all the values
     */
    public Set<E> values(){
        return new HashSet<E>(map.values());
    }

    /**
     * Returns #amount of unique elements chosen randomly for a lottery-type system
     *
     * @param amount the max amount of unique elements you want returned
     * @return a list containing maximum #amount unique elements, unless there are
     *             less than that many elements in the collection
     */
    public List<E> getUniqueElements(int amount){
        if(map.size() <= amount){
            return Lists.newArrayList(map.values());
        }
        List<E> uniqueElements = new ArrayList<>();
        while(uniqueElements.size() != amount){
            if(!uniqueElements.contains(next())){
                uniqueElements.add(last());
            }
        }
        return uniqueElements;
    }

    /**
     * Select a random item from the list based on the chance
     * Uses a ThreadLocalRandom because Random sucks shit
     * ThreadLocalRandom is faster
     *
     * @return a random element from the collection, selected based on its relative weight
     */
    public E next() {
        double value = ThreadLocalRandom.current().nextDouble() * total;
        last = map.ceilingEntry(value).getValue();
        return last;
    }
}