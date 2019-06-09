package net.pl3x.bukkit.weapons.weapons;

import net.pl3x.bukkit.weapons.Weapons;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class BaseWeapon implements Listener {
    ItemStack weapon;

    BaseWeapon() {
        Bukkit.getPluginManager().registerEvents(this, Weapons.getInstance());
    }

    abstract void reload();

    public ItemStack getItemStack() {
        return weapon.clone();
    }

    public boolean equals(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack == weapon) {
            return true;
        }
        if (weapon.getType() != stack.getType()) {
            return false;
        }
        if (weapon.hasItemMeta() != stack.hasItemMeta()) {
            return false;
        }
        if (weapon.hasItemMeta()) {
            ItemMeta weaponMeta = weapon.getItemMeta();
            ItemMeta stackMeta = stack.getItemMeta();
            if (weaponMeta == stackMeta) {
                return true;
            }
            if (!hasSameName(weaponMeta, stackMeta)) {
                return false;
            }
            if (!hasSameLore(weaponMeta, stackMeta)) {
                return false;
            }
            if (!hasSameEnchants(weaponMeta, stackMeta)) {
                return false;
            }
            if (!hasSameAttributeModifiers(weaponMeta, stackMeta)) {
                return false;
            }
            if (!hasSameLocalizedName(weaponMeta, stackMeta)) {
                return false;
            }
            if (!hasSameCustomModelData(weaponMeta, stackMeta)) {
                return false;
            }
            if (!hasSameDestroyableKeys(weaponMeta, stackMeta)) {
                return false;
            }
            if (!hasSamePlaceableKeys(weaponMeta, stackMeta)) {
                return false;
            }
            if (!hasSameItemFlags(weaponMeta, stackMeta)) {
                return false;
            }
            return true;
        }
        return (!weapon.hasItemMeta() || Bukkit.getItemFactory().equals(weapon.getItemMeta(), stack.getItemMeta()));
    }

    private static boolean hasSameName(ItemMeta meta1, ItemMeta meta2) {
        if (meta1.hasDisplayName() != meta2.hasDisplayName()) {
            return false;
        }
        return !meta1.hasDisplayName() || meta1.getDisplayName().equals(meta2.getDisplayName());
    }

    private static boolean hasSameLore(ItemMeta meta1, ItemMeta meta2) {
        if (meta1.hasLore() != meta2.hasLore()) {
            return false;
        }
        return !meta1.hasLore() || meta1.getLore().equals(meta2.getLore());
    }

    private static boolean hasSameEnchants(ItemMeta meta1, ItemMeta meta2) {
        if (meta1.hasEnchants() != meta2.hasEnchants()) {
            return false;
        }
        return !meta1.hasEnchants() || meta1.getEnchants().equals(meta2.getEnchants());
    }

    private static boolean hasSameAttributeModifiers(ItemMeta meta1, ItemMeta meta2) {
        if (meta1.hasAttributeModifiers() != meta2.hasAttributeModifiers()) {
            return false;
        }
        return !meta1.hasAttributeModifiers() || meta1.getAttributeModifiers().equals(meta2.getAttributeModifiers());
    }

    private static boolean hasSameLocalizedName(ItemMeta meta1, ItemMeta meta2) {
        if (meta1.hasLocalizedName() != meta2.hasLocalizedName()) {
            return false;
        }
        return !meta1.hasLocalizedName() || meta1.getLocalizedName().equals(meta2.getLocalizedName());
    }

    private static boolean hasSameCustomModelData(ItemMeta meta1, ItemMeta meta2) {
        if (meta1.hasCustomModelData() != meta2.hasCustomModelData()) {
            return false;
        }
        return !meta1.hasCustomModelData() || meta1.getCustomModelData() == meta2.getCustomModelData();
    }

    private static boolean hasSameDestroyableKeys(ItemMeta meta1, ItemMeta meta2) {
        if (meta1.hasDestroyableKeys() != meta2.hasDestroyableKeys()) {
            return false;
        }
        return !meta1.hasDestroyableKeys() || meta1.getDestroyableKeys().equals(meta2.getDestroyableKeys());
    }

    private static boolean hasSamePlaceableKeys(ItemMeta meta1, ItemMeta meta2) {
        if (meta1.hasPlaceableKeys() != meta2.hasPlaceableKeys()) {
            return false;
        }
        return !meta1.hasPlaceableKeys() || meta1.getPlaceableKeys().equals(meta2.getPlaceableKeys());
    }

    private static boolean hasSameItemFlags(ItemMeta meta1, ItemMeta meta2) {
        return meta1.getItemFlags().equals(meta2.getItemFlags());
    }
}
