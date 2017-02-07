package me.ktar.utilities.misc;
/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 *
 * This file is part of Utilities.
 *
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class ReflectionUtil {

    /*
     * The server version string to location NMS & OBC classes
     */
    private static String versionString;

    /*
     * Cache of NMS classes that we've searched for
     */
    private static final Map<String, Class<?>> LOADED_NMS_CLASSES = new HashMap<>();

    /*
     * Cache of OBS classes that we've searched for
     */
    private static final Map<String, Class<?>> LOADED_OBC_CLASSES = new HashMap<>();

    /*
     * Cache of methods that we've found in particular classes
     */
    private static final Map<Class<?>, Map<String, Method>> LOADED_METHODS;

    static {
        LOADED_METHODS = new HashMap<>();
    }

    /*
     * Cache of fields that we've found in particular classes
     */
    private static final Map<Class<?>, Map<String, Field>> LOADED_FIELDS = new HashMap<>();

    private ReflectionUtil() {
    }

    /**
     * Gets the version string for NMS & OBC class paths
     *
     * @return The version string of OBC and NMS packages
     */
    public static String getVersion() {
        if (versionString == null) {
            String name = Bukkit.getServer().getClass().getPackage().getName();
            versionString = name.substring(name.lastIndexOf('.') + 1) + '.';
        }

        return versionString;
    }

    /**
     * Get an NMS Class
     *
     * @param nmsClassName The name of the class
     * @return The class
     */
    public static Class<?> getNMSClass(String nmsClassName) {
        if (LOADED_NMS_CLASSES.containsKey(nmsClassName)) {
            return LOADED_NMS_CLASSES.get(nmsClassName);
        }

        String clazzName = "net.minecraft.server." + getVersion() + nmsClassName;
        Class<?> clazz;

        try {
            clazz = Class.forName(clazzName);
        } catch (Throwable t) {
            t.printStackTrace();
            return LOADED_NMS_CLASSES.put(nmsClassName, null);
        }

        LOADED_NMS_CLASSES.put(nmsClassName, clazz);
        return clazz;
    }

    /**
     * Get a class from the org.bukkit.craftbukkit package
     *
     * @param obcClassName the path to the class
     * @return the found class at the specified path
     */
    public static synchronized Class<?> getOBCClass(String obcClassName) {
        if (LOADED_OBC_CLASSES.containsKey(obcClassName)) {
            return LOADED_OBC_CLASSES.get(obcClassName);
        }

        String clazzName = "org.bukkit.craftbukkit." + getVersion() + obcClassName;
        Class<?> clazz;

        try {
            clazz = Class.forName(clazzName);
        } catch (Throwable t) {
            t.printStackTrace();
            LOADED_OBC_CLASSES.put(obcClassName, null);
            return null;
        }

        LOADED_OBC_CLASSES.put(obcClassName, clazz);
        return clazz;
    }

    /**
     * Get a Bukkit {@link Player} players NMS playerConnection object
     *
     * @param player The player
     * @return The players connection
     */
    public static Object getConnection(Player player) {
        Method getHandleMethod = getMethod(player.getClass(), "getHandle");

        if (getHandleMethod != null) {
            try {
                Object nmsPlayer = getHandleMethod.invoke(player);
                Field playerConField = getField(nmsPlayer.getClass(), "playerConnection");
                return playerConField.get(nmsPlayer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Get a classes constructor
     *
     * @param clazz  The constructor class
     * @param params The parameters in the constructor
     * @return The constructor object
     */
    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... params) {
        try {
            return clazz.getConstructor(params);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * Get a method from a class that has the specific parameters
     *
     * @param clazz      The class we are searching
     * @param methodName The name of the method
     * @param params     Any parameters that the method has
     * @return The method with appropriate parameters
     */
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
        if (!LOADED_METHODS.containsKey(clazz)) {
            LOADED_METHODS.put(clazz, new HashMap<>());
        }

        Map<String, Method> methods = LOADED_METHODS.get(clazz);

        if (methods.containsKey(methodName)) {
            return methods.get(methodName);
        }

        try {
            Method method = clazz.getMethod(methodName, params);
            methods.put(methodName, method);
            LOADED_METHODS.put(clazz, methods);
            return method;
        } catch (Exception e) {
            e.printStackTrace();
            methods.put(methodName, null);
            LOADED_METHODS.put(clazz, methods);
            return null;
        }
    }

    /**
     * Get a field with a particular name from a class
     *
     * @param clazz     The class
     * @param fieldName The name of the field
     * @return The field object
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        if (!LOADED_FIELDS.containsKey(clazz)) {
            LOADED_FIELDS.put(clazz, new HashMap<>());
        }

        Map<String, Field> fields = LOADED_FIELDS.get(clazz);

        if (fields.containsKey(fieldName)) {
            return fields.get(fieldName);
        }

        try {
            Field field = clazz.getField(fieldName);
            fields.put(fieldName, field);
            LOADED_FIELDS.put(clazz, fields);
            return field;
        } catch (Exception e) {
            e.printStackTrace();
            fields.put(fieldName, null);
            LOADED_FIELDS.put(clazz, fields);
            return null;
        }
    }
}