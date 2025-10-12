package ink.neokoni.lightutils.Commands;

import ink.neokoni.lightutils.Configs;
import org.bukkit.command.CommandSender;

public class ReloadSubcommand {
    ReloadSubcommand(CommandSender sender){
        new Configs().reloadConfig(sender);
    }
}
