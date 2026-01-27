package ink.neokoni.lightutils;

import ink.neokoni.lightutils.Commands.*;
import ink.neokoni.lightutils.DataStorage.PlayerDatas;
import ink.neokoni.lightutils.Listeners.*;
import ink.neokoni.lightutils.PAPIs.TickInfoPAPI;
import ink.neokoni.lightutils.Tasks.AsyncAutoSaveDataTask;
import ink.neokoni.lightutils.Utils.DataStorageUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class LightUtils extends JavaPlugin {
    private static LightUtils instance;
    public static boolean isFolia;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        markIsFolia();

        // init DataStorage
        DataStorageUtils.initAll();

        // init Commands
        new LightUtilsCommand();
        new FakeSeedCommand();
        new PaperPluginsCommand();
        new FreeCamCommand();
        new FlyCommand();
        new SetWorldSpawnCommand();

        // init external plugins support
        regPapi();

        // start tasks
        new AsyncAutoSaveDataTask();

        // init event listeners
        new PlayerJoinLeaveListener();
        new FarmlandRevertListener();
        new PlayerSpawnListener();
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
