package net.pl3x.bukkit.weapons.command;

import net.pl3x.bukkit.weapons.Weapons;
import net.pl3x.bukkit.weapons.configuration.Config;
import net.pl3x.bukkit.weapons.configuration.Lang;
import net.pl3x.bukkit.weapons.weapons.BaseWeapon;
import net.pl3x.bukkit.weapons.weapons.WeaponManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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
        if (sender.hasPermission("command.weapons")) {
            if (args.length == 1) {
                return Stream.of("reload", "give")
                        .filter(arg -> arg.startsWith(args[0].toLowerCase()))
                        .collect(Collectors.toList());
            }
            if (args.length == 2 && args[0].toLowerCase().equals("give")) {
                return Bukkit.getOnlinePlayers().stream()
                        .filter(player -> player.getName().toLowerCase().startsWith(args[1]))
                        .map(HumanEntity::getName)
                        .collect(Collectors.toList());
            }
            if (args.length == 3 && args[0].toLowerCase().equals("give")) {
                return WeaponManager.WEAPONS_REGISTRY.keySet().stream()
                        .filter(name -> name.startsWith(args[2].toLowerCase()))
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
                        BaseWeapon weapon = WeaponManager.WEAPONS_REGISTRY.get(args[2].toLowerCase());
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

                WeaponManager.WEAPONS_REGISTRY.forEach((name, weapon) -> weapon.reload());

                response += " reloaded";
            }
        }

        Lang.send(sender, response);
        return true;
    }
}
