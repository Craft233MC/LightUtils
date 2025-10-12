package ink.neokoni.lightutils;

import ink.neokoni.lightutils.Commands.FakeSeedCommand;
import ink.neokoni.lightutils.Commands.FreeCamCommand;
import ink.neokoni.lightutils.Commands.MainCommand;
import ink.neokoni.lightutils.Commands.PaperPluginsCommand;
import ink.neokoni.lightutils.PAPIs.TickInfoPAPI;
import org.bukkit.plugin.java.JavaPlugin;

public final class LightUtils extends JavaPlugin {
    private static LightUtils instance;
    public static boolean isFolia;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        markIsFolia();

        new Configs().reloadConfig(null); // init config files

        // init Commands
        new MainCommand();
        new FakeSeedCommand();
        new PaperPluginsCommand();
        new FreeCamCommand();

        regPapi();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void regPapi() {
        new TickInfoPAPI().register();

    }

    public static LightUtils getInstance() {
        return instance;
    }

    private void markIsFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            isFolia = true;
        } catch (ClassNotFoundException e) {
            isFolia = false;
        }
    }
}
