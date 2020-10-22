package net.pl3x.bukkit.weapons.weapons;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class SuperPickaxe extends BaseWeapon {
    private Material inProgress;
    protected int length, width, height;
    protected boolean singleType;

    public SuperPickaxe(String id) {
        super(id);
    }

    @Override
    public void reload() {
        super.reload();

        this.length = config.getInt("length", 2);
        this.width = config.getInt("width", 2);
        this.height = config.getInt("height", 2);
        this.singleType = config.getBoolean("single-type", true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (inProgress != null) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (!this.equals(tool)) {
            return;
        }

        // variable stuffs
        boolean creative = player.getGameMode() == GameMode.CREATIVE;
        int startX, startY, startZ;
        int endX, endY, endZ;

        BlockFace direction = event.getPlayer().getTargetBlockFace(10);
        if (direction == null) {
            return; // sanity check (should never happen)
        }

        // figure out how to loop the blocks (direction)
        switch (direction) {
            case NORTH:
            case SOUTH:
            default:
                startX = -length;
                endX = length;
                startY = -height;
                endY = height;
                startZ = -width;
                endZ = width;
                break;
            case EAST:
            case WEST:
                startX = -width;
                endX = width;
                startY = -height;
                endY = height;
                startZ = -length;
                endZ = length;
                break;
            case UP:
            case DOWN:
                startX = -length;
                endX = length;
                startY = -width;
                endY = width;
                startZ = -height;
                endZ = height;
                break;
        }

        inProgress = event.getBlock().getType();

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                for (int z = startZ; z <= endZ; z++) {
                    if (!(x == 0 && y == 0 && z == 0)) {
                        breakBlock(player, event.getBlock().getRelative(x, y, z), tool, creative);
                    }
                }
            }
        }

        inProgress = null;
    }

    private void breakBlock(Player player, Block block, ItemStack tool, boolean creative) {
        if (block == null) {
            return;
        }

        Material type = block.getType();
        if (type.isAir() || (singleType && type != inProgress)) {
            return;
        }

        if (!creative && type == Material.BEDROCK) {
            return;
        }

        if (!new BlockBreakEvent(block, player).callEvent()) {
            return;
        }

        if (creative) {
            block.setType(Material.AIR, true);
        } else {
            block.breakNaturally(tool);
        }
    }
}
