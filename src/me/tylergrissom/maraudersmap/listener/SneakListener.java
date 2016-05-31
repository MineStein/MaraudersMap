package me.tylergrissom.maraudersmap.listener;

import me.tylergrissom.maraudersmap.Main;
import me.tylergrissom.maraudersmap.utility.WarpUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffectType;

/**
 * Copyright (c) 2013-2016 Tyler Grissom
 */
public class SneakListener implements Listener {

    private Main plugin;

    public SneakListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onShift(final PlayerToggleSneakEvent event) {
        final Player player = event.getPlayer();
        final WarpUtility warpUtility = plugin.getWarpUtility();

        if (warpUtility.getWarpQueue().containsKey(player.getUniqueId().toString())) {
            if (warpUtility.getWarpConfigurationSection(warpUtility.getWarpQueue().get(player.getUniqueId().toString())).getBoolean("cancellable", true)) {
                warpUtility.getWarpQueue().remove(player.getUniqueId().toString());

                player.removePotionEffect(PotionEffectType.SLOW);
                player.removePotionEffect(PotionEffectType.JUMP);

                player.sendMessage("§7Cancelled warp process.");
            } else {
                player.sendMessage("§4§lX §cYou cannot cancel warping to §e" + warpUtility.getWarpQueue().get(player.getUniqueId().toString()));
            }
        }
    }
}
