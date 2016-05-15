package me.tylergrissom.maraudersmap.listener;

import me.tylergrissom.maraudersmap.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Copyright (c) 2013-2016 Tyler Grissom
 */
public class MovementListener implements Listener {

    private Main plugin;

    public MovementListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();

        for (String key : plugin.getConfig().getConfigurationSection("warps").getKeys(false)) {
            Location l1 = plugin.getLocationUtility().getLocationFromConfigSection(plugin.getConfig().getConfigurationSection("warps." + key + ".discover-region.point-one"));
            Location l2 = plugin.getLocationUtility().getLocationFromConfigSection(plugin.getConfig().getConfigurationSection("warps." + key + ".discover-region.point-two"));

            if (contains(l1, l2, player.getLocation())) {
                if (!plugin.getWarpUtility().playerHasWarp(player, key) && plugin.getConfig().getBoolean("warps." + key + ".discoverable")) {
                    plugin.getWarpUtility().discoverWarp(player, key);
                }
            }
        }
    }

    public boolean contains(Location l1, Location l2, Location loc) {
        return loc.getBlockX() >= l1.getBlockX() && loc.getBlockX() <= l2.getBlockX()
                && loc.getBlockY() >= l1.getBlockY() && loc.getBlockY() <= l2.getBlockY()
                && loc.getBlockZ() >= l1.getBlockZ() && loc.getBlockZ() <= l2.getBlockZ();
    }
}
