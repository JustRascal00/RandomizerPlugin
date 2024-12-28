package org.rascal00.testPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class TestPlugin extends JavaPlugin implements Listener {

    private final Random random = new Random();
    private final Map<Material, Material> dropMappings = new HashMap<>(); // Maps block types to random drops

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("TestPlugin has been enabled!");

        // Register events
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("TestPlugin has been disabled!");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // Cancel default drops
        event.setDropItems(false);

        // Get the block's material
        Material blockType = event.getBlock().getType();

        // Determine the random drop for this block type
        Material randomDrop = dropMappings.computeIfAbsent(blockType, k -> getRandomMaterial());

        // Create a random item stack
        int quantity = random.nextInt(567) + 1; // Random quantity (1 to 567)
        ItemStack randomItem = new ItemStack(randomDrop, quantity);

        // Drop the item naturally at the block's location
        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), randomItem);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // Clear default drops
        event.getDrops().clear();

        // Get the entity's type (optional customization)
        Material randomDrop = getRandomMaterial(); // For simplicity, a random material for entities

        // Create a random item stack
        int quantity = random.nextInt(567) + 1;
        ItemStack randomItem = new ItemStack(randomDrop, quantity);

        // Drop the item naturally at the entity's location
        event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), randomItem);
    }

    private Material getRandomMaterial() {
        Material[] materials = Material.values();
        Material material;
        do {
            material = materials[random.nextInt(materials.length)];
        } while (!material.isItem()); // Ensure it is an item
        return material;
    }
}
