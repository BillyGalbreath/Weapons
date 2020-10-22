package net.pl3x.bukkit.weapons.weapons;

import net.pl3x.bukkit.weapons.Weapons;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class Scythe extends BaseWeapon {
    private int reach;
    private boolean collectDrops;

    private boolean inProgress;

    public Scythe(String id) {
        super(id);
    }

    @Override
    public void reload() {
        super.reload();

        this.reach = config.getInt("reach", 2);
        this.collectDrops = config.getBoolean("collect-drops", true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (inProgress) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (!this.equals(tool)) {
            return;
        }

        Block block = event.getBlock();
        BlockData data = block.getState().getBlockData();
        if (!(data instanceof Ageable)) {
            return;
        }

        inProgress = true;

        for (int x = -reach; x <= reach; x++) {
            for (int y = -reach; y <= reach; y++) {
                for (int z = -reach; z <= reach; z++) {
                    breakBlock(player, block.getRelative(x, y, z), tool);
                }
            }
        }

        inProgress = false;

        Material type = block.getType();

        Weapons.delay(() -> {
            Ageable ageable = (Ageable) data;
            if (ageable.getAge() < ageable.getMaximumAge()) {
                block.setBlockData(data, true);
            } else {
                block.setType(type, true);
            }
        }, 1);
    }

    private void breakBlock(Player player, Block block, ItemStack tool) {
        if (block == null) {
            return;
        }

        BlockData data = block.getState().getBlockData();
        if (!(data instanceof Ageable)) {
            return;
        }

        Ageable ageable = (Ageable) data;
        if (ageable.getAge() < ageable.getMaximumAge()) {
            return;
        }

        if (!new BlockBreakEvent(block, player).callEvent()) {
            return;
        }

        Material type = block.getType();
        block.breakNaturally(tool);
        block.setType(type, true);
    }
}
