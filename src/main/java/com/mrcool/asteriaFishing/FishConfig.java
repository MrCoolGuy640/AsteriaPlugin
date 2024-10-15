package com.mrcool.asteriaFishing;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FishConfig {

    public static class Fish {
        String secret1 = "#FFB6C1";
        String secret2 = "#DDA0DD";
        String godly1 = "#FFFFFF";
        String godly2 = "#FFD700";

        String name;
        String rarity;
        String lore;
        double chance;

        public Fish(String name, String rarity, String lore, double chance) {
            this.name = name;
            this.rarity = rarity;
            this.lore = lore;
            this.chance = chance;
        }

        public String getColoredName() {
            if (getRarityColor() != null) {
                return getRarityColor() + name;
            } else {
                if (rarity.equalsIgnoreCase("godly")) {
                    return gradient(name, godly1, godly2);
                } else {
                    return gradient(name, secret1, secret2);
                }
            }
        }

        private ChatColor getRarityColor() {
            switch (rarity.toLowerCase()) {
                case "common":
                    return ChatColor.WHITE;
                case "uncommon":
                    return ChatColor.GREEN;
                case "rare":
                    return ChatColor.AQUA;
                case "epic":
                    return ChatColor.LIGHT_PURPLE;
                case "legendary":
                    return ChatColor.GOLD;
                case "godly":
                case "secret":
                    return null; // Gradient handling
                default:
                    return ChatColor.WHITE; // Default to White
            }
        }

        public List<String> getLore() {
            String rarityPart = rarity.toUpperCase();
            List<String> loreList = new ArrayList<>();

            // Add the main description to the lore list
            loreList.add(ChatColor.GRAY + lore);

            // Add a blank line to separate description and rarity
            loreList.add("");

            // Add the rarity part with color!
            if (getRarityColor() == null) {
                loreList.add(getGradientRarity(rarityPart)); // Add the gradient rarity (only happens if getRarityColor() is null
            } else {
                loreList.add(getRarityColor() + rarityPart); // Add the colored rarity
            }
            // System.out.println(loreList);
            return loreList;
        }

        private String getGradientRarity(String rarity) {
            if (rarity.equalsIgnoreCase("godly")) {
                return gradient("godly", godly1, godly2); // White to Gold
            } else if (rarity.equalsIgnoreCase("secret")) {
                return gradient("secret", secret1, secret2); // Blue to Purple
            } else {
                AsteriaFishing.getPlugin().getLogger().warning("getGradientRarity for " + rarity + " failed!");
                return rarity; // Default to the rarity name if something goes wrong (this is a problem)
            }
        }

        public static String gradient(String message, String startHex, String endHex) {
            StringBuilder gradientString = new StringBuilder();
            int length = message.length();

            for (int i = 0; i < length; i++) {
                // Calculate the color for each character of the message String
                float ratio = (float) i / (length - 1);
                String color = getGradientColor(startHex, endHex, ratio);
                gradientString.append(color).append(message.charAt(i));
            }

            return gradientString.toString();
        }

        private static String getGradientColor(String startHex, String endHex, float ratio) {
            // Convert hex to RGB
            int startColor = Integer.parseInt(startHex.replace("#", ""), 16);
            int endColor = Integer.parseInt(endHex.replace("#", ""), 16);

            // Extract RGB components
            int r1 = (startColor >> 16) & 0xFF;
            int g1 = (startColor >> 8) & 0xFF;
            int b1 = startColor & 0xFF;

            int r2 = (endColor >> 16) & 0xFF;
            int g2 = (endColor >> 8) & 0xFF;
            int b2 = endColor & 0xFF;

            // Interpolate the RGB values
            int r = (int) (r1 + (r2 - r1) * ratio);
            int g = (int) (g1 + (g2 - g1) * ratio);
            int b = (int) (b1 + (b2 - b1) * ratio);

            // Convert back to hex string (this allows Minecraft to use the colours!)
            return net.md_5.bungee.api.ChatColor.of(String.format("#%02x%02x%02x", r, g, b)).toString();
        }
    }

    private final Map<String, Fish> fishMap = new HashMap<>();

    public FishConfig() {
        // Define the fishy :D
        fishMap.put("Crystal Fish", new Fish("Crystal Fish", "common", "A beautiful fish shimmering like crystal.", 10.0));
        fishMap.put("Emerald Fish", new Fish("Emerald Fish", "uncommon", "A rare fish with a green shine.", 10.0));
        fishMap.put("Sapphire Fish", new Fish("Sapphire Fish", "rare", "This fish glows like a sapphire.", 10.0));
        fishMap.put("Amethyst Fish", new Fish("Amethyst Fish", "epic", "A mystical fish with a purple hue.", 10.0));
        fishMap.put("Golden Fish", new Fish("Golden Fish", "legendary", "A fish that glimmers like gold.", 10.0));
        fishMap.put("Seraph Fish", new Fish("Seraph Fish", "godly", "A divine fish that radiates light.", 10.0));
        fishMap.put("Mystic Fish", new Fish("Mystic Fish", "secret", "A fish shrouded in mystery.", 10.0));
    }

    // uses the total combined weight to calculate a random fish
    public Fish getRandomFish() {
        double totalChance = fishMap.values().stream().mapToDouble(fish -> fish.chance).sum();
        double randomValue = Math.random() * totalChance;
        double cumulativeChance = 0;

        for (Fish fish : fishMap.values()) {
            cumulativeChance += fish.chance;
            if (randomValue <= cumulativeChance) {
                return fish;
            }
        }
        return null; // In case something goes wrong
    }
}
