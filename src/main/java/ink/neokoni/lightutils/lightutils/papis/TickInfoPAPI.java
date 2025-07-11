package ink.neokoni.lightutils.lightutils.papis;

import ink.neokoni.lightutils.lightutils.LightUtils;
import io.papermc.paper.threadedregions.TickRegionScheduler;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TickInfoPAPI extends PlaceholderExpansion {
    private final LightUtils plugin = LightUtils.getInstance();
    private static String mspt = "0.00";
    private static String tps = "0.00";
    @Override
    public @NotNull String getIdentifier() {
        return "lightutils";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.valueOf(plugin.getPluginMeta().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        switch (params) {
            case "mspt": {
                Bukkit.getAsyncScheduler().runNow(plugin, task -> {
                    if (LightUtils.isFolia){
                        getFoliaMSPT(player);
                    } else {
                        getPaperMSPT();
                    }
                });
                return mspt;
            }
            case "tps": {
                Bukkit.getAsyncScheduler().runNow(plugin, task -> {
                    if (LightUtils.isFolia){
                        getFoliaTPS(player);
                    } else {
                        getPaperTPS();
                    }
                });
                return tps;
            }
            default: return null;
        }
    }

    public void getFoliaMSPT(Player player) {
        Bukkit.getRegionScheduler().run(plugin, player.getLocation(), task -> {
            long nanoTime = System.nanoTime();
            TickRegionScheduler.RegionScheduleHandle handle = TickRegionScheduler
                    .getCurrentRegion()
                    .getData().getRegionSchedulingHandle();

            double raw = handle.getTickReport5s(nanoTime)
                    .timePerTickData().segmentAll().average() / 1.0E6;

            mspt = String.format("%.2f", raw);
        });
    }

    public void getPaperMSPT() {
        double raw = Bukkit.getAverageTickTime();
        mspt = String.format("%.2f", raw);
    }

    public void getFoliaTPS(Player player) {
        double raw = Objects.requireNonNull(Bukkit.getRegionTPS(player.getLocation()))[0];
        tps = String.format("%.1f", raw);
    }

    public void getPaperTPS() {
        double raw = Bukkit.getTPS()[0];
        tps = String.format("%.1f", raw);
    }
}
