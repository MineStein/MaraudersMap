package me.tylergrissom.maraudersmap.utility;

import me.tylergrissom.maraudersmap.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WarpUtility {

    public enum SelectionPoint {
        POINT_ONE,

        POINT_TWO;
    }

    private Main plugin;

    public WarpUtility(Main plugin) {
        this.plugin = plugin;
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
