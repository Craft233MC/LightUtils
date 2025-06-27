package ink.neokoni.lightutils.lightutils;

import ink.neokoni.lightutils.lightutils.handler.onGSitTeleport;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class LightUtils extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        regGSitBlocker();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void regGSitBlocker() {
        if (Bukkit.getPluginManager().isPluginEnabled("GSit") &&
                Bukkit.getPluginManager().isPluginEnabled("HuskHomes")) {
            new onGSitTeleport().register(this);
        }
    }
}
