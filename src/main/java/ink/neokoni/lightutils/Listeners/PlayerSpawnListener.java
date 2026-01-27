package ink.neokoni.lightutils.Listeners;

import ink.neokoni.lightutils.DataStorage.Configs;
import ink.neokoni.lightutils.LightUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.concurrent.TimeUnit;

public class PlayerSpawnListener implements Listener {
    public PlayerSpawnListener() {
        if (Configs.getConfigs().getBoolean("utils.setworldspawn.enable")) {
            LightUtils plugin = LightUtils.getInstance();
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    @EventHandler
    private void onPlayerFirstJoin(PlayerJoinEvent e) {
        if (isValidSpawnLocation()) {
            return;
        }
        if (e.getPlayer().hasPlayedBefore()) {
            return;
        }
        teleportPlayerToSpawn(e.getPlayer());
    }

    @EventHandler
    private void onPlayerRespawn(PlayerRespawnEvent e) {
        if (isValidSpawnLocation()) {
            return;
        }
        if (e.getPlayer().getRespawnLocation()!=null) {
            return;
        }
        try {
            teleportPlayerToSpawn(e.getPlayer());
        } catch (Exception ex) {
            // Ignore invalid format
        }
    }

    @EventHandler
    // Fix respawn event on Folia
    private void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        if (LightUtils.isFolia) {
            Bukkit.getAsyncScheduler().runAtFixedRate(
                    LightUtils.getInstance(),
                    task -> {
                        if (player.isOnline() && !player.isDead()) {
                            teleportPlayerToSpawn(player);
                            task.cancel();
                        }
                        if (!player.isOnline()) {
                            task.cancel();
                        }
                    },
                    50L,
                    50L,
                    TimeUnit.MILLISECONDS
            );
        }
    }

    private void teleportPlayerToSpawn(Player player) {
        try {
            String locationStr = Configs.getConfigs().getString("utils.setworldspawn.location");
            String[] parts = locationStr.split(",");
            World world = Bukkit.getServer().getWorld(parts[0]);
            double x = Double.parseDouble(parts[1].trim());
            double y = Double.parseDouble(parts[2].trim());
            double z = Double.parseDouble(parts[3].trim());
            player.teleportAsync(new Location(world, x, y, z));
        } catch (Exception ex) {
            // Ignore invalid format
        }
    }

    private boolean isValidSpawnLocation() {
        String locationStr = Configs.getConfigs().getString("utils.setworldspawn.location");
        if (locationStr != null && !locationStr.isEmpty()) {
            double x,y,z;
            String[] parts = locationStr.split(",");
            if (parts.length == 4) {
                try {
                    World world = Bukkit.getServer().getWorld(parts[0]);
                    if (world == null) return false;
                    x = Double.parseDouble(parts[1].trim());
                    y = Double.parseDouble(parts[2].trim());
                    z = Double.parseDouble(parts[3].trim());
                    return false;
                } catch (Exception ex) {
                    return true;
                }
            }
        }
        return true;
    }
}
