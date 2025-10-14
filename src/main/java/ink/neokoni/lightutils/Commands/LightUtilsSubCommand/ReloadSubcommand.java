package ink.neokoni.lightutils.Commands.LightUtilsSubCommand;

import ink.neokoni.lightutils.Utils.DataStorageUtils;
import org.bukkit.command.CommandSender;

public class ReloadSubcommand {
    public ReloadSubcommand(CommandSender sender){
        DataStorageUtils.reloadAllDataAsync(sender);
    }
}
