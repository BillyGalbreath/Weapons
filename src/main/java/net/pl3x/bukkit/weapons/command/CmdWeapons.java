package net.pl3x.bukkit.weapons.command;

import net.pl3x.bukkit.weapons.weapons.BaseWeapon;
import net.pl3x.bukkit.weapons.weapons.WeaponManager;
import net.pl3x.bukkit.weapons.Weapons;
import net.pl3x.bukkit.weapons.configuration.Config;
import net.pl3x.bukkit.weapons.configuration.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CmdWeapons implements TabExecutor {
    private final Weapons plugin;

    public CmdWeapons(Weapons plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && sender.hasPermission("command.weapons")) {
            return Stream.of("reload", "give")
                    .filter(arg -> arg.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("command.weapons")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        String response = "&d" + plugin.getName() + " v" + plugin.getDescription().getVersion();

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("give")) {
                if (args.length > 1) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        Lang.send(sender, Lang.PLAYER_NOT_ONLINE);
                        return true;
                    }

                    if (args.length > 2) {
                        BaseWeapon weapon = WeaponManager.getWeapon(args[2]);
                        if (weapon == null) {
                            Lang.send(sender, Lang.WEAPON_NOT_FOUND);
                            return true;
                        }

                        target.getInventory().addItem(weapon.getItemStack()).forEach((count, leftover) -> {
                            Item dropped = target.getWorld().dropItem(target.getLocation(), leftover);
                            dropped.setOwner(target.getUniqueId());
                            dropped.setCanMobPickup(false);
                            dropped.setPickupDelay(0);
                        });

                        return true;
                    }
                }
                return false; // show command usage
            }

            if (args[0].equalsIgnoreCase("reload")) {
                Config.reload(plugin);
                Lang.reload(plugin);

                WeaponManager.reload();

                response += " reloaded";
            }
        }

        Lang.send(sender, response);
        return true;
    }
}
