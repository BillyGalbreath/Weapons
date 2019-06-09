package net.pl3x.bukkit.weapons.weapons;

public class WeaponManager {
    public static final RainOfArrows RAIN_OF_ARROWS = new RainOfArrows();

    public static void reload() {
        RAIN_OF_ARROWS.reload();
    }

    public static BaseWeapon getWeapon(String name) {
        switch (name.toUpperCase()) {
            case "RAIN_OF_ARROWS":
                return RAIN_OF_ARROWS;
            default:
                return null;
        }
    }
}
