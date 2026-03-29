package pl.Doom.Abilities;

import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import pl.Doom.AbilityMapping;
import pl.Main;
import pl.managers.CooldownManager;

public class AdrenalineRush implements Listener {
    @EventHandler
    public void attack(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Player player))return;
        if (event.isCancelled())return;
        if (player.getAttackCooldown() < 0.8f)return;
        String name = "adrenaline_rush";
        if (Main.getAbilityManager().getAbility(player.getUniqueId()) != AbilityMapping.ADRENALINE_RUSH)return;
        int chance = Main.getInstance().getConfig().getInt("chances." + name, 20);
        int duration = Main.getInstance().getConfig().getInt("durations." + name, 35);

        if (CooldownManager.isEffectActive(player.getUniqueId(), name + "_active")){
            upgradeHaste(player);
            return;
        }
        if (CooldownManager.isOnCooldown(player.getUniqueId(), "1"))return;

        if ((Math.random() * 100) < chance){
            if (!(event.getEntity() instanceof LivingEntity e))return;
            if (Main.isTrusted(player, e))return;
            CooldownManager.setDuration(player.getUniqueId(), name + "_active", duration);
            ability(player, duration);
        }
    }

    public static void ability(Player player, int duration){
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        int cooldown = Main.getInstance().getConfig().getInt("cooldowns.adrenaline_rush", 90);
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, duration * 20, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, duration * 20, 2));
        new BukkitRunnable(){
            int i = 0;
                @Override
                public void run() {
                    if (i >= (duration * 20)){
                        CooldownManager.setCooldown(player.getUniqueId(), "1", cooldown);
                         cancel();
                         return;
                    }
                    int radius = 1;
                    for (int i = 0; i < 360; i+=60){
                        Location loc = player.getLocation();
                        World world = player.getWorld();

                        double angle = Math.toRadians(i);
                        double x = loc.getX() + (radius * Math.cos(angle));
                        double y = loc.getY();
                        double z = loc.getZ() + (radius * Math.sin(angle));
                        Location particleLoc = new Location(world, x, y, z);
                        loc.getWorld().spawnParticle(Particle.DUST, particleLoc, 0, 0, 0, 0, 0, new Particle.DustOptions(Color.FUCHSIA, 1F));
                        loc.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, particleLoc, 0, 0, 0, 0, 0);
                    }


                    i++;
            }
        }.runTaskTimer(Main.getInstance(), 0, 1);

    }

    public static void upgradeHaste(Player player){
        if (player.getPotionEffect(PotionEffectType.HASTE) == null)return;
        int duration = player.getPotionEffect(PotionEffectType.HASTE).getDuration();
        int amplifier = player.getPotionEffect(PotionEffectType.HASTE).getAmplifier();

        int max = Main.getInstance().getConfig().getInt("AdrenalineRushHasteMax", 10);
        if (amplifier >= max)return;
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, duration, amplifier + 1));
    }

}
