package ink.neokoni.lightutils.DataStorage;

import ink.neokoni.lightutils.LightUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Languages {
    private static File pluginFolder = LightUtils.getInstance().getDataFolder();
    private static YamlConfiguration language = new YamlConfiguration();
    public static boolean isLanguageExist() {
        return new File(pluginFolder, "lang.yml").exists();
    }

    public static void loadLanguage() {
        if (!isLanguageExist()) {
            LightUtils.getInstance().saveResource("lang.yml", false);
            return;
        }

        File langFile = new File(pluginFolder, "lang.yml");
        language = YamlConfiguration.loadConfiguration(langFile);
    }

    public static YamlConfiguration getLanguages() {
        return language;
    }
}
