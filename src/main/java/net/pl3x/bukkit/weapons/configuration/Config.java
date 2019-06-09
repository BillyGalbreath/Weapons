package net.pl3x.bukkit.weapons.configuration;

import com.google.common.base.Throwables;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Config {
    public static String LANGUAGE_FILE = "lang-en.yml";

    public static double RAIN_OF_ARROWS_POWER = 3.0D;
    public static double RAIN_OF_ARROWS_INACCURACY = 5.0D;
    public static int RAIN_OF_ARROWS_NO_DAMAGE_TICKS = 0;

    private static void init() {
        LANGUAGE_FILE = getString("language-file", LANGUAGE_FILE);

        RAIN_OF_ARROWS_POWER = getDouble("rain-of-arrows.power", RAIN_OF_ARROWS_POWER);
        RAIN_OF_ARROWS_INACCURACY = getDouble("rain-of-arrows.inaccuracy", RAIN_OF_ARROWS_INACCURACY);
        RAIN_OF_ARROWS_NO_DAMAGE_TICKS = getInt("rain-of-arrows.no-damage-ticks", RAIN_OF_ARROWS_NO_DAMAGE_TICKS);
    }

    // ############################  DO NOT EDIT BELOW THIS LINE  ############################

    /**
     * Reload the configuration file
     */
    public static void reload(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException ignore) {
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load config.yml, please correct your syntax errors", ex);
            throw Throwables.propagate(ex);
        }
        config.options().header("This is the configuration file for " + plugin.getName());
        config.options().copyDefaults(true);

        Config.init();

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

    private static boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, config.getBoolean(path));
    }

    private static int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInt(path, config.getInt(path));
    }

    private static double getDouble(String path, double def) {
        config.addDefault(path, def);
        return config.getDouble(path, config.getDouble(path));
    }
}
