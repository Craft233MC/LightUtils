package ink.neokoni.lightutils.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.datafixers.util.Pair;
import ink.neokoni.lightutils.Configs;
import ink.neokoni.lightutils.LightUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FreeCamCommand {
    private final LightUtils plugin = LightUtils.getInstance();
    private final Map<Player, Pair<Location, GameMode>> enabled = new ConcurrentHashMap<>();

    private final String perms = Configs.getConfig("config").getString("freecam.execute-perms");
    private boolean isEnabled = Configs.getConfig("config").getBoolean("freecam.enable");
    public FreeCamCommand() {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("freecam")
                    .requires(source -> source.getSender().hasPermission(perms) && isEnabled)
                    .executes(ctx -> {
                        return switchFreeCam(ctx.getSource().getSender());
                    });
            commands.registrar().register(command.build());
        });
    }

    private int switchFreeCam(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return Command.SINGLE_SUCCESS;
        }
        Player player = (Player) sender;

        if (enabled.containsKey(player)) {
            player.setGameMode(enabled.get(player).getSecond());
            player.teleport(enabled.get(player).getFirst());
            enabled.remove(player);
        } else {
            enabled.put(player, new Pair<>(player.getLocation(), player.getGameMode()));
            player.setGameMode(GameMode.SPECTATOR);
        }
        return Command.SINGLE_SUCCESS;
    }
}
