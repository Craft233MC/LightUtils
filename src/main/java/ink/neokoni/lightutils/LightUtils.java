package ink.neokoni.lightutils;

import com.github.retrooper.packetevents.PacketEvents;
import ink.neokoni.lightutils.Commands.*;
import ink.neokoni.lightutils.DataStorage.PlayerDatas;
import ink.neokoni.lightutils.Listeners.*;
import ink.neokoni.lightutils.PAPIs.PAPICore;
import ink.neokoni.lightutils.Tasks.AsyncAutoSaveDataTask;
import ink.neokoni.lightutils.Tasks.DetectIsPlayerDeadTask;
import ink.neokoni.lightutils.Utils.BungeeCardChannelUtils;
import ink.neokoni.lightutils.Utils.DataStorageUtils;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public final class LightUtils extends JavaPlugin {
    private static LightUtils instance;
    public static boolean isFolia;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        PacketEvents.getAPI().init();
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
        new DetectIsPlayerDeadTask();

        // init event listeners
        new PlayerJoinLeaveListener();
        new FarmlandRevertListener();
        new PlayerSpawnListener();
        new PlayerDoneAdvancementWhenFreeCamListener();

        // init functions
        new BungeeCardChannelUtils();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PlayerDatas.writeToFile();
        PacketEvents.getAPI().terminate();
    }

    private void regPapi() {
        new PAPICore().register();

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
