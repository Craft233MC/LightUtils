package ink.neokoni.lightutils.Listeners;

import ink.neokoni.lightutils.Commands.FreeCamCommand;
import ink.neokoni.lightutils.LightUtils;
import ink.neokoni.lightutils.Utils.TextUtils;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.william278.huskhomes.event.HomeCreateEvent;
import net.william278.huskhomes.position.Position;
import net.william278.huskhomes.position.World;
import org.bukkit.Location;
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
        if (event.getCreator() instanceof Player player) {
            if(!FreeCamCommand.getEnabledMap().containsKey(player)) {
                return;
            }
            Location loc = FreeCamCommand.getEnabledMap().get(player).getFirst(); // 获取玩家进入自由视角前的位置
            Position pos = event.getPosition(); // 获取玩家创建的家位置
            pos.setX(loc.x());pos.setY(loc.y());pos.setX(loc.z());
            pos.setWorld(World.from(loc.getWorld().getName(), loc.getWorld().getUID()));
            event.setPosition(pos);
            event.setName(PlainTextComponentSerializer.plainText().serialize(TextUtils.getLang("freecam.cannot-create-home")));
        }
    }
}

