package ink.neokoni.lightutils.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.datafixers.util.Pair;
import ink.neokoni.lightutils.DataStorage.Configs;
import ink.neokoni.lightutils.DataStorage.PlayerDatas;
import ink.neokoni.lightutils.LightUtils;
import ink.neokoni.lightutils.Tasks.FreeCamDetectPlayersDistanceTask;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FreeCamCommand {
    private final LightUtils plugin = LightUtils.getInstance();
    private static final Map<Player, Pair<Location, GameMode>> enabled = new ConcurrentHashMap<>();

    private final String perms = Configs.getConfig("config").getString("freecam.execute-perms");
    private final boolean isEnabled = Configs.getConfig("config").getBoolean("freecam.enable");
    private final int limit = Configs.getConfig("config").getInt("freecam.limit");

    public FreeCamCommand() {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("freecam")
                    .requires(source -> source.getSender().hasPermission(perms) && isEnabled)
                    .executes(ctx -> {
                        return switchFreeCam(ctx.getSource().getSender(),limit);
                    });
            commands.registrar().register(command.build());
        });
    }

    public static int switchFreeCam(CommandSender sender, int limit) {
        var playerData = PlayerDatas.getPlayerData();
        if (!(sender instanceof Player)) {
            return Command.SINGLE_SUCCESS;
        }
        Player player = (Player) sender;

        Pair savedPairData = new Pair<>(null, null);

        if (playerData.getObject(player.getUniqueId()+".freecam.location", Location.class) != null &&
                playerData.getString(player.getUniqueId()+".freecam.gamemode") != null) {
            savedPairData = new Pair(
                    playerData.getObject(player.getUniqueId()+".freecam.location", Location.class),
                    GameMode.valueOf(playerData.getString(player.getUniqueId()+".freecam.gamemode")));
        }

        if (savedPairData.getFirst() != null && savedPairData.getSecond()!= null) {
            enabled.put(player, savedPairData);
        }

        if (enabled.containsKey(player)) {
            playerData.set(player.getUniqueId()+".freecam.gamemode", null);
            playerData.set(player.getUniqueId()+".freecam.location", null);

            PlayerDatas.savePlayerData(playerData);

            player.setGameMode(enabled.get(player).getSecond());
            player.teleport(enabled.get(player).getFirst());
            enabled.remove(player);
        } else {
            enabled.put(player, new Pair<>(player.getLocation(), player.getGameMode()));
            playerData.set(player.getUniqueId()+".freecam.gamemode", player.getGameMode().toString());
            playerData.set(player.getUniqueId()+".freecam.location", player.getLocation());

            PlayerDatas.savePlayerData(playerData);

            player.setGameMode(GameMode.SPECTATOR);

            if (limit > 0) {
                new FreeCamDetectPlayersDistanceTask(player, player.getLocation().clone(), limit);
            }

        }
        return Command.SINGLE_SUCCESS;
    }

    public static Map<Player, Pair<Location, GameMode>> getEnabledMap() {
        return enabled;
    }
}
