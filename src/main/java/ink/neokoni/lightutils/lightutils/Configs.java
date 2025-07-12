package ink.neokoni.lightutils.lightutils;

import ink.neokoni.lightutils.lightutils.tasks.FixedRateBroadcastMsgTask;
import ink.neokoni.lightutils.lightutils.utils.SeedUtils;
import ink.neokoni.lightutils.lightutils.utils.TextUtils;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Configs {
    private static final  LightUtils plugin = LightUtils.getInstance();
    private static YamlConfiguration config;
    private static YamlConfiguration lang;

    public boolean isFileExist(String fileName){
        return new File(plugin.getDataFolder(),  fileName+".yml").exists();
    }

    public void createFile(String fileName) {
        plugin.saveResource(fileName+".yml", false);
    }

    public void reloadConfig(@Nullable CommandSender sender){
        if (config==null) {
            reloadConfigLogic();
            new FixedRateBroadcastMsgTask().run();
            return;
        }

        ScheduledTask reloadTask = Bukkit.getAsyncScheduler().runNow(plugin, task -> reloadConfigLogic());
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

    public void reloadConfigLogic(){
        config = loadConfig("config");
        lang = loadConfig("lang");

        new SeedUtils();
    }

    public static YamlConfiguration getConfig(String fileName) {
        return switch (fileName) {
            case "config" -> config;
            case "lang" -> lang;
            default -> null;
        };
    }

    public YamlConfiguration loadConfig(String fileName) {
        if(!isFileExist(fileName)){
            createFile(fileName);
        }
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), fileName+".yml"));
    }

    public boolean saveConfig(String fileName, YamlConfiguration config){
        try {
            config.save(new File(plugin.getDataFolder(), fileName+".yml"));

            switch (fileName) {
                case "config" -> this.config = config;
                case "lang" -> lang = config;
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
