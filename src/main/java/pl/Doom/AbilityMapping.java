package pl.Doom;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import pl.Books;

public enum AbilityMapping {
    WIND_MANIPULATION("§fWind Manipulation", "\uE912") {
        @Override
        public boolean matchesItem(ItemStack item) {
            return item.isSimilar(createItem());
        }

        @Override
        public ItemStack createItem() {
            return Books.createWindManipulationBook();
        }
    },
    DARK_SENSE("§7Dark Sense", "\uE913") {
        @Override
        public boolean matchesItem(ItemStack item) {
            return item.isSimilar(createItem());
        }

        @Override
        public ItemStack createItem() {
            return Books.createDarkSense();
        }
    },
    ADRENALINE_RUSH("§dAdrenaline Rush", "\uE914") {
        @Override
        public boolean matchesItem(ItemStack item) {
            return item.isSimilar(createItem());
        }

        @Override
        public ItemStack createItem() {
            return Books.createAdrenalineRush();
        }
    },
    HELLSCORCH("§6Hellscorch", "\uE916") {
        @Override
        public boolean matchesItem(ItemStack item) {
            return item.isSimilar(createItem());
        }

        @Override
        public ItemStack createItem() {
            return Books.createHellScorch();
        }
    },
    RAW_STRENGTH("§cRaw Strength", "\uE919") {
        @Override
        public boolean matchesItem(ItemStack item) {
            return item.isSimilar(createItem());
        }

        @Override
        public ItemStack createItem() {
            return Books.createRawStrength();
        }
    },
    THUNDERCHARGE("§bThundercharge", "\uE920") {
        @Override
        public boolean matchesItem(ItemStack item) {
            return item.isSimilar(createItem());
        }

        @Override
        public ItemStack createItem() {
            return Books.createThunderCharge();
        }
    };

    private final String hackName;
    private final String cooldownEmoji;

    AbilityMapping(String hackName, String cooldownEmoji) {
        this.hackName = hackName;
        this.cooldownEmoji = cooldownEmoji;
    }

    public String getHackName() {
        return hackName;
    }

    public String getColor() {
        return ChatColor.getLastColors(hackName);
    }

    public String getCooldownEmoji() {
        return cooldownEmoji;
    }

    public abstract boolean matchesItem(ItemStack item);

    public abstract ItemStack createItem();

    public static AbilityMapping fromItem(ItemStack item) {
        for (AbilityMapping mapping : values()) {
            if (mapping.matchesItem(item)) return mapping;
        }
        return null;
    }
}
