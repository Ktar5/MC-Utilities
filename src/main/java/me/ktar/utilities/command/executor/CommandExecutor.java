package me.ktar.utilities.command.executor;

import org.bukkit.command.CommandSender;

import java.util.List;

/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 *
 * This file is part of Utilities.
 *
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */
public interface CommandExecutor {

    /**
     * Handles the listened execution of this {@link CommandExecutor}.
     * @param sender - the {@link CommandSender} of this command execution.
     * @param alias - the alias the {@link CommandSender} chose to use for this execution.
     * @param args - the arguments the {@link CommandSender} gave for this execution.
     * @return true if this listened execution was successful.
     */
    public boolean onExecute(CommandSender sender, String alias, String[] args);

    /**
     * Handles the listened tab completion of this {@link CommandExecutor}.
     * @param sender - the {@link CommandSender} of this tab completion.
     * @param alias - the alias the {@link CommandSender} chose to use for this tab completion.
     * @param args - the arguments the {@link CommandSender} has currently finished.
     * @return the list of strings matching the {@link CommandSender}'s query.
     */
    public List<String> onTabComplete(CommandSender sender, String alias, String[] args);

}
