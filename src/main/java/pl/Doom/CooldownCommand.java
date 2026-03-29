package pl.Doom;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.managers.CooldownManager;

import java.util.UUID;

public class CooldownCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /cooldown
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                CooldownManager.removeAllCooldowns(player.getUniqueId());
                player.sendMessage(ChatColor.GREEN + "Your cooldowns have been cleared!");
                player.setCooldown(Material.SCULK_CATALYST, 1);
                player.updateInventory();
            } else {
                sender.sendMessage(ChatColor.RED + "Only players can clear their own cooldowns.");
            }
            return true;
        }

        // /cooldown <player>
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer != null && targetPlayer.isOnline()) {
            UUID playerUUID = targetPlayer.getUniqueId();
            CooldownManager.removeAllCooldowns(playerUUID);
            sender.sendMessage(ChatColor.GREEN + "Cooldowns cleared for " + targetPlayer.getName() + "!");

            targetPlayer.setCooldown(Material.SCULK_CATALYST, 1);
            targetPlayer.updateInventory();
        } else {
            sender.sendMessage(ChatColor.RED + "Player '" + args[0] + "' not found or not online.");
        }

        return true;
    }
}
