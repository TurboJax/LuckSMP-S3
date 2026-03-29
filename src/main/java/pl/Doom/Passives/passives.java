package pl.Doom.Passives;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import pl.Doom.AbilityMapping;
import pl.Main;

public class passives implements Listener {
    public static void task(){
        new BukkitRunnable(){
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()){
                    AbilityMapping luck = Main.getAbilityManager().getAbility(p.getUniqueId());
                    if (luck == null)continue;
                    switch (luck){
                        case HELLSCORCH -> p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 40, 0));
                        case THUNDERCHARGE -> p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 0));
                        case ADRENALINE_RUSH -> p.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 40, 0));
                        case RAW_STRENGTH -> p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 40, 0));

                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 20);
    }

    @EventHandler
    public void potion(EntityPotionEffectEvent event){
        if (!(event.getEntity() instanceof Player player))return;
        if (event.getNewEffect() == null) return;
        if (!isNegative(event.getNewEffect().getType()))return;
        AbilityMapping luck = Main.getAbilityManager().getAbility(player.getUniqueId());
        if (luck == null)return;
        if (luck == AbilityMapping.DARK_SENSE){
            event.setCancelled(true);
        }
    }

    private boolean isNegative(PotionEffectType type) {
        return type == PotionEffectType.BLINDNESS
                || type == PotionEffectType.DARKNESS
                || type == PotionEffectType.HUNGER
                || type == PotionEffectType.POISON
                || type == PotionEffectType.SLOWNESS
                || type == PotionEffectType.MINING_FATIGUE
                || type == PotionEffectType.SLOW_FALLING
                || type == PotionEffectType.WEAKNESS
                || type == PotionEffectType.WITHER;
    }
}
