package pl.managers;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownManager {
    // Maps to manage cooldowns and durations
    private static final Map<UUID, Map<String, Long>> cooldowns = new ConcurrentHashMap<>();
    private static final Map<UUID, Map<String, Long>> durations = new ConcurrentHashMap<>();
    public static final Map<String, String> displayNames = new ConcurrentHashMap<>();
    private static final Map<UUID, Map<String, Long>> cooldownDurations = new ConcurrentHashMap<>();

    public CooldownManager() {
        // Private constructor to prevent instantiation
    }

    // ------------------------------------
    // Duration Management
    // ------------------------------------

    public static void setDuration(UUID playerUUID, String key, long seconds) {
        durations.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>())
                .put(key, System.currentTimeMillis() + seconds * 1000L);
    }

    public static boolean isEffectActive(UUID playerUUID, String key) {
        return getEffectTimeLeft(playerUUID, key) > 0L;
    }

    public static long getEffectTimeLeft(UUID playerUUID, String key) {
        Map<String, Long> playerDurations = durations.get(playerUUID);
        if (playerDurations == null || !playerDurations.containsKey(key)) return 0L;

        long timeLeft = playerDurations.get(key) - System.currentTimeMillis();
        return timeLeft > 0L ? timeLeft : 0L;
    }

    public static void clearSpecificDuration(UUID playerUUID, String key) {
        Map<String, Long> playerDurations = durations.get(playerUUID);
        if (playerDurations != null) {
            playerDurations.remove(key);
        }
    }

    public static void cleanupExpiredDurations() {
        long currentTime = System.currentTimeMillis();

        for (UUID playerUUID : durations.keySet()) {
            Map<String, Long> playerDurations = durations.get(playerUUID);
            if (playerDurations != null) {
                playerDurations.entrySet().removeIf(entry -> entry.getValue() <= currentTime);
            }
        }
    }

    // ------------------------------------
    // Cooldown Management
    // ------------------------------------

    public static boolean isOnCooldown(UUID playerUUID, String key) {
        return getCooldownTimeLeft(playerUUID, key) > 0L;
    }

    public static void setCooldown(UUID playerUUID, String key, long seconds) {
        cooldowns.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>())
                .put(key, System.currentTimeMillis() + seconds * 1000L);

        cooldownDurations.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>())
                .put(key, seconds);
    }
    public static long getCachedDuration(UUID playerUUID, String key) {
        return cooldownDurations.getOrDefault(playerUUID, Collections.emptyMap()).getOrDefault(key, 0L);
    }


    public static long getCooldownTimeLeft(UUID playerUUID, String key) {
        Map<String, Long> playerCooldowns = cooldowns.get(playerUUID);
        if (playerCooldowns == null || !playerCooldowns.containsKey(key)) return 0L;

        long timeLeft = playerCooldowns.get(key) - System.currentTimeMillis();
        return timeLeft > 0L ? timeLeft : 0L;
    }

    // ------------------------------------
    // New Method: Remove Cooldown
    // ------------------------------------
    public static boolean hasAnyEffect(UUID playerUUID) {
        Map<String, Long> playerDurations = durations.get(playerUUID);
        if (playerDurations == null) return false;

        long currentTime = System.currentTimeMillis();
        for (long expiry : playerDurations.values()) {
            if (expiry > currentTime) return true;
        }
        return false;
    }

    public static boolean hasAnyCooldown(UUID playerUUID) {
        Map<String, Long> playerCooldowns = cooldowns.get(playerUUID);
        if (playerCooldowns == null) return false;

        long currentTime = System.currentTimeMillis();
        for (long expiry : playerCooldowns.values()) {
            if (expiry > currentTime) return true;
        }
        return false;
    }

    public static void removeAllCooldowns(UUID playerUUID) {
        cooldowns.remove(playerUUID);
    }
    public static void removeAllDurations(UUID playerUUID) {
        cooldownDurations.remove(playerUUID);
    }

    public static void resetAllCooldowns(UUID playerUUID) {
        Map<String, Long> activeCooldowns = cooldowns.get(playerUUID);
        Map<String, Long> originalDurations = cooldownDurations.get(playerUUID);

        if (activeCooldowns == null || originalDurations == null) return;

        for (String key : activeCooldowns.keySet()) {
            Long duration = originalDurations.get(key);
            if (duration != null && duration > 0) {
                setCooldown(playerUUID, key, duration);
            }
        }
    }
}
