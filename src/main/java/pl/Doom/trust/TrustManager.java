package pl.Doom.trust;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.Main;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TrustManager {

    private final File file;
    private final FileConfiguration config;
    private final Map<UUID, Set<UUID>> trusts = new HashMap<>();

    public TrustManager() {
        file = new File(Main.getInstance().getDataFolder(), "trusts.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);

        for (String key : config.getKeys(false)) {
            UUID owner = UUID.fromString(key);
            List<String> list = config.getStringList(key);
            Set<UUID> set = new HashSet<>();
            for (String s : list) {
                set.add(UUID.fromString(s));
            }
            trusts.put(owner, set);
        }
    }

    public void trustPlayer(Player owner, Player trusted) {
        trusts.computeIfAbsent(owner.getUniqueId(), k -> new HashSet<>()).add(trusted.getUniqueId());
        save(owner.getUniqueId());
    }

    public void untrustPlayer(Player owner, Player trusted) {
        Set<UUID> set = trusts.get(owner.getUniqueId());
        if (set == null) return;
        set.remove(trusted.getUniqueId());
        save(owner.getUniqueId());
    }

    public Set<UUID> getTrustedPlayers(Player owner) {
        return trusts.getOrDefault(owner.getUniqueId(), new HashSet<>());
    }

    public boolean isTrusted(Player owner, Player trusted) {
        return getTrustedPlayers(owner).contains(trusted.getUniqueId());
    }

    private void save(UUID owner) {
        Set<UUID> set = trusts.getOrDefault(owner, new HashSet<>());
        List<String> list = new ArrayList<>();
        for (UUID uuid : set) {
            list.add(uuid.toString());
        }
        config.set(owner.toString(), list);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}