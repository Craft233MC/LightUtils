package ink.neokoni.lightutils.PAPIs;

import ink.neokoni.lightutils.LightUtils;
import io.papermc.paper.threadedregions.TickRegionScheduler;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class TickInfoPAPI extends PlaceholderExpansion {
    private final LightUtils plugin = LightUtils.getInstance();

    // realtime get mspt is too slow for velocitab
    // it cant get mspt, so add HashMap as cache
    private static HashMap<Player, Double> msptInfo = new HashMap<>();
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
                if (LightUtils.isFolia){
                    return getFoliaMSPT(player);
                } else {
                    return getPaperMSPT();
                }
            }
            case "tps": {
                if (LightUtils.isFolia){
                    return getFoliaTPS(player);
                } else {
                    return getPaperTPS();
                }
            }
            default: return null;
        }
    }

    public String getFoliaMSPT(Player player) {
        Bukkit.getRegionScheduler().run(plugin, player.getLocation(), task ->{
            long nanoTime = System.nanoTime();
            TickRegionScheduler.RegionScheduleHandle handle = TickRegionScheduler
                    .getCurrentRegion()
                    .getData().getRegionSchedulingHandle();

            double raw = handle.getTickReport5s(nanoTime)
                    .timePerTickData().segmentAll().average() / 1.0E6;
            msptInfo.put(player, raw);
        });

        if (msptInfo.get(player) == null) return "2.33"; // is get mspt task have yet done

        return String.format("%.2f", msptInfo.get(player));
    }

    public String getPaperMSPT() {
        double raw = Bukkit.getAverageTickTime();
        return String.format("%.2f", raw);
    }

    public String getFoliaTPS(Player player) {
        double raw = Objects.requireNonNull(Bukkit.getRegionTPS(player.getLocation()))[0];
        String result = String.format("%.1f", raw);
        if (raw > 20.0) result = "*20.0";

        return result;
    }

    public String getPaperTPS() {
        double raw = Bukkit.getTPS()[0];
        String result = String.format("%.1f", raw);
        if (raw > 20.0) result = "*20.0";

        return result;
    }
}
