package me.ktar.utilities.command.executor;

import me.ktar.utilities.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 *
 * This file is part of Utilities.
 *
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */
public class SubCommandExecutor implements CommandExecutor
{

    /**
     * Should it call "onNoChildExecute" if there is no known child command
     */
    protected boolean arguedCommand = false;

    public void setArguedCommand(){
        arguedCommand = true;
    }

    /**
     * A read of execution sub-types' aliases to their corresponding singletons.
     */
    private final Map<String, Command> children = new HashMap<>();

    /**
     * Gets the a read of execution sub-types' aliases to their corresponding singletons.
     * @return the a read of execution sub-types' aliases to their corresponding singletons.
     */
    public Map<String, Command> getChildren() {
        return children;
    }

    /**
     * Registers a child command to this main command.
     * @param command - the child {@link Command} to be registered.
     */
    public void registerChildCommand(Command command) {
        children.put(command.getLabel().toLowerCase(), command);
        for (String alias : command.getAliases())
            children.put(alias.toLowerCase(), command);
    }

    /**
     * Unregisters a child command from this main command.
     * @param command - the child {@link Command} to be unregistered.
     */
    public void unregisterChildCommand(Command command) {
        children.remove(command.getLabel().toLowerCase());
        for (String alias : command.getAliases())
            children.remove(alias.toLowerCase());
    }

    /**
     * Handles the default listened execution of this {@link SubCommandExecutor}, <br>
     * when the command was executed without a defining {@link CommandExecutor} type.
     *
     * @param sender - the {@link CommandSender} of this command execution.
     * @param alias - the alias the {@link CommandSender} chose to use for this execution.
     * @return true if this listened execution was successful.
     */
    public boolean onExecuteDefault(CommandSender sender, String alias) {
        return false;
    }

    @Override
    public boolean onExecute(CommandSender sender, String alias, String[] args) {
        if (args.length == 0)
            return onExecuteDefault(sender, alias);
        Command child = children.get(args[0]);
        if (child == null){
            if(arguedCommand){
                return onNoChildExecute(sender, alias, args);
            }
            return false;
        }
        return child.execute(sender, args[0], Arrays.copyOfRange(args, 1, args.length));
    }

    public boolean onNoChildExecute(CommandSender sender, String alias, String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        String starts = args[0].toLowerCase();
        List<String> players = Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getName().toLowerCase().startsWith(starts))
                .map((Function<Player, String>) Player::getName)
                .collect(Collectors.toList());

        if (args.length == 0)
            return players;
        else if (args.length == 1) {
            List<String> result = new ArrayList<>(children.size());
            for (Map.Entry<String, Command> entry : children.entrySet()) {
                Command child = entry.getValue();
                if (entry.getKey().startsWith(starts) && child.canExecute(sender, alias, args))
                        result.add(entry.getKey());
            }

            result.addAll(players);
            Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
            return result;
        } else {
            Command child = children.get(args[0]);
            if (child == null || !child.canExecute(sender, alias, args))
                return Collections.emptyList();
            String[] childArgs = new String[args.length - 1];
            System.arraycopy(args, 1, childArgs, 0, childArgs.length);
            return child.tabComplete(sender, args[0], childArgs);
        }
    }

    public boolean isSubCommandRegistered(String sub)
    {
        return children.containsKey(sub);
    }
}
