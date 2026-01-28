package ink.neokoni.lightutils.PAPIs;

import ink.neokoni.lightutils.DataStorage.Languages;
import ink.neokoni.lightutils.Utils.TextUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.time.LocalDateTime;

public class GreetingWordsPAPI {
    public static String get() {
        YamlConfiguration lang = Languages.getLanguages();
        int currentHour = LocalDateTime.now().getHour();
        for (String s: lang.getConfigurationSection("greetings").getKeys(false)) {
            String currentGreetingPath = "greetings." + s;
            if (s.contains("-")) {
                String[] range = s.split("-");
                int start = Integer.parseInt(range[0]);
                int end = Integer.parseInt(range[1]);
                if (currentHour >= start && currentHour <= end) {
                    return TextUtils.getLangString(currentGreetingPath);
                }
            } else {
                if (String.valueOf(currentHour).equals(s)) {
                    return TextUtils.getLangString(currentGreetingPath);
                }
            }
        }
        return "Null";
    }
}
