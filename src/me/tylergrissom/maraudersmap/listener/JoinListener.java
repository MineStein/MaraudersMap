package me.tylergrissom.maraudersmap.listener;

import me.tylergrissom.maraudersmap.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private Main plugin;

    public JoinListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String starterWarp = this.plugin.getConfig().getString("starterWarp");

        if ((starterWarp != null) && (this.plugin.getConfig().get("players." + player.getUniqueId().toString()) == null)) {
            this.plugin.getWarpUtility().discoverWarp(player, starterWarp);
        }
    }
}
