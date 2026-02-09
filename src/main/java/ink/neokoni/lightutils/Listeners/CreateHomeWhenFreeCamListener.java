package ink.neokoni.lightutils.Listeners;

import ink.neokoni.lightutils.Commands.FreeCamCommand;
import ink.neokoni.lightutils.LightUtils;
import ink.neokoni.lightutils.Utils.TextUtils;
import net.william278.huskhomes.api.HuskHomesAPI;
import net.william278.huskhomes.event.HomeCreateEvent;
import net.william278.huskhomes.position.Location;
import net.william278.huskhomes.position.Position;
import net.william278.huskhomes.position.World;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class CreateHomeWhenFreeCamListener implements Listener {
    public CreateHomeWhenFreeCamListener() {
        LightUtils plugin = LightUtils.getInstance();
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        if (pluginManager.isPluginEnabled("HuskHomes")) {
            pluginManager.registerEvents(this, plugin);
        }
    }

    @EventHandler
    private void onCreateHome(HomeCreateEvent event) {
        if (event.getCreator().hasPermission("minecraft.admin")) {
            return;
        }
        Player player = Bukkit.getPlayerExact(event.getOwner().getName());
        if (player != null) {
            if(!FreeCamCommand.getEnabledMap().containsKey(player)) {
                return;
            }
            var loc = FreeCamCommand.getEnabledMap().get(player).getFirst(); // 获取玩家进入自由视角前的位置
            Position position = Position.at( // 获取玩家创建的家位置
                    Location.at(loc.getX(), loc.getY(), loc.getZ(),
                            World.from(loc.getWorld().getName())), event.getPosition().getServer());
            event.setPosition(position);
            player.getScheduler().runDelayed(
                    LightUtils.getInstance(),
                    task -> {
                        HuskHomesAPI.getInstance().setHomeDescription(
                                event.getOwner(),
                                event.getName(),
                                TextUtils.getLangString("freecam.cannot-create-home")
                        );
                    },
                    null,
                    2L
            );
        }
    }
}

