package ink.neokoni.lightutils.PAPIs;

import ink.neokoni.lightutils.LightUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PAPICore extends PlaceholderExpansion {
    private final LightUtils plugin = LightUtils.getInstance();
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
                return LightUtils.isFolia? TickInfoPAPI.getFoliaMSPT(player) : TickInfoPAPI.getPaperMSPT();
            }
            case "tps": {
                return LightUtils.isFolia? TickInfoPAPI.getFoliaTPS(player) : TickInfoPAPI.getPaperTPS();
            }
            default: return null;
        }
    }
}
