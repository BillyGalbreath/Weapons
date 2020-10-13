package net.pl3x.bukkit.weapons.weapons;

import net.pl3x.bukkit.weapons.Weapons;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class BaseWeapon implements Listener {
    ItemStack weapon;

    public BaseWeapon() {
        Bukkit.getPluginManager().registerEvents(this, Weapons.getInstance());
    }

    public abstract void reload();

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
        if (!stack.hasItemMeta()) {
            return false;
        }
        ItemMeta stackMeta = stack.getItemMeta();
        if (!stackMeta.hasCustomModelData()) {
            return true;
        }
        return weapon.getItemMeta().getCustomModelData() == stackMeta.getCustomModelData();
    }
}
