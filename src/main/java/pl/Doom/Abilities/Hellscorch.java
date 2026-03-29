package pl.Doom.Abilities;

import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import pl.Doom.AbilityMapping;
import pl.Doom.Utils.TrueDamage;
import pl.Main;
import pl.managers.CooldownManager;

public class Hellscorch implements Listener {

    @EventHandler
    public void attack(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Player player))return;
        if (event.isCancelled())return;
        if (player.getAttackCooldown() < 0.8f)return;
        String name = "hellscorch1";
        if (Main.getAbilityManager().getAbility(player.getUniqueId()) != AbilityMapping.HELLSCORCH)return;
        if (player.getFireTicks() < 1)return;
        if (CooldownManager.isOnCooldown(player.getUniqueId(), "1"))return;
        int chance = Main.getInstance().getConfig().getInt("chances." + name, 20);
        double damage = Main.getInstance().getConfig().getDouble("damage." + name, 1.3);
        int cooldown = Main.getInstance().getConfig().getInt("cooldowns." + name, 10);

        if ((Math.random() * 100) < chance){
            if (!(event.getEntity() instanceof LivingEntity e))return;
            if (Main.isTrusted(player, e))return;
            event.setDamage(event.getDamage() * damage);
            e.getWorld().playSound(e.getLocation(), Sound.ENTITY_BLAZE_HURT, 1, 1);
            Location loc = e.getLocation().clone().add(0, 1, 0);
            loc.getWorld().spawnParticle(Particle.FLAME, loc, 30, 0.5, 0.5, 0.5, 0.1);
            loc.getWorld().spawnParticle(Particle.DUST, loc, 30, 0.5, 0.5, 0.5, 0, new Particle.DustOptions(Color.ORANGE, 1F));
            CooldownManager.setCooldown(player.getUniqueId(), "1", cooldown);
        }
    }

    @EventHandler
    public void attack2(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Player player))return;
        if (event.isCancelled())return;
        if (player.getAttackCooldown() < 0.8f)return;
        String name = "hellscorch2";
        if (CooldownManager.isOnCooldown(player.getUniqueId(), "2"))return;
        if (Main.getAbilityManager().getAbility(player.getUniqueId()) != AbilityMapping.HELLSCORCH)return;
        int chance = Main.getInstance().getConfig().getInt("chances." + name, 15);
        double damage = Main.getInstance().getConfig().getDouble("damage." + name, 12);
        int cooldown = Main.getInstance().getConfig().getInt("cooldowns." + name, 60);

        if ((Math.random() * 100) < chance){
            if (!(event.getEntity() instanceof LivingEntity e))return;
            if (Main.isTrusted(player, e))return;
            TrueDamage.trueDamage(e, damage, player);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_ON_FIRE, 1, 1);
            Location loc = e.getLocation().clone().add(0, 1, 0);
            loc.getWorld().spawnParticle(Particle.DUST, loc, 30, 0.5, 0.5, 0.5, 0, new Particle.DustOptions(Color.ORANGE, 1F));
            loc.getWorld().spawnParticle(Particle.LAVA, loc, 30, 0.5, 0.5, 0.5);

            CooldownManager.setCooldown(player.getUniqueId(), "2", cooldown);
        }
    }
}
