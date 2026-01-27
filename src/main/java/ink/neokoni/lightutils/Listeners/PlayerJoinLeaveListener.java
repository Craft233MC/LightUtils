package ink.neokoni.lightutils.Listeners;

import ink.neokoni.lightutils.DataStorage.Configs;
import ink.neokoni.lightutils.LightUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeaveListener implements Listener {
    public PlayerJoinLeaveListener() {
        if (Configs.getConfigs().getBoolean("utils.cancels.player-join-leave-msg")) {
            LightUtils plugin = LightUtils.getInstance();
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        e.joinMessage(null);
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e) {
        e.quitMessage(null);
    }
}
