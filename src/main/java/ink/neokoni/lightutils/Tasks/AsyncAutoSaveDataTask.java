package ink.neokoni.lightutils.Tasks;

import ink.neokoni.lightutils.DataStorage.PlayerDatas;
import ink.neokoni.lightutils.LightUtils;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;

public class AsyncAutoSaveDataTask {
    private final LightUtils plugin = LightUtils.getInstance();
    public AsyncAutoSaveDataTask() {
        Bukkit.getAsyncScheduler().runAtFixedRate(
                plugin,
                task -> {
                    PlayerDatas.writeToFile();
                },
                5,
                5,
                TimeUnit.MINUTES
        );
    }
}
