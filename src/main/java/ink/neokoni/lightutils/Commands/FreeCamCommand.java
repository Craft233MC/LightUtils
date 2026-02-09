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
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.nio.Buffer;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FreeCamCommand {
    private final LightUtils plugin = LightUtils.getInstance();
    private static final Map<OfflinePlayer, Pair<Location, GameMode>> enabled = new ConcurrentHashMap<>();

    public FreeCamCommand() {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("freecam")
                    .requires(source -> source.getSender().hasPermission(
                            Configs.getConfigs().getString("freecam.execute-perms")) &&
                            Configs.getConfigs().getBoolean("freecam.enable"))
                    .executes(ctx -> {
                        return switchFreeCam(ctx.getSource().getSender(),
                                Configs.getConfigs().getInt("freecam.limit"));
                    });
            commands.registrar().register(command.build());
        });

        YamlConfiguration playerData = PlayerDatas.getPlayerData();
        playerData.getKeys(false).forEach(uuid -> {
            if (playerData.isSet(uuid+".freecam.gamemode") && playerData.isSet(uuid+".freecam.location")) {
                OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(UUID.fromString(uuid));
                Location location = playerData.getObject(uuid+".freecam.location", Location.class);
                GameMode gameMode = GameMode.valueOf(playerData.getString(uuid+".freecam.gamemode"));
                enabled.put(player, Pair.of(location, gameMode));
            }
        });
    }

    public static int switchFreeCam(CommandSender sender, int limit) {
        var playerData = PlayerDatas.getPlayerData();
        if (!(sender instanceof Player)) {
            return Command.SINGLE_SUCCESS;
        }
        Player player = (Player) sender;

        Pair<Location, GameMode> savedPairData = new Pair<>(null, null);

        if (playerData.getObject(player.getUniqueId()+".freecam.location", Location.class) != null &&
                playerData.getString(player.getUniqueId()+".freecam.gamemode") != null) {
            Location loc = playerData.getObject(player.getUniqueId()+".freecam.location", Location.class);
            GameMode mode = GameMode.valueOf(playerData.getString(player.getUniqueId()+".freecam.gamemode"));
            savedPairData = Pair.of(loc, mode);
        }

        if (savedPairData.getFirst() != null && savedPairData.getSecond()!= null) {
            enabled.put(player, savedPairData);
        }

        if (enabled.containsKey(player)) {
            playerData.set(player.getUniqueId()+".freecam.gamemode", null);
            playerData.set(player.getUniqueId()+".freecam.location", null);

            PlayerDatas.savePlayerData(playerData);
            PlayerDatas.writeToFile();

            player.setGameMode(enabled.get(player).getSecond());
            player.teleportAsync(enabled.get(player).getFirst());
            enabled.remove(player);
        } else {
            Location originalLocation = player.getLocation().clone();
            enabled.put(player, new Pair<>(originalLocation, player.getGameMode()));
            playerData.set(player.getUniqueId()+".freecam.gamemode", player.getGameMode().toString());
            playerData.set(player.getUniqueId()+".freecam.location", player.getLocation());

            PlayerDatas.savePlayerData(playerData);
            PlayerDatas.writeToFile();

            player.setGameMode(GameMode.SPECTATOR);

            if (limit > 0) {
                new FreeCamDetectPlayersDistanceTask(player, originalLocation, limit);
            }

        }
        return Command.SINGLE_SUCCESS;
    }

    public static Map<OfflinePlayer, Pair<Location, GameMode>> getEnabledMap() {
        return enabled;
    }
}
