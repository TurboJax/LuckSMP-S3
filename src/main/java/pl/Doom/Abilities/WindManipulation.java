package pl.Doom.Abilities;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WindCharge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import pl.Doom.AbilityMapping;
import pl.Doom.Utils.TrueDamage;
import pl.Main;
import pl.managers.CooldownManager;

public class WindManipulation implements Listener {
    @EventHandler
    public void Mace(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;

        String name = "wind_manipulation1";
        if (CooldownManager.isOnCooldown(player.getUniqueId(), "1")) return;
        if (Main.getAbilityManager().getAbility(player.getUniqueId()) != AbilityMapping.WIND_MANIPULATION) return;

        int chance = Main.getInstance().getConfig().getInt("chances." + name, 50);
        int cooldown = Main.getInstance().getConfig().getInt("cooldowns." + name, 35);
        int distance = Main.getInstance().getConfig().getInt("WindManipulationMaceMinFallDistance", 5);

        if (player.getFallDistance() < distance) return;
        if (!player.getInventory().getItemInMainHand().getType().toString().contains("_AXE"))return;

        if (Math.random() * 100 < chance) {
            if (Main.isTrusted(player, target))return;
            mace(event, player.getFallDistance());
            CooldownManager.setCooldown(player.getUniqueId(), "1", cooldown);
        }
    }

    public void mace(EntityDamageByEntityEvent event, double fall) {
        Entity target = event.getEntity();
        target.getWorld().playSound(target.getLocation(), Sound.ITEM_MACE_SMASH_GROUND_HEAVY, 1, 1);

        Block block = event.getEntity().getLocation().clone().subtract(0,1, 0).getBlock();
        if (block.getType() != Material.AIR) {
            int radius = 2;
            for (int i = 0; i < 360; i += 8) {
                Location loc = event.getEntity().getLocation();
                World world = event.getEntity().getWorld();

                double angle = Math.toRadians(i);
                double x = loc.getX() + (radius * Math.cos(angle));
                double y = loc.getY();
                double z = loc.getZ() + (radius * Math.sin(angle));
                Location particleLoc = new Location(world, x, y, z);
                world.spawnParticle(Particle.DUST_PILLAR, particleLoc, 1, 0, 0, 0, 0, block.getType().createBlockData());
            }
        }


        double damage = Main.getInstance().getConfig().getDouble("damage.wind_manipulation1", 2.5);
        double finalDamage = fall * damage;
        event.setDamage(finalDamage);
        event.getDamager().setVelocity(new Vector(0, 0, 0));
        event.getDamager().setFallDistance(0);
    }

    @EventHandler
    public void WindCharge(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof WindCharge charge)) return;
        if (!(charge.getShooter() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;

        String name = "wind_manipulation2";
        if (CooldownManager.isOnCooldown(player.getUniqueId(), "2")) return;
        if (Main.getAbilityManager().getAbility(player.getUniqueId()) != AbilityMapping.WIND_MANIPULATION) return;

        int chance = Main.getInstance().getConfig().getInt("chances." + name, 45);
        int cooldown = Main.getInstance().getConfig().getInt("cooldowns." + name, 35);
        int duration = Main.getInstance().getConfig().getInt("durations." + name, 2);
        int damage = Main.getInstance().getConfig().getInt("damage." + name, 2);

        if (Math.random() * 100 < chance) {
            if (Main.isTrusted(player, target))return;
            TrueDamage.trueDamage(target, damage, player);
            pullTargetToPlayer(target, player, duration);
            CooldownManager.setCooldown(player.getUniqueId(), "2", cooldown);
        }
    }

    public static void pullTargetToPlayer(LivingEntity target, Player player, int duration) {
        new BukkitRunnable() {
            int ticks = 0;
            final int maxTicks = 20;

            @Override
            public void run() {
                if (ticks >= maxTicks || !target.isValid() || !player.isOnline()) {
                    freeze(target, duration);
                    cancel();
                    return;
                }

                if (player.getLocation().distance(target.getLocation()) <= 2) {
                    freeze(target, duration);
                    target.setVelocity(new Vector(0, 0, 0));
                    cancel();
                    return;
                }

                Location from = target.getLocation().clone().add(0, 1.0, 0);
                Location to = player.getLocation().clone().add(0, 1.0, 0);

                Vector vec = to.toVector().subtract(from.toVector());
                double length = vec.length();
                Vector step = vec.normalize().multiply(1);

                for (double i = 0; i < length; i += 1) {
                    Location point = from.clone().add(step.clone().multiply(i));
                    from.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, point, 0, 0, 0, 0, 0);
                }

                Vector direction = player.getLocation().toVector()
                        .subtract(target.getLocation().toVector())
                        .normalize();
                target.setVelocity(direction.multiply(1.5));
                ticks++;
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }

    private static void freeze(Entity target, int duration) {
        CooldownManager.setCooldown(target.getUniqueId(), "frozen_ability_1", duration);
        Location loc = target.getLocation();
        final Location freezeLocation = loc.clone();

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= duration * 20 || target.isDead() || target.getLocation().distance(freezeLocation) > 2) {
                    cancel();
                    return;
                }
                target.teleport(freezeLocation);
                ticks += 1;
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }

    @EventHandler
    public void onStunnedAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (CooldownManager.isOnCooldown(player.getUniqueId(), "frozen_ability_1")) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void consume(PlayerItemConsumeEvent event) {
        if (CooldownManager.isOnCooldown(event.getPlayer().getUniqueId(), "frozen_ability_1")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void RightClick(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        if (CooldownManager.isOnCooldown(event.getPlayer().getUniqueId(), "frozen_ability_1")) {
            event.setCancelled(true);
        }
    }

}
