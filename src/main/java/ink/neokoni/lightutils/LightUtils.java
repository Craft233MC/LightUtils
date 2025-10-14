package ink.neokoni.lightutils;

import ink.neokoni.lightutils.Commands.*;
import ink.neokoni.lightutils.DataStorage.Configs;
import ink.neokoni.lightutils.DataStorage.PlayerDatas;
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

        PlayerDatas.loadPlayerData();
        new Configs().reloadConfig(null); // init config files

        // init Commands
        new MainCommand();
        new FakeSeedCommand();
        new PaperPluginsCommand();
        new FreeCamCommand();
        new FlyCommand();

        regPapi();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PlayerDatas.writeToFile();
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
