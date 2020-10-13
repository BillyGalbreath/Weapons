package net.pl3x.bukkit.weapons;

import net.pl3x.bukkit.weapons.command.CmdWeapons;
import net.pl3x.bukkit.weapons.configuration.Config;
import net.pl3x.bukkit.weapons.configuration.Lang;
import net.pl3x.bukkit.weapons.weapons.WeaponManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Weapons extends JavaPlugin {
    private static Weapons instance;

    public Weapons() {
        instance = this;
    }

    public void onEnable() {
        Config.reload(this);
        Lang.reload(this);

        WeaponManager.WEAPONS_REGISTRY.forEach((name, weapon) -> weapon.reload());

        PluginCommand command = getCommand("weapons");
        if (command != null) {
            command.setExecutor(new CmdWeapons(this));
        }
    }

    public static Weapons getInstance() {
        return instance;
    }

    public static BukkitTask delay(Runnable runnable, int delay) {
        return instance.getServer().getScheduler().runTaskLater(instance, runnable, delay);
    }
}
