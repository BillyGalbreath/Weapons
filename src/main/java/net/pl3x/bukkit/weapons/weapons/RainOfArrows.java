package net.pl3x.bukkit.weapons.weapons;

import net.pl3x.bukkit.weapons.Weapons;
import net.pl3x.bukkit.weapons.configuration.Config;
import net.pl3x.bukkit.weapons.configuration.Lang;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public class RainOfArrows extends BaseWeapon {
    private static final double DEG2RAD = Math.PI / 180;
    private static final FixedMetadataValue FIXED_META = new FixedMetadataValue(Weapons.getInstance(), true);

    public void reload() {
        weapon = new ItemStack(Material.BOW);
        ItemMeta meta = weapon.getItemMeta();
        meta.setDisplayName(Lang.colorize(Lang.BOW_RAIN_OF_ARROWS_NAME));
        meta.setLore(Lang.colorize(Lang.BOW_RAIN_OF_ARROWS_LORE));
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 5, true);
        weapon.setItemMeta(meta);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onRainOfArrows(EntityShootBowEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return; // not shot by player
        }

        if (event.getForce() < 1.0) {
            return; // not fully drawn bow
        }

        ItemStack bow = event.getBow();
        if (!WeaponManager.RAIN_OF_ARROWS.equals(bow)) {
            return;
        }

        ItemStack stack = event.getArrowItem();
        if (stack.getAmount() < 2) {
            return; // do not trigger special ability for single arrows
        }

        event.setCancelled(true);

        Player player = (Player) event.getEntity();

        double pitch = player.getEyeLocation().getPitch() * DEG2RAD;
        double yaw = player.getEyeLocation().getYaw() * DEG2RAD;

        double x = -Math.sin(yaw) * Math.cos(pitch);
        double y = -Math.sin(pitch);
        double z = Math.cos(yaw) * Math.cos(pitch);

        double power = Config.RAIN_OF_ARROWS_POWER;
        double inaccuracy = Config.RAIN_OF_ARROWS_INACCURACY;

        ThreadLocalRandom rand = ThreadLocalRandom.current();

        for (int i = 0; i < stack.getAmount(); i++) {
            Arrow arrow = player.launchProjectile(Arrow.class, new Vector(
                    x + rand.nextGaussian() * 0.0075D * inaccuracy,
                    y + rand.nextGaussian() * 0.0075D * inaccuracy,
                    z + rand.nextGaussian() * 0.0075D * inaccuracy
            ).multiply(power));
            arrow.setMetadata("RainOfArrows", FIXED_META);
            arrow.setCritical(true);
        }

        if (player.getGameMode() != GameMode.CREATIVE) {
            stack.setAmount(0);
            event.setConsumeArrow(true);
        } else {
            event.setConsumeArrow(false);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onArrowHit(ProjectileHitEvent event) {
        if (event.getEntityType() != EntityType.ARROW) {
            return;
        }

        Projectile arrow = event.getEntity();
        if (!arrow.hasMetadata("RainOfArrows")) {
            return;
        }

        Entity entity = event.getHitEntity();
        if (!(entity instanceof LivingEntity)) {
            return;
        }

        // allow rapid damage from multiple arrows
        ((LivingEntity) entity).setNoDamageTicks(Config.RAIN_OF_ARROWS_NO_DAMAGE_TICKS);
    }
}
