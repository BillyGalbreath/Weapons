package net.pl3x.bukkit.weapons.weapons;

import net.pl3x.bukkit.weapons.configuration.Config;
import net.pl3x.bukkit.weapons.configuration.Lang;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TheHammer extends SuperPickaxe {
    public void reload() {
        weapon = new ItemStack(Material.NETHERITE_PICKAXE);
        ItemMeta meta = weapon.getItemMeta();
        meta.setDisplayName(Lang.colorize(Config.THE_HAMMER_NAME));
        meta.setLore(Lang.colorize(Config.THE_HAMMER_LORE));
        meta.setCustomModelData(998);
        weapon.setItemMeta(meta);

        this.length = Config.THE_HAMMER_LENGTH;
        this.width = Config.THE_HAMMER_WIDTH;
        this.height = Config.THE_HAMMER_HEIGHT;
        this.singleType = Config.THE_HAMMER_SINGLE_TYPE;
    }
}
