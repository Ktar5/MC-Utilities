package me.ktar.utilities.examples;
/*
 * Copyright (C) 2013-Current Carter Gale (Ktar5) <ktarfive@gmail.com>
 * 
 * This file is part of Utilities.
 * 
 * Utilities can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */

import me.ktar.utilities.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

//@StaticCommand
public class ExampleCommand extends Command {

    public ExampleCommand() {
        super("shrug");
        setUsage("<command>");
    }

    @Override
    public boolean canExecute(CommandSender sender, String alias, String[] args) {
        return sender.isOp(); //Replace with check their rank
    }


    @Override
    public boolean onExecute(CommandSender sender, String alias, String[] args) {
        if (!(sender instanceof Player))
            return false;

        String arg = "";

        if (args != null)
            for (String a : args)
                arg += a + " ";

        Player p = (Player) sender;
        p.chat(arg + " \u00AF\\_(\u30C4)_/\u00AF");

        return true;
    }

}

