package pl.Doom;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.CooldownCommand;
import pl.Main;
import pl.managers.CooldownManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActionBarUpdater extends BukkitRunnable {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();

            AbilityMapping book = Main.getAbilityManager().getAbility(uuid);

            if (book == null)return;
            String color = book.getColor();
            String cooldown = formatTime(CooldownManager.getCooldownTimeLeft(uuid, "1"));
            String cooldown2 = "";
            if (CooldownManager.isOnCooldown(uuid, "2")) {
                cooldown2 = "    §7|    " + color + formatTime(CooldownManager.getCooldownTimeLeft(uuid, "2"));
            }
            player.sendActionBar(color + cooldown + cooldown2);
        }
    }

    private String formatTime(long totalMS) {
        long seconds = totalMS / 1000;
        if (seconds <= 0){
            return "";
        }
        return seconds + "s";
    }
}
