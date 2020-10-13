package net.pl3x.bukkit.weapons.configuration;

import com.google.common.base.Throwables;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Config {
    public static String LANGUAGE_FILE = "lang-en.yml";

    public static String RAIN_OF_ARROWS_NAME = "&bRain of Arrows";
    public static List<String> RAIN_OF_ARROWS_LORE = Arrays.asList("Shoots entire stacks", "with a single shot");
    public static double RAIN_OF_ARROWS_POWER = 3.0D;
    public static double RAIN_OF_ARROWS_INACCURACY = 5.0D;
    public static boolean RAIN_OF_ARROWS_RESET_DAMAGE_TICKS = false;

    public static String SUPER_PICKAXE_NAME = "&rSuper Pickaxe";
    public static List<String> SUPER_PICKAXE_LORE = Arrays.asList("Breaks multiple blocks", "of the same type");
    public static int SUPER_PICKAXE_LENGTH = 2;
    public static int SUPER_PICKAXE_WIDTH = 2;
    public static int SUPER_PICKAXE_HEIGHT = 2;
    public static boolean SUPER_PICKAXE_SINGLE_TYPE = true;

    public static String THE_HAMMER_NAME = "&rThe Hammer";
    public static List<String> THE_HAMMER_LORE = Arrays.asList("Breaks multiple blocks", "of any type");
    public static int THE_HAMMER_LENGTH = 2;
    public static int THE_HAMMER_WIDTH = 0;
    public static int THE_HAMMER_HEIGHT = 2;
    public static boolean THE_HAMMER_SINGLE_TYPE = false;

    public static String SCYTHE_NAME = "&rScythe";
    public static List<String> SCYTHE_LORE = Arrays.asList("Breaks multiple blocks", "of any type");
    public static int SCYTHE_REACH = 2;
    public static boolean SCYTHE_COLLECT_DROPS = true;

    private static void init() {
        LANGUAGE_FILE = getString("language-file", LANGUAGE_FILE);

        RAIN_OF_ARROWS_NAME = getString("rain-of-arrows.name", RAIN_OF_ARROWS_NAME);
        RAIN_OF_ARROWS_LORE = getStringList("rain-of-arrows.lore", RAIN_OF_ARROWS_LORE);
        RAIN_OF_ARROWS_POWER = getDouble("rain-of-arrows.power", RAIN_OF_ARROWS_POWER);
        RAIN_OF_ARROWS_INACCURACY = getDouble("rain-of-arrows.inaccuracy", RAIN_OF_ARROWS_INACCURACY);
        RAIN_OF_ARROWS_RESET_DAMAGE_TICKS = getBoolean("rain-of-arrows.reset-damage-ticks", RAIN_OF_ARROWS_RESET_DAMAGE_TICKS);

        SUPER_PICKAXE_NAME = getString("super-pickaxe.name", SUPER_PICKAXE_NAME);
        SUPER_PICKAXE_LORE = getStringList("super-pickaxe.lore", SUPER_PICKAXE_LORE);
        SUPER_PICKAXE_LENGTH = getInt("super-pickaxe.length", SUPER_PICKAXE_LENGTH);
        SUPER_PICKAXE_WIDTH = getInt("super-pickaxe.width", SUPER_PICKAXE_WIDTH);
        SUPER_PICKAXE_HEIGHT = getInt("super-pickaxe.height", SUPER_PICKAXE_HEIGHT);
        SUPER_PICKAXE_SINGLE_TYPE = getBoolean("super-pickaxe.single-type", SUPER_PICKAXE_SINGLE_TYPE);

        THE_HAMMER_NAME = getString("the-hammer.name", THE_HAMMER_NAME);
        THE_HAMMER_LORE = getStringList("the-hammer.lore", THE_HAMMER_LORE);
        THE_HAMMER_LENGTH = getInt("the-hammer.length", THE_HAMMER_LENGTH);
        THE_HAMMER_WIDTH = getInt("the-hammer.width", THE_HAMMER_WIDTH);
        THE_HAMMER_HEIGHT = getInt("the-hammer.height", THE_HAMMER_HEIGHT);
        THE_HAMMER_SINGLE_TYPE = getBoolean("the-hammer.single-type", THE_HAMMER_SINGLE_TYPE);
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

    private static List<String> getStringList(String path, List<String> def) {
        config.addDefault(path, def);
        List<String> list = config.getStringList(path);
        return list.isEmpty() ? def : list;
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
