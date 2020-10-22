package net.pl3x.bukkit.weapons.weapons;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.inventory.ItemStack;

public class WeaponManager {
    public static final Map<String, BaseWeapon> WEAPONS_REGISTRY = new HashMap<>();

    public static final BaseWeapon RAIN_OF_ARROWS = register(new RainOfArrows("rain_of_arrows"));
    public static final BaseWeapon SUPER_PICKAXE = register(new SuperPickaxe("super_pickaxe"));
    public static final BaseWeapon THE_HAMMER = register(new TheHammer("the_hammer"));
    public static final BaseWeapon SCYTHE = register(new Scythe("scythe"));

    private static BaseWeapon register(BaseWeapon weapon) {
        WEAPONS_REGISTRY.put(weapon.getId(), weapon);
        return weapon;
    }

    public static BaseWeapon getWeapon(String id) {
        return WEAPONS_REGISTRY.get(id);
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
