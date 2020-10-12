package net.pl3x.bukkit.weapons.configuration;

import com.google.common.base.Throwables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Lang {
    public static String PLAYER_NOT_ONLINE = "&4Player not online!";
    public static String WEAPON_NOT_FOUND = "&4Weapon not found!";

    public static String BOW_RAIN_OF_ARROWS_NAME = "&bRain of Arrows";
    public static List<String> BOW_RAIN_OF_ARROWS_LORE = Arrays.asList("Shoots entire stacks", "with a single shot");

    private static void init() {
        PLAYER_NOT_ONLINE = getString("player-not-online", PLAYER_NOT_ONLINE);
        WEAPON_NOT_FOUND = getString("weapon-not-found", WEAPON_NOT_FOUND);

        BOW_RAIN_OF_ARROWS_NAME = getString("rain-of-arrows.name", BOW_RAIN_OF_ARROWS_NAME);
        BOW_RAIN_OF_ARROWS_LORE = getStringList("rain-of-arrows.lore", BOW_RAIN_OF_ARROWS_LORE);
    }

    // ############################  DO NOT EDIT BELOW THIS LINE  ############################

    /**
     * Reload the language file
     */
    public static void reload(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), Config.LANGUAGE_FILE);
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException ignore) {
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load " + Config.LANGUAGE_FILE + ", please correct your syntax errors", ex);
            throw Throwables.propagate(ex);
        }
        config.options().header("This is the main language file for " + plugin.getName());
        config.options().copyDefaults(true);

        Lang.init();

        try {
            config.save(configFile);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + configFile, ex);
        }
    }

    private static YamlConfiguration config;

    private static String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    private static List<String> getStringList(String path, List<String> def) {
        config.addDefault(path, def);
        List<String> list = config.getStringList(path);
        return list.isEmpty() ? def : list;
    }

    /**
     * Sends a message to a recipient
     *
     * @param recipient Recipient of message
     * @param message   Message to send
     */
    public static void send(CommandSender recipient, String message) {
        if (recipient != null) {
            for (String part : colorize(message).split("\n")) {
                recipient.sendMessage(part);
            }
        }
    }

    /**
     * Colorize a String
     *
     * @param str String to colorize
     * @return Colorized String
     */
    public static String colorize(String str) {
        if (str == null) {
            return "";
        }
        str = ChatColor.translateAlternateColorCodes('&', str);
        if (ChatColor.stripColor(str).isEmpty()) {
            return "";
        }
        return str;
    }

    /**
     * Colorize a list of strings
     *
     * @param list List of strings to colorize
     * @return Colorized list of strings
     */
    public static List<String> colorize(List<String> list) {
        return list.stream()
                .map(Lang::colorize)
                .collect(Collectors.toList());
    }
}
