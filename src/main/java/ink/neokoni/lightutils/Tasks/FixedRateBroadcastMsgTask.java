package ink.neokoni.lightutils.Tasks;

import ink.neokoni.lightutils.DataStorage.Configs;
import ink.neokoni.lightutils.LightUtils;
import ink.neokoni.lightutils.Utils.TextUtils;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FixedRateBroadcastMsgTask {
    private final LightUtils plugin = LightUtils.getInstance();
    public void run() {
        boolean enabled = Configs.getConfigs().getBoolean("broadcast-msg.enable");
        List<String> msgs = Configs.getConfigs().getStringList("broadcast-msg.messages");
        int interval = Configs.getConfigs().getInt("broadcast-msg.interval");

        if (!enabled) {
            return;
        }

        Bukkit.getAsyncScheduler().runAtFixedRate(plugin, task -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                msgs.forEach(msg -> {
                    player.sendMessage(TextUtils.colored(msg));
                });
            });
        }, 5, interval, TimeUnit.SECONDS);
    }
}
