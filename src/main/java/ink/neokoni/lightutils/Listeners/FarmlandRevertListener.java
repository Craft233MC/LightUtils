package ink.neokoni.lightutils.Listeners;

import ink.neokoni.lightutils.DataStorage.Configs;
import ink.neokoni.lightutils.LightUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class FarmlandRevertListener implements Listener {
    public FarmlandRevertListener() {
        if (Configs.getConfigs().getBoolean("utils.cancels.entity-farmland-revert")) {
            LightUtils plugin = LightUtils.getInstance();
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }
    @EventHandler
    private void onPlayerFarmlandRevert(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.PHYSICAL) && e.getClickedBlock().getType().equals(Material.FARMLAND)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onEntityFarmlandRevert(EntityInteractEvent e) {
        if (e.getBlock().getType().equals(Material.FARMLAND)) {
            e.setCancelled(true);
        }
    }
}
