package net.pl3x.bukkit.weapons.weapons;

import java.util.List;
import net.pl3x.bukkit.weapons.Weapons;
import net.pl3x.bukkit.weapons.configuration.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class BaseWeapon implements Listener {
    private final String id;

    private Material material;
    private String name;
    private List<String> lore;
    private int model;

    protected ConfigurationSection config;

    public BaseWeapon(String id) {
        this.id = id;

        Bukkit.getPluginManager().registerEvents(this, Weapons.getInstance());
    }

    public void reload() {
        config = Weapons.getInstance().getConfig().getConfigurationSection(id);
        if (config == null) {
            throw new IllegalArgumentException("Null config for " + id);
        }

        String material = config.getString("material");
        if (material == null || material.isEmpty()) {
            throw new IllegalArgumentException("Null material for " + id);
        }
        this.material = Material.valueOf(material.toUpperCase());

        String name = config.getString("name");
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Missing name for " + id);
        }
        this.name = Lang.colorize(name);
        if (this.name.isEmpty()) {
            throw new IllegalArgumentException("Effectively missing name for " + id);
        }

        this.lore = Lang.colorize(config.getStringList("lore"));

        this.model = config.getInt("model");
        if (this.model == 0) {
            throw new IllegalArgumentException("Missing model for " + id);
        }
    }

    public String getId() {
        return id;
    }

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if (!lore.isEmpty()) {
            meta.setLore(lore);
        }
        meta.setCustomModelData(model);
        item.setItemMeta(meta);
        return item;
    }

    public boolean equals(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (material != stack.getType()) {
            return false;
        }
        if (!stack.hasItemMeta()) {
            return false;
        }
        ItemMeta stackMeta = stack.getItemMeta();
        if (!stackMeta.hasCustomModelData()) {
            return false;
        }
        return model == stackMeta.getCustomModelData();
    }
}
