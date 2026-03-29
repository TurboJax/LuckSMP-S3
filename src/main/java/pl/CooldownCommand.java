package pl;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.managers.CooldownManager;

public class CooldownCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player target;
        if (args.length == 0) {
            if (sender instanceof Player player) {
                target = player;
            } else {
                sender.sendMessage("§cYou are not a player");
                return true;
            }
        } else {
            target = Bukkit.getPlayer(args[0]);
        }

        if (target != null && target.isOnline()) {
            CooldownManager.removeAllCooldowns(target.getUniqueId());
            sender.sendMessage("§aCooldowns cleared for " + target.getName() + "!");
        } else {
            sender.sendMessage("§c" + args[0] + " is not online.");
        }

        return true;
    }
}
