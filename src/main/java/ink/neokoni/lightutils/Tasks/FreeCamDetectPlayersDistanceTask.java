package ink.neokoni.lightutils.Tasks;

import ink.neokoni.lightutils.Commands.FreeCamCommand;
import ink.neokoni.lightutils.LightUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FreeCamDetectPlayersDistanceTask {
    public FreeCamDetectPlayersDistanceTask(Player player, Location location, int distance) {
        player.getScheduler().runAtFixedRate(
                LightUtils.getInstance(),
                scheduledTask -> {
                    if (!FreeCamCommand.getEnabledMap().containsKey(player)) {
                        scheduledTask.cancel();
                    }
                    Location[] locations = new Location[Bukkit.getOnlinePlayers().size()];
                    int i = 0;
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.equals(player)) {
                            locations[i] = location;
                        } else {
                            locations[i] = p.getLocation();
                        }
                        i++;
                    }

                    boolean outLimit = true;
                    for (Location value : locations) {
                        if (value==null) {
                            continue;
                        }
                        if (player.getLocation().distance(value) <= distance) {
                            outLimit = false;
                        }
                    }
                    if (outLimit) {
                        FreeCamCommand.switchFreeCam(player, 0);
                        scheduledTask.cancel();
                    }
                },
                null,
                5,
                5
        );
    }
}
