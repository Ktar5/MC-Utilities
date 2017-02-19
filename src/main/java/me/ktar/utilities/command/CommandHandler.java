package me.ktar.utilities.command;

import me.ktar.utilities.misc.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Field;
import java.util.Map;

/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 *
 * This file is part of Utilities.
 *
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

public class CommandHandler {

    /**
     * The CraftServer's command read to be referenced.
     */
    private static final CommandMap COMMAND_MAP;

    /**
     * The CraftServer's alias to command read to be referenced.
     */
    private static final Map<String, Command> ALIAS_TO_COMMAND_MAP;

    /**
     * Gets the CraftServer's command read to be referenced.
     *
     * @return the CraftServer's command read to be referenced.
     */
    public static CommandMap getCommandMap() {
        return COMMAND_MAP;
    }

    /**
     * Gets the CraftServer's alias to command read to be referenced.
     *
     * @return the CraftServer's alias to command read to be referenced.
     */
    public static Map<String, Command> getAliasToCommandMap() {
        return ALIAS_TO_COMMAND_MAP;
    }

    /**
     * Unregisters a set of aliases.
     *
     * @param commandOrAlias The aliases to unregister.
     */
    public static void unregister(String... commandOrAlias) {
        for (String s : commandOrAlias)
            unregister(s);
    }

    private static void unregister(String commandOrAlias) {
        if (!ALIAS_TO_COMMAND_MAP.containsKey(commandOrAlias))
            return;
        Command cmd = ALIAS_TO_COMMAND_MAP.remove(commandOrAlias);
        boolean unregister = true;

        for (String alias : cmd.getAliases()) {
            if (alias.equalsIgnoreCase(commandOrAlias))
                continue;
            if (ALIAS_TO_COMMAND_MAP.containsKey(alias)) {
                unregister = false;
                break;
            }
        }
        if (unregister && !cmd.getLabel().equalsIgnoreCase(commandOrAlias) && ALIAS_TO_COMMAND_MAP.containsKey(cmd.getLabel()))
            unregister = false;
        if (unregister) {
            cmd.unregister(getCommandMap());
        }
    }

    /**
     * Registers this command in the bukkit command read.
     *
     * @return if this command was successfully registered or not.
     */
    public static boolean registerCommand(Command command) {
        return !getCommandMap().register("", command);
    }

    /**
     * Will try to forcefully add your command to the command read, overwriting all present entries.
     */
    public static boolean registerCommandForce(Command command) {
        unregister(command.getLabel());
        command.getAliases().forEach(CommandHandler::unregister);
        return registerCommand(command);
    }

    static {
        CommandMap commandMap = null;
        try {
            Class<?> craftServerClass = ReflectionUtil.getOBCClass("CraftServer");
            Field field = craftServerClass.getDeclaredField("commandMap");
            field.setAccessible(true);
            commandMap = (CommandMap) field.get(Bukkit.getServer());
        } catch (Exception e) {
            System.err.println("Could not access Bukkit.getServer().commandMap");
        } finally {
            COMMAND_MAP = commandMap;
        }

        Map<String, Command> aliasMap = null;

        try {
            Field field = SimpleCommandMap.class.getDeclaredField("knownCommands");
            field.setAccessible(true);
            aliasMap = (Map<String, Command>) field.get(commandMap);
        } catch (Exception e) {
            System.err.println("Could not access Bukkit.getServer().commandMap.knownCommands");
        } finally {
            ALIAS_TO_COMMAND_MAP = aliasMap;
        }
    }

}
