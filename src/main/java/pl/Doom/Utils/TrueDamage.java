package pl.Doom.Utils;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import pl.Main;

public class TrueDamage {

    public static void trueDamage(LivingEntity target, double damage, Player damager){
        int health = (int)target.getHealth();
        if (target instanceof Player player){
            if (player.isBlocking())return;
            if (player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE)return;
            if (Main.isTrusted(damager, player))return;
            if (target.isInvulnerable())return;

            Material mainHand = player.getInventory().getItemInMainHand().getType();
            Material offhand = player.getInventory().getItemInOffHand().getType();
            if (mainHand == Material.TOTEM_OF_UNDYING || offhand == Material.TOTEM_OF_UNDYING){
                if (health <= damage){
                    player.damage(200);
                } else {
                    normalTrueDamage(player, damage);
                }
            }

            else {
                normalTrueDamage(player, damage);
            }
        }


        else {
            normalTrueDamage(target, damage);
        }
    }

    private static void normalTrueDamage(LivingEntity target, double damage){
        double absHearts = 0;
        if (target.hasPotionEffect(PotionEffectType.ABSORPTION)){
            absHearts = target.getAbsorptionAmount();
            target.setAbsorptionAmount(Math.max(absHearts - damage, 0));
            damage = Math.max(damage - absHearts, 0);
        }
        target.setHealth(Math.max(target.getHealth() - damage, 0));
        target.playHurtAnimation(0f);
    }

}
