package ink.neokoni.lightutils.Tasks;

import ink.neokoni.lightutils.LightUtils;
import ink.neokoni.lightutils.Listeners.PlayerSpawnListener;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class DetectIsPlayerDeadTask {
    public static Map<UUID, ScheduledTask> waitingForRespawnTasks = new ConcurrentHashMap<>();
    public DetectIsPlayerDeadTask() {
        if (LightUtils.isFolia) return;
        Bukkit.getAsyncScheduler().runAtFixedRate(
                LightUtils.getInstance(),
                checkTask -> {
                    Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                        if (player.isDead() && !waitingForRespawnTasks.containsKey(player.getUniqueId())) {
                            Location deathLocation = player.getLocation().clone();
                            waitingForRespawnTasks.put(player.getUniqueId(),
                                    Bukkit.getAsyncScheduler().runAtFixedRate(
                                            LightUtils.getInstance(),
                                            task -> {
                                                if (player.isOnline() && !player.isDead()) {
                                                    if (PlayerSpawnListener.isValidSpawnLocation()
                                                            && player.getRespawnLocation() == null) {
                                                        player.teleportAsync(deathLocation);
                                                        PlayerSpawnListener.teleportPlayerToSpawn(player);
                                                    }
                                                    waitingForRespawnTasks.remove(player.getUniqueId()).cancel();
                                                }

                                                if (!player.isOnline()) {
                                                    waitingForRespawnTasks.remove(player.getUniqueId()).cancel();
                                                }
                                            }
                                            , 1L, 5L, TimeUnit.MILLISECONDS));
                        }
                    });

                    waitingForRespawnTasks.keySet().forEach(uuid -> {
                        Player player = Bukkit.getServer().getPlayer(uuid);
                        if (player == null || (player != null && !player.isOnline())) {
                            waitingForRespawnTasks.remove(uuid).cancel();
                        }
                    });
                },
                1L,
                1L,
                TimeUnit.SECONDS
        );
    }
}
