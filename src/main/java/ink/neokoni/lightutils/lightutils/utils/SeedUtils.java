package ink.neokoni.lightutils.lightutils.utils;

import ink.neokoni.lightutils.lightutils.Configs;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Random;

public class SeedUtils {
    private static String fakeSeed;
    public static String getRealSeed() {
        return String.valueOf(Bukkit.getWorlds().getFirst().getSeed());
    }

    public static String getFakeSeed() {
        String fakeType = Configs.getConfig("config").getString("fake-seed.fake-type");

        switch (fakeType) {
            case "FIXED" -> {return Configs.getConfig("config").getString("fake-seed.seed");}
            case "RANDOM" -> {return fakeSeed;}
            default -> {
                return getRealSeed();
            }
        }
    }

    public static boolean isReturnFakeSeed(CommandSender sender) {
        boolean functionEnabled = Configs.getConfig("config").getBoolean("fake-seed.enable");
        boolean hadPerms = sender.hasPermission(Configs.getConfig("config").getString("fake-seed.unlock-real-perms"));

        if (!functionEnabled) return false;

        return !hadPerms;

    }

    public void genFakeSeed() {
        int tmpNum=0;
        Random random = new Random();
        while (tmpNum==0){
            tmpNum = random.nextInt(-10, 10);
        }
        fakeSeed = String.valueOf(tmpNum);
        int length = Configs.getConfig("config").getInt("fake-seed.length");

        for (int i = 0; i < length; i++) {
            String tmp = String.valueOf(random.nextInt(0, 9));
            fakeSeed = fakeSeed + tmp;
        }
    }
}
