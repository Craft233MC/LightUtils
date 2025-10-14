package ink.neokoni.lightutils.Utils;

import ink.neokoni.lightutils.DataStorage.Configs;
import ink.neokoni.lightutils.DataStorage.Languages;
import ink.neokoni.lightutils.DataStorage.PlayerDatas;
import ink.neokoni.lightutils.LightUtils;
import ink.neokoni.lightutils.Tasks.FixedRateBroadcastMsgTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.concurrent.TimeUnit;

public class DataStorageUtils {
    public static void loadDataOnly() {
        Configs.loadConfig();
        Languages.loadLanguage();
        PlayerDatas.isPlayerDataExist();
    }
    public static void initAll() {
        loadDataOnly();

        new FixedRateBroadcastMsgTask().run();
    }

    public static void reloadAllDataAsync(CommandSender sender) {
        LightUtils plugin = LightUtils.getInstance();
        ScheduledTask reloadTask = Bukkit.getAsyncScheduler().runNow(plugin, task -> loadDataOnly());
        if (sender != null){
            Bukkit.getAsyncScheduler().runAtFixedRate(plugin, task -> {
                if (reloadTask.getExecutionState().equals(ScheduledTask.ExecutionState.FINISHED)){
                    sender.sendMessage(TextUtils.getLang("reload"));
                    Bukkit.getGlobalRegionScheduler().cancelTasks(plugin);
                    Bukkit.getAsyncScheduler().cancelTasks(plugin);

                    sender.sendMessage(TextUtils.getLang("reload-stopped-task"));

                    new FixedRateBroadcastMsgTask().run();
                    sender.sendMessage(TextUtils.getLang("reload-started-task"));

                    task.cancel();
                }
            }, 50, 50, TimeUnit.MILLISECONDS); // 1000 / 20 = 50ms = 1tick

        }
    }
}
