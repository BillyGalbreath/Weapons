package net.pl3x.bukkit.weapons.weapons;

import java.util.HashMap;
import java.util.Map;

public class WeaponManager {
    public static final RainOfArrows RAIN_OF_ARROWS = new RainOfArrows();

    public static final Map<String, BaseWeapon> WEAPONS = new HashMap<>();

    static {
        WEAPONS.put("rain_of_arrows", RAIN_OF_ARROWS);
    }
}
