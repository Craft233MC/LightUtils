package ink.neokoni.lightutils.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import ink.neokoni.lightutils.DataStorage.Configs;
import ink.neokoni.lightutils.LightUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand {
    private final LightUtils plugin = LightUtils.getInstance();
    public FlyCommand() {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("fly")
                    .requires(source -> hasFlyPerms(source.getSender()) &&
                            Configs.getConfig("config").getBoolean("fly.enable"))
                    .executes(ctx -> {
                        return switchFly(ctx.getSource().getSender());
                    });
            commands.registrar().register(command.build());
        });
    }

    private int switchFly(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return Command.SINGLE_SUCCESS;
        }
        Player player = (Player) sender;
        if (!player.getAllowFlight()) {
            player.setAllowFlight(true);
            return Command.SINGLE_SUCCESS;
        }
        player.setAllowFlight(false);
        return Command.SINGLE_SUCCESS;
    }

    private boolean hasFlyPerms(CommandSender sender) {
        String perms = Configs.getConfig("config").getString("fly.execute-perms");
        if (perms == null || perms.isEmpty() || perms.equals("")) {
            return true;
        }
        return sender.hasPermission(perms);
    }
}
