package ink.neokoni.lightutils.DataStorage;

import ink.neokoni.lightutils.LightUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Configs {
    private static File pluginFolder = LightUtils.getInstance().getDataFolder();
    private static LightUtils plugin = LightUtils.getInstance();
    private static YamlConfiguration configs = new YamlConfiguration();
    public static boolean isConfigExist() {
        return new File(pluginFolder, "config.yml").exists();
    }

    public static void createConfigFile() {
        plugin.saveResource("config.yml", false);
    }

    public static void loadConfig() {
        if (!isConfigExist()) {
            createConfigFile();
            return;
        }

        File configFile = new File(pluginFolder, "config.yml");
        configs = YamlConfiguration.loadConfiguration(configFile);
    }


    public static YamlConfiguration getConfigs() {
        return configs;
    }

    public static YamlConfiguration saveConfigs() {
        try {
            File configFile = new File(pluginFolder, "config.yml");
            configs.save(configFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return configs;
    }
}
