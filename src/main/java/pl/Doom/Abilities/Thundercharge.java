package pl.Doom.Abilities;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import pl.Doom.AbilityMapping;
import pl.Doom.Utils.TrueDamage;
import pl.Main;
import pl.managers.CooldownManager;

public class Thundercharge implements Listener {
    @EventHandler
    public void attack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (event.isCancelled()) return;
        if (player.getAttackCooldown() < 0.8f) return;
        String name = "thundercharge";
        if (CooldownManager.isOnCooldown(player.getUniqueId(), "1")) return;
        if (Main.getAbilityManager().getAbility(player.getUniqueId())
                != AbilityMapping.THUNDERCHARGE) return;

        int chance = Main.getInstance().getConfig().getInt("chances." + name, 20);
        int damage = Main.getInstance().getConfig().getInt("damage." + name, 3);
        int duration = Main.getInstance().getConfig().getInt("durations." + name, 9);
        int cooldown = Main.getInstance().getConfig().getInt("cooldowns." + name, 35);

        if ((Math.random() * 100) < chance) {
            if (!(event.getEntity() instanceof LivingEntity e)) return;
            if (Main.isTrusted(player, e)) return;
            e.getWorld().strikeLightningEffect(e.getLocation());
            TrueDamage.trueDamage(e, damage, player);
            buff(player, duration);
            CooldownManager.setCooldown(player.getUniqueId(), "1", cooldown);
        }
    }

    public void buff(Player player, int duration) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration * 20, 2));
        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i >= (duration * 20)) {
                    cancel();
                    return;
                }

                int radius = 1;
                for (int i = 0; i < 360; i += 60) {
                    Location loc = player.getLocation();
                    World world = player.getWorld();

                    double angle = Math.toRadians(i);
                    double x = loc.getX() + (radius * Math.cos(angle));
                    double y = loc.getY();
                    double z = loc.getZ() + (radius * Math.sin(angle));
                    Location particleLoc = new Location(world, x, y, z);
                    world.spawnParticle(
                            Particle.DUST,
                            particleLoc,
                            0,
                            0,
                            0,
                            0,
                            0,
                            new Particle.DustOptions(Color.AQUA, 1F));
                    world.spawnParticle(
                            Particle.TRIAL_SPAWNER_DETECTION_OMINOUS,
                            particleLoc,
                            1,
                            0.1,
                            0.1,
                            0.1,
                            0);
                }

                i++;
            }
        }.runTaskTimer(Main.getInstance(), 0, 1);
    }
}
