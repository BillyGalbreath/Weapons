package net.pl3x.bukkit.weapons.weapons;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.inventory.ItemStack;

public class WeaponManager {
    public static final Map<String, BaseWeapon> WEAPONS_REGISTRY = new HashMap<>();

    public static final BaseWeapon RAIN_OF_ARROWS = register("rain_of_arrows", new RainOfArrows());
    public static final BaseWeapon SUPER_PICKAXE = register("super_pickaxe", new SuperPickaxe());
    public static final BaseWeapon THE_HAMMER = register("the_hammer", new TheHammer());
    public static final BaseWeapon SCYTHE = register("scythe", new Scythe());

    private static BaseWeapon register(String name, BaseWeapon weapon) {
        WEAPONS_REGISTRY.put(name, weapon);
        weapon.reload();
        return weapon;
    }

    public static BaseWeapon getWeapon(ItemStack item) {
        for (BaseWeapon weapon : WEAPONS_REGISTRY.values()) {
            if (weapon.equals(item)) {
                return weapon;
            }
        }
        return null;
    }
}
