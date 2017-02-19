package me.ktar.utilities.command;

import me.ktar.utilities.command.executor.CommandExecutor;
import me.ktar.utilities.command.executor.SubCommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 *
 * This file is part of Utilities.
 *
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */
public class Command extends org.bukkit.command.Command implements CommandExecutor {

    private boolean playerOnly;
    private int minArgs;

    /**
     * Instantiates a new command.
     *
     * @param name    - the name of this command.
     * @param aliases - the aliases this command possesses.
     */
    public Command(String name, String... aliases) {
        super(name);
        setAliases(Arrays.asList(aliases));
        setUsage("<command>");
        setPermissionMessage(ChatColor.RED + "You do not have permission to execute this command.");
    }

    /**
     * Determines if the specified execution attempt can continue.
     *
     * @param sender - the {@link CommandExecutor} of this command execution attempt.
     * @param alias  - the alias the {@link CommandExecutor} chose to use for this execution.
     * @param args   - the arguments the {@link CommandExecutor} gave for referencing.
     * @return true, if the execution attempt can continue.
     */
    public boolean canExecute(CommandSender sender, String alias, String[] args) {
        return isEnabled();
    }

    public final void setPlayerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
    }

    public final void setMinArgLength(int minArgs) {
        this.minArgs = minArgs;
    }

    private void sendUsage(CommandSender sender, String alias) {
        if (getUsage() != null && getUsage().length() > 0)
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage().replace("<command>", "/" + alias)
                    .replace("<arg>", alias));
    }

    @Override
    public final boolean execute(CommandSender sender, String alias, String[] args) {

        if (canExecute(sender, alias, args))
            try {
                if (playerOnly && !(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be executed from a player.");
                    return true;
                }
                if (getPermission() != null && !sender.hasPermission(getPermission())) {
                    sender.sendMessage(getPermissionMessage());
                    return true;
                }
                if (args.length < minArgs) {
                    sendUsage(sender, alias);
                    return false;
                }
                if (getExecutor() == null) {
                    return true;
                } else if (getExecutor().onExecute(sender, alias, args)) {
                    return true;
                } else {
                    if(getExecutor() == this || (getExecutor() instanceof SubCommandExecutor
                            && (args.length == 0 || !((SubCommandExecutor)getExecutor()).isSubCommandRegistered(args[0]))))
                    {
                        sendUsage(sender, alias);
                        return false;
                    }
                    return true;
                }
            } catch (CommandException e) {
                sender.sendMessage(ChatColor.RED + "" + ChatColor.BLUE + ">" + ChatColor.RED + " " + e.getMessage());
                return true;
            } catch (RuntimeException e) {
                sender.sendMessage(ChatColor.RED + "Something went wrong!");
                sender.sendMessage(ChatColor.RED + "Usage: " + getUsage().replace("<command>", "/" + alias)
                        .replace("<arg>", alias));
                if (sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + formatException(e).replace("<command>", alias)
                            .replace("<arg>", alias));
                }
                e.printStackTrace();
                return true;
            }
        sender.sendMessage("Unknown command. Type \"/help\" for help.");
        return true;
    }

    @Override
    public final List<String> tabComplete(CommandSender sender, String alias, String[] args) {

        if (canExecute(sender, alias, args)) {
            List<String> completions = getExecutor().onTabComplete(sender, alias, args);

            if (completions == null) {
                return super.tabComplete(sender, alias, args);
            }

            return completions;
        } else
            return super.tabComplete(sender, alias, args);
    }

    @Override
    public boolean onExecute(CommandSender sender, String alias, String[] args) {
        throw new CommandException("/<command> is not implemented");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        return super.tabComplete(sender, alias, args);
    }

    /**
     * Registers this command in the bukkit command read.
     *
     * @return if this command was successfully registered or not.
     */
    public final boolean registerCommand() {
        return CommandHandler.registerCommand(this);
    }

    /**
     * The executor of this command.<br>
     * By default: {@value}
     */
    private CommandExecutor executor = this;

    /**
     * Gets the executor of this command.
     *
     * @return the executor of this command.
     */
    public final CommandExecutor getExecutor() {
        return executor;
    }

    /**
     * Sets the executor of this command.
     *
     * @param executor the new executor of this command.
     */
    public final void setExecutor(CommandExecutor executor) {
        this.executor = executor;
    }

    /**
     * If this command is enabled and accessible for use.
     */
    private boolean enabled = true;

    /**
     * Checks if is if this command is enabled and accessible for use.
     *
     * @return the if this command is enabled and accessible for use.
     */
    public final boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the if this command is enabled and accessible for use.
     *
     * @param enabled the new if this command is enabled and accessible for use.
     */
    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Formats the specified exception to be easily read in a Minecraft chat, console, or other type of medium.
     *
     * @param exception - the exception that was thrown and in need of formatting.
     * @return the formatted, easily-readable exception message.
     */
    public static String formatException(Exception exception) {
        if (exception instanceof CommandException)
            return exception.getMessage();
        if (exception.getStackTrace().length == 0)
            return "Something went wrong...";
        StackTraceElement element = exception.getStackTrace()[0];
        for (StackTraceElement e : exception.getStackTrace())
            if (e.getClassName().startsWith("gg.shaded.")) {
                element = e;
                break;
            }
        String className = element.getClassName();
        try {
            className = Class.forName(element.getClassName()).getSimpleName();
        } catch (ClassNotFoundException ignored) {
        }
        return String.format("%s: %s.%s(%s:%s)", exception.getClass().getSimpleName(), className,
                element.getMethodName(), element.getFileName(), element.getLineNumber());
    }

    /**
     * Gets the represented command from the specified name query.
     *
     * @param name - the name to be querying the registered command list for.
     * @return the represented command with the specified name.
     */
    public final static Command getByName(String name) {
        org.bukkit.command.Command command = CommandHandler.getCommandMap().getCommand(name);
        if (command instanceof Command)
            return (Command) command;
        return null;
    }

}

