package pl.Doom;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import pl.Main;

public class EquippedLuckCommand implements Listener, CommandExecutor {

    String GUI = "§f\uE900\uE902";

    private void openEquippedBooksGUI(Player player) {
        String GUI2 = GUI;
        Inventory gui = Bukkit.createInventory(null, 9, GUI2);
        AbilityMapping luck = Main.getAbilityManager().getAbility(player.getUniqueId());
        if (luck != null) {
            gui.setItem(4, luck.createItem());
        }

        player.openInventory(gui);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("luck")) {
            if (args.length == 0 && sender instanceof Player player) {
                openEquippedBooksGUI(player);
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void Interact(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            String guiTitle = event.getView().getTitle();
            if (guiTitle.contains(GUI)) {
                event.setCancelled(true);
            }
        }
    }
}
