package me.ktar.utilities;
/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 *
 * This file is part of Utilities.
 *
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

import lombok.Getter;
import me.ktar.utilities.annotation.AnnotationRegistry;
import me.ktar.utilities.log.Logger;
import me.ktar.utilities.ui.gui.MenuListener;
import me.ktar.utilities.ui.hotbar.HotbarCoordinator;
import me.ktar.utilities.ui.hotbar.listeners.HotbarListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This is the main class for my commonly used Utilities.
 * A few notes about this:
 *      1). This isn't a stand-alone plugin, so it needs
 *              to be shaded into a plugin.
 *      2). Use {@link Utilities#initialize(JavaPlugin, String)}
 *              to initialize (only call this method once).
 *      3). This is a heavy-duty API that should be implemented as
 *              high up the dependency tree as possible. Meaning,
 *              there should only exist one instance of {@link Utilities}
 *              per server instance.
 */
public class Utilities {

    @Getter private static Utilities instance = null;
    @Getter private final HotbarCoordinator hotbarCoordinator;
    @Getter private final Logger logger;

    private Utilities(JavaPlugin plugin, String packageBaseName){
        this.logger = new Logger(plugin);
        this.hotbarCoordinator = new HotbarCoordinator();
        Bukkit.getServer().getPluginManager().registerEvents(new MenuListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new HotbarListener(), plugin);
        AnnotationRegistry.register(plugin, instance, packageBaseName);
    }

    /**
     * Initialize all the fun API shit
     *
     * @param plugin An instance of your plugin
     * @param basePackageName A project-specific (unless placed in a core) base package
     *                        ex.: "me.ktar.utilities" or "com.spigot" for the latter
     */
    public static void initialize(JavaPlugin plugin, String basePackageName){
        if(instance != null){
            instance.logger.error("");
            instance.logger.error("!!!================================================!!!");
            instance.logger.error("!!      You can not initialize Utilities twice      !!");
            instance.logger.error("!!   I decided to throw you a friendly stacktrace   !!");
            instance.logger.error("!!!================================================!!!");
            instance.logger.error("");
            throw new ExceptionInInitializerError("Don't initialize Utilities twice, nerd.");
        }else {
            instance = new Utilities(plugin, basePackageName);
        }
    }


}
