package me.tylergrissom.maraudersmap.utility;

import me.tylergrissom.maraudersmap.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class LocationUtility {

    private Main plugin;

    public LocationUtility(Main plugin) {
        this.plugin = plugin;
    }

    public Location getLocationFromConfigSection(ConfigurationSection section) {
        return new Location(Bukkit.getWorld(section.getString("world")), section.getDouble("x"), section.getDouble("y"), section.getDouble("z"));
    }

    public void setConfigSectionFromLocation(ConfigurationSection section, Location loc) {
        section.set("world", loc.getWorld().getName());
        section.set("x", loc.getX());
        section.set("y", loc.getY());
        section.set("z", loc.getZ());

        plugin.saveConfig();
    }
}