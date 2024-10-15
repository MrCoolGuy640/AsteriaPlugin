package com.mrcool.asteriaFishing;

import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class FishingListener implements Listener {

    private final AsteriaFishing plugin;
    private final FishConfig fishConfig;

    public FishingListener(AsteriaFishing plugin) {
        this.plugin = plugin;
        this.fishConfig = new FishConfig();
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        // plugin.getLogger().warning("Fishing Activated!!!");
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            // plugin.getLogger().warning("Fishing Activated!!! 2");
            FishConfig.Fish caughtFish = fishConfig.getRandomFish();
            if (caughtFish != null) {
                // plugin.getLogger().warning("NOT NULL!!!!");
                ItemStack fishItem = new ItemStack(Material.COD); // Use a specific fish item type here
                ItemMeta meta = fishItem.getItemMeta(); // Get the ItemMeta

                if (meta != null) { // Ensure that meta is not null
                    meta.setDisplayName(caughtFish.getColoredName());
                    List<String> loreList = caughtFish.getLore();
                    meta.setLore(loreList); // Set lore
                    fishItem.setItemMeta(meta); // Set the updated meta back to the ItemStack
                }

                // Cast the vanilla drop as an 'Item' so we can set it to air, and thus erase it from existence
                Item defaultDrop = (Item) event.getCaught();
                defaultDrop.setItemStack(new ItemStack(Material.AIR));

                // Give the player the custom drop instead!
                event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), fishItem);
            }
        }
    }
}
