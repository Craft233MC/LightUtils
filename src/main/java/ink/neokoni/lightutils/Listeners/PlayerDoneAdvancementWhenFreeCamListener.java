package ink.neokoni.lightutils.Listeners;

import ink.neokoni.lightutils.Commands.FreeCamCommand;
import ink.neokoni.lightutils.LightUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class PlayerDoneAdvancementWhenFreeCamListener implements Listener {
    public PlayerDoneAdvancementWhenFreeCamListener() {
        LightUtils plugin = LightUtils.getInstance();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    private void onAdvancementDone(PlayerAdvancementDoneEvent e) {
        // At least... Right, don't broadcast it... We're watching...
        if (FreeCamCommand.getEnabledMap().containsKey(e.getPlayer())) {
            // Only you know...
            if (e.message()!=null) e.getPlayer().sendMessage(e.message());
            e.message(null);
        }
    }
}
