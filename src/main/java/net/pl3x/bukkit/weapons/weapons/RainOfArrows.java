package net.pl3x.bukkit.weapons.weapons;

import java.util.concurrent.ThreadLocalRandom;
import net.pl3x.bukkit.weapons.Weapons;
import org.bukkit.GameMode;
import org.bukkit.entity.AbstractArrow;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class RainOfArrows extends BaseWeapon {
    private static final double DEG2RAD = Math.PI / 180;
    private static final FixedMetadataValue FIXED_META = new FixedMetadataValue(Weapons.getInstance(), true);

    private double power;
    private double inaccuracy;
    private boolean resetDamageTicks;

    public RainOfArrows(String id) {
        super(id);
    }

    @Override
    public void reload() {
        super.reload();

        this.power = config.getDouble("power", 3.0D);
        this.inaccuracy = config.getDouble("inaccuracy", 5.0D);
        this.resetDamageTicks = config.getBoolean("reset-damage-ticks", false);
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
        if (!this.equals(bow)) {
            return;
        }

        ItemStack stack = event.getConsumable();
        if (stack == null || stack.getAmount() < 2) {
            return; // do not trigger special ability for single arrows
        }

        event.setCancelled(true);

        Player player = (Player) event.getEntity();

        double pitch = player.getEyeLocation().getPitch() * DEG2RAD;
        double yaw = player.getEyeLocation().getYaw() * DEG2RAD;

        double x = -Math.sin(yaw) * Math.cos(pitch);
        double y = -Math.sin(pitch);
        double z = Math.cos(yaw) * Math.cos(pitch);

        boolean creative = player.getGameMode() == GameMode.CREATIVE;

        ThreadLocalRandom rand = ThreadLocalRandom.current();

        int amount = stack.getAmount();
        for (int i = 0; i < amount; i++) {
            Arrow arrow = player.launchProjectile(Arrow.class, new Vector(
                    x + rand.nextGaussian() * 0.0075D * inaccuracy,
                    y + rand.nextGaussian() * 0.0075D * inaccuracy,
                    z + rand.nextGaussian() * 0.0075D * inaccuracy
            ).multiply(power));
            arrow.setShooter(player);
            arrow.setMetadata("RainOfArrows", FIXED_META);
            arrow.setCritical(rand.nextBoolean());
            if (creative) {
                arrow.setPickupStatus(AbstractArrow.PickupStatus.CREATIVE_ONLY);
            }
        }

        if (creative) {
            event.setConsumeItem(false);
        } else {
            stack.setAmount(0);
            event.setConsumeItem(true);
            if (bow != null && bow.damage(amount)) {
                player.broadcastItemBreak(player.getInventory().getItemInMainHand().equals(bow) ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onArrowHit(ProjectileHitEvent event) {
        if (!resetDamageTicks) {
            return;
        }

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
        ((LivingEntity) entity).setNoDamageTicks(0);
    }
}
