package pl.Doom.trust;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TrustCommand implements CommandExecutor {
    private final TrustManager trustManager;

    public TrustCommand(TrustManager trustManager) {
        this.trustManager = trustManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c" + "Only players can use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§c" + "/" + label + " <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage("§c" + args[0] + " is not online");
            return true;
        }

        if (label.equalsIgnoreCase("trust")) {
            if (trustManager.isTrusted(player, target)) {
                player.sendMessage("§a" + target.getName() + " is already trusted.");
            } else {
                trustManager.trustPlayer(player, target);
                player.sendMessage("§a" + "You have trusted " + target.getName() + ".");
            }
        } else if (label.equalsIgnoreCase("untrust")) {
            if (!trustManager.isTrusted(player, target)) {
                player.sendMessage("§e" + target.getName() + " is not trusted.");
            } else {
                trustManager.untrustPlayer(player, target);
                player.sendMessage("§cYou have untrusted " + target.getName() + ".");
            }
        }

        return true;
    }
}