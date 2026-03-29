package pl.Doom.Utils;

import org.bukkit.Color;

public class ChatUtil {

    public static String color(String msg) {
        if (msg == null) return null;
        return msg.replaceAll("(?i)&([0-9A-FK-OR])", "§$1");
    }

    public static String chatColor(Color color) {
        return String.format("§x§%1$X§%2$X§%3$X§%4$X§%5$X§%6$X",
                (color.getRed() >> 4) & 0xF,
                color.getRed() & 0xF,
                (color.getGreen() >> 4) & 0xF,
                color.getGreen() & 0xF,
                (color.getBlue() >> 4) & 0xF,
                color.getBlue() & 0xF
        );
    }

    public static String Hex(String hexCode) {
        if (!hexCode.startsWith("#") || hexCode.length() != 7) {
            return "Invalid hex code! Use format: #RRGGBB";
        }

        StringBuilder minecraftColor = new StringBuilder("§x");
        for (char c : hexCode.substring(1).toCharArray()) {
            minecraftColor.append("§").append(c);
        }

        return minecraftColor.toString();
    }

    public static String fancyFont(String text) {
        text = text.toLowerCase();
        StringBuilder builder = new StringBuilder();
        for (char c : text.toCharArray()) {
            switch (c) {
                case 'a' -> builder.append('ᴀ');
                case 'b' -> builder.append('ʙ');
                case 'c' -> builder.append('ᴄ');
                case 'd' -> builder.append('ᴅ');
                case 'e' -> builder.append('ᴇ');
                case 'f' -> builder.append('ғ');
                case 'g' -> builder.append('ɢ');
                case 'h' -> builder.append('ʜ');
                case 'i' -> builder.append('ɪ');
                case 'j' -> builder.append('ᴊ');
                case 'k' -> builder.append('ᴋ');
                case 'l' -> builder.append('ʟ');
                case 'm' -> builder.append('ᴍ');
                case 'n' -> builder.append('ɴ');
                case 'o' -> builder.append('ᴏ');
                case 'p' -> builder.append('ᴘ');
                case 'q' -> builder.append('ǫ');
                case 'r' -> builder.append('ʀ');
                case 's' -> builder.append('ꜱ');
                case 't' -> builder.append('ᴛ');
                case 'u' -> builder.append('ᴜ');
                case 'v' -> builder.append('ᴠ');
                case 'w' -> builder.append('ᴡ');
                case 'x' -> builder.append('x');
                case 'y' -> builder.append('ʏ');
                case 'z' -> builder.append('ᴢ');
                case ' ' -> builder.append(' ');
                default -> builder.append(c);
            }
        }
        return builder.toString();
    }



}
