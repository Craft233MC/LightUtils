package ink.neokoni.lightutils.lightutils;

import ink.neokoni.lightutils.lightutils.commands.FakeSeedCommand;
import ink.neokoni.lightutils.lightutils.commands.MainCommand;
import ink.neokoni.lightutils.lightutils.papis.TickInfoPAPI;
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
        new MainCommand();
        new FakeSeedCommand();

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
