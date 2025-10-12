package ink.neokoni.lightutils.Utils;

import ink.neokoni.lightutils.DataStorage.Configs;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class TextUtils {
    private static String replace(String str, String key, String value) {
        return str.replace(key, value);
    }

    public static Component colored(String str){
        return MiniMessage.miniMessage().deserialize(str);
    }

    public static Component colored(String str, String key, String value) {
        return MiniMessage.miniMessage().deserialize(replace(str, key, value));
    }

    public static Component getLang(String str){
        return colored(Configs.getConfig("lang").getString(str));
    }

    public static Component getLang(String str, String key, String value) {
        return colored(Configs.getConfig("lang").getString(str), key, value);
    }

    public static String getLangString(String str){
        return Configs.getConfig("lang").getString(str);
    }

    public static String getLangString(String str, String key, String value) {
        return replace(Configs.getConfig("lang").getString(str), key, value);
    }
}
