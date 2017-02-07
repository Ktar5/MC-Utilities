package me.ktar.utilities.annotation;
/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 *
 * This file is part of Utilities.
 *
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

import me.ktar.utilities.Utilities;
import me.ktar.utilities.command.Command;
import me.ktar.utilities.command.CommandHandler;
import me.ktar.utilities.command.StaticCommand;
import me.ktar.utilities.ui.hotbar.Hotbar;
import me.ktar.utilities.ui.hotbar.StaticHotbar;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;

public class AnnotationRegistry {
    /**
     * @param plugin      The plugin to register everything to
     * @param utilities   The main Utilities instance
     * @param basePackage A base package to look in **BE PROJECT-SPECIFIC**
     *                    ex.: "me.ktar.utilities" or "com.spigot.spigot-api"
     */
    public static void register(Plugin plugin, Utilities utilities, String basePackage) {
        long lastTime = System.currentTimeMillis();
        long startTime = System.currentTimeMillis();

        Reflections ref = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(basePackage, plugin.getClass().getClassLoader()))
                .setScanners(new FieldAnnotationsScanner(), new SubTypesScanner(false), new TypeAnnotationsScanner()));

        System.out.println("Scanned classpath -- took " + (System.currentTimeMillis() - lastTime) + "ms");
        lastTime = System.currentTimeMillis();

        ref.getTypesAnnotatedWith(StaticListener.class).forEach(c -> {
            System.out.println("Registering " + c.getCanonicalName());
            try {
                if (Listener.class.isAssignableFrom(c)) {
                    Listener listener = (Listener) getInstance(c, plugin);
                    Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
                }
            } catch (Exception e) {
                System.out.println("There was a problem while registering the listener"
                        + c.getSimpleName() + " in " + c.getCanonicalName());
                e.printStackTrace();
            }
        });

        System.out.println("Registered listeners -- took " + (System.currentTimeMillis() - lastTime) + "ms");
        lastTime = System.currentTimeMillis();

        ref.getTypesAnnotatedWith(StaticHotbar.class).forEach(c -> {
            System.out.println("Registering hotbar: " + c.getSimpleName());
            try {
                if (Hotbar.class.isAssignableFrom(c)) {
                    Hotbar hotbar = (Hotbar) getInstance(c, plugin);
                    utilities.getHotbarCoordinator().register(hotbar);
                }
            } catch (Exception e) {
                System.out.println("There was a problem while registering the hotbar: "
                        + c.getSimpleName() + " as " + c.getCanonicalName());
                e.printStackTrace();
            }
        });

        System.out.println("Registered hotbars -- took " + (System.currentTimeMillis() - lastTime) + "ms");
        lastTime = System.currentTimeMillis();

        ref.getTypesAnnotatedWith(StaticCommand.class).forEach(c -> {
            System.out.println("Registering command: " + c.getSimpleName());
            try {
                if (Command.class.isAssignableFrom(c)) {
                    Command command = (Command) getInstance(c, plugin);
                    CommandHandler.registerCommand(command);
                }
            } catch (Exception e) {
                System.out.println("There was a problem while registering the command: "
                        + c.getSimpleName() + " as " + c.getCanonicalName());
                e.printStackTrace();
            }
        });

        System.out.println("Registered commands -- took " + (System.currentTimeMillis() - lastTime) + "ms");

        System.out.println("Completed annotation processing, total time: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private static Object getInstance(Class clazz, Plugin plugin) {
        for (Constructor constructor : clazz.getConstructors()) {
            try {
                if (constructor.getParameters().length == 1) {
                    if (!constructor.getParameters()[0].getType().equals(plugin.getClass())) {
                        throw new IllegalArgumentException("Couldn't register " + clazz.getSimpleName() + ". Invalid constructor.");
                    }

                    return constructor.newInstance(plugin);
                } else {
                    return constructor.newInstance();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
