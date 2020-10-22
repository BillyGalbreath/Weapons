package net.pl3x.bukkit.weapons.command;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import net.pl3x.bukkit.weapons.weapons.BaseWeapon;
import org.bukkit.entity.Player;

public class CmdWeaponsNew {
    @CommandDescription("Controls the plugin")
    @CommandMethod("command|alias <action> <target> <weapon>")
    private void weapons(
            Player sender,
            @Argument("action") String action,
            @Argument("target") Player target,
            @Argument("weapon") BaseWeapon weapon
    ) {
        sender.sendMessage("Action: " + action);
        sender.sendMessage("Target: " + target.getName());
        sender.sendMessage("Weapon: " + weapon.getId());
    }
}
