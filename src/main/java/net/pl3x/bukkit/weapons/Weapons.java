package net.pl3x.bukkit.weapons;

import net.pl3x.bukkit.weapons.command.CmdWeapons;
import net.pl3x.bukkit.weapons.configuration.Config;
import net.pl3x.bukkit.weapons.configuration.Lang;
import net.pl3x.bukkit.weapons.weapons.WeaponManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Weapons extends JavaPlugin {
    private static Weapons instance;

    public Weapons() {
        instance = this;
    }

    public void onEnable() {
        Config.reload(this);
        Lang.reload(this);

        WeaponManager.WEAPONS.forEach((name, weapon) -> weapon.reload());

        getCommand("weapons").setExecutor(new CmdWeapons(this));
    }

    public static Weapons getInstance() {
        return instance;
    }
}
