package me.tylergrissom.maraudersmap.utility;

import me.tylergrissom.maraudersmap.Main;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarpUtility {

    private Main plugin;
    private Map<String, String> warpQueue;

    public Main getPlugin() {
        return plugin;
    }

    public Map<String, String> getWarpQueue() {
        return warpQueue;
    }

    public WarpUtility(Main plugin) {
        this.plugin = plugin;
        this.warpQueue = new HashMap<>();
    }

    public boolean isWarpValid(String warp) {
        return true;
    }

    public boolean playerHasWarp(Player player, String warp) {
        return plugin.getConfig().getStringList("players." + player.getUniqueId().toString()).contains(warp);
    }

    public List<String> getWarps(Player player) {
        return plugin.getConfig().getStringList("players." + player.getUniqueId().toString());
    }

    public boolean warpExists(String warpname) {
        return this.plugin.getConfig().get("warps." + warpname) != null;
    }

    public ConfigurationSection getWarpConfigurationSection(String warp) {
        if (warpExists(warp)) return plugin.getConfig().getConfigurationSection("warps." + warp);

        return null;
    }

    public void discoverWarp(Player player, String warp) {
        if ((isWarpValid(warp)) && (!playerHasWarp(player, warp))) {
            player.sendMessage("§7You have discovered a new area for your Marauder's Map!");

            List<String> list = new ArrayList<>(this.plugin.getConfig().getStringList("players." + player.getUniqueId()));

            list.add(warp);

            this.plugin.getConfig().set("players." + player.getUniqueId().toString(), list);
            this.plugin.saveConfig();
        }
    }
}
