package ink.neokoni.lightutils.DataStorage;

import ink.neokoni.lightutils.LightUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class PlayerDatas {
    private static File pluginFolder = LightUtils.getInstance().getDataFolder();
    private static YamlConfiguration playerData = new YamlConfiguration();
    public static boolean isDataStorageExist() {
        return new File(pluginFolder, "PlayerData.yml").exists();
    }

    public static void createDataFile() {
        try  {
            File file = new File(pluginFolder, "PlayerData.yml");
            playerData.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "FAILED TO CREATE PlayerData FILE!");
        }
    }

    public static void loadPlayerData() {
        if (!isDataStorageExist()) {
            createDataFile();
            return;
        }

        File dataFile = new File(pluginFolder, "PlayerData.yml");
        playerData = YamlConfiguration.loadConfiguration(dataFile);
    }

    public static void savePlayerData(YamlConfiguration data) {
        File dataFile = new File(pluginFolder, "PlayerData.yml");
        try {
            data.save(dataFile);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "FAILED TO SAVE PlayerData FILE!");
        }
    }

    public static YamlConfiguration getPlayerData() {
        return playerData;
    }
}
