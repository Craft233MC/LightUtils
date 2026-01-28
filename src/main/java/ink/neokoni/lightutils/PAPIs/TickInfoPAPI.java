package ink.neokoni.lightutils.PAPIs;

import ink.neokoni.lightutils.LightUtils;
import io.papermc.paper.threadedregions.TickRegionScheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;

public class TickInfoPAPI {
    // realtime get mspt is too slow for velocitab
    // it cant get mspt, so add HashMap as cache
    private static HashMap<Player, Double> msptInfo = new HashMap<>();
    private static final LightUtils plugin = LightUtils.getInstance();
    public static String getFoliaMSPT(Player player) {
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

    public static String getPaperMSPT() {
        double raw = Bukkit.getAverageTickTime();
        return String.format("%.2f", raw);
    }

    public static String getFoliaTPS(Player player) {
        double raw = Objects.requireNonNull(Bukkit.getRegionTPS(player.getLocation()))[0];
        String result = String.format("%.1f", raw);
        if (raw > 20.0) result = "*20.0";

        return result;
    }

    public static String getPaperTPS() {
        double raw = Bukkit.getTPS()[0];
        String result = String.format("%.1f", raw);
        if (raw > 20.0) result = "*20.0";

        return result;
    }
}
