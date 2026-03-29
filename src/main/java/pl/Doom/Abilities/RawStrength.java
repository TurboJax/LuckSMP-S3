package pl.Doom.Abilities;

import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import pl.Doom.AbilityMapping;
import pl.Main;
import pl.managers.CooldownManager;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class RawStrength implements Listener {

    public static HashMap<UUID, Integer> Combo = new HashMap<>();
    @EventHandler
    public void attack(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Player player))return;
        if (event.isCancelled())return;
        if (player.getAttackCooldown() < 0.8f)return;
        String name = "raw_strength";
        if (Main.getAbilityManager().getAbility(player.getUniqueId()) != AbilityMapping.RAW_STRENGTH)return;
        int chance = Main.getInstance().getConfig().getInt("chances." + name, 35);
        double damage = Main.getInstance().getConfig().getDouble("damage." + name, 1.5);
        int duration = Main.getInstance().getConfig().getInt("durations." + name, 10);
        int cooldown = Main.getInstance().getConfig().getInt("cooldowns." + name, 0);
        int comboForAbility = Main.getInstance().getConfig().getInt("RawStrengthComboForAbility", 2);
        int combo = Combo.getOrDefault(player.getUniqueId(), 0);
        Combo.put(player.getUniqueId(), combo + 1);

        if (Combo.getOrDefault(player.getUniqueId(), 0) < comboForAbility){
            return;
        }

        if (CooldownManager.isEffectActive(player.getUniqueId(), "raw_strength_active")){
            extra(event, damage);
            return;
        }
        if (CooldownManager.isOnCooldown(player.getUniqueId(), "1"))return;

        if ((Math.random() * 100) < chance){
            if (!(event.getEntity() instanceof LivingEntity e))return;
            if (Main.isTrusted(player, e))return;
            extra(event, damage);
            CooldownManager.setDuration(player.getUniqueId(), "raw_strength_active", duration);
            CooldownManager.setCooldown(player.getUniqueId(), "1", cooldown);
        }
    }

    public static void extra(EntityDamageByEntityEvent event, double damage) {
        Random random = new Random();
        float pitch = 1.4f + random.nextFloat() * 0.2f;
        event.setDamage(event.getDamage() * damage);
        event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.BLOCK_ANVIL_LAND, 1, pitch);
        Location loc = event.getEntity().getLocation().clone().add(0, 1, 0);
        loc.getWorld().spawnParticle(Particle.DUST, loc, 5, 0.2, 0.2, 0.2, 0, new Particle.DustOptions(Color.RED, 1F));
    }

    @EventHandler
    public void attackStop(EntityDamageByEntityEvent event){
        if (!(event.getEntity() instanceof Player player))return;
        Combo.put(player.getUniqueId(), 0);
        if (CooldownManager.isEffectActive(player.getUniqueId(), "raw_strength_active")){
            CooldownManager.setDuration(player.getUniqueId(), "raw_strength_active", 0);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SHULKER_SHOOT, 1, 1);
        }
    }
}
