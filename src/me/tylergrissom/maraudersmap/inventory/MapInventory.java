package me.tylergrissom.maraudersmap.inventory;

import me.tylergrissom.maraudersmap.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class MapInventory {

    private Main plugin;

    public MapInventory(Main plugin) {
        this.plugin = plugin;
    }

    private ItemStack getWarpItem(String warp) {
        ConfigurationSection section = this.plugin.getConfig().getConfigurationSection("warps." + warp + ".item");
        Material material = section.getString("material") == null ? Material.STAINED_GLASS_PANE : Material.valueOf(section.getString("material"));
        String displayName = section.getString("display-name");
        int amount = section.getInt("amount") == 0 ? 1 : section.getInt("amount");
        List<String> lore = section.getStringList("lore").stream().map(str -> ChatColor.translateAlternateColorCodes('&', str)).collect(Collectors.toList());

        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta meta = itemStack.getItemMeta();

        if (displayName != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        }

        if (section.getStringList("lore") != null) {
            String requiredPermission = plugin.getConfig().getString("warps." + warp + ".required-permission");
            int delay = plugin.getConfig().getInt("warps." + warp + ".delay", 0);
            String delayStr;

            if (requiredPermission != null && !requiredPermission.equals("")) {
                lore.add("");

                if (delay <= 0) {
                    delayStr = "Instant";
                } else {
                    delayStr = plugin.getTimeUtility().format(delay);
                }

                lore.add("§7Teleport time:");
                lore.add("  §e§l" + delayStr);
                lore.add("§cRequired permission:");
                lore.add("  §4§l" + requiredPermission);

                if (!plugin.getConfig().getBoolean("warps." + warp + ".enabled")) {
                    lore.add("§4§lDisabled");
                }
            }

            meta.setLore(lore);
        }

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public void open(Player player) {
        List<String> warps = this.plugin.getConfig().getStringList("players." + player.getUniqueId().toString());

        for (String str : warps) {
            if (this.plugin.getConfig().getConfigurationSection("warps." + str) == null) {
                warps.remove(str);
            }
        }

        player.playSound(player.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 1.0F, 1.0F);

        ItemStack[] inventoryContents = new ItemStack[warps.size()];
        int size = 9;

        if (inventoryContents.length > 9) size = 18;
        if (inventoryContents.length > 18) size = 27;
        if (inventoryContents.length > 27) size = 36;
        if (inventoryContents.length > 36) size = 45;
        if (inventoryContents.length > 45) size = 54;

        // TODO Multi page support

        for (int i = 0; i < warps.size(); i++) {
            inventoryContents[i] = ((getWarpItem(warps.get(i)) == null || !plugin.getConfig().getBoolean("warps." + warps.get(i) + ".enabled")) ? new ItemStack(Material.AIR) : getWarpItem(warps.get(i)));
        }

        Inventory inventory = Bukkit.createInventory(null, size, "Marauder's Map");

        inventory.setContents(inventoryContents);

        player.openInventory(inventory);
    }
}
