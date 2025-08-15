package ink.neokoni.lightutils.commands;

import ink.neokoni.lightutils.Configs;
import org.bukkit.command.CommandSender;

public class ReloadSubcommand {
    ReloadSubcommand(CommandSender sender){
        new Configs().reloadConfig(sender);
    }
}
