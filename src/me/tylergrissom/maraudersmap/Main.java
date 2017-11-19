package me.tylergrissom.maraudersmap;

import com.elmakers.mine.bukkit.magic.MagicPlugin;
import com.elmakers.mine.bukkit.slikey.effectlib.EffectManager;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import me.tylergrissom.maraudersmap.command.MaraudersMapCommand;
import me.tylergrissom.maraudersmap.listener.InventoryListener;
import me.tylergrissom.maraudersmap.listener.JoinListener;
import me.tylergrissom.maraudersmap.listener.MovementListener;
import me.tylergrissom.maraudersmap.listener.SneakListener;
import me.tylergrissom.maraudersmap.utility.LocationUtility;
import me.tylergrissom.maraudersmap.utility.TimeUtility;
import me.tylergrissom.maraudersmap.utility.WarpUtility;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private Main plugin;
    private WarpUtility warpUtility;
    private LocationUtility locationUtility;
    private TimeUtility timeUtility;
    private WorldEditPlugin worldEditPlugin;
    private MagicPlugin magicPlugin;
    private EffectManager effectManager;
    public static Main staticPlugin;

    public Main getPlugin() {
        return this.plugin;
    }

    public WorldEditPlugin getWorldEditPlugin() {
        return worldEditPlugin;
    }

    public WarpUtility getWarpUtility() {
        return this.warpUtility;
    }

    public LocationUtility getLocationUtility() {
        return this.locationUtility;
    }

    public TimeUtility getTimeUtility() {
        return timeUtility;
    }

    public EffectManager getEffectManager() {
        return effectManager;
    }

    public MagicPlugin getMagicPlugin() {
        return magicPlugin;
    }

    public void onEnable() {
        this.plugin = this;
        this.warpUtility = new WarpUtility(this);
        this.locationUtility = new LocationUtility(this);
        this.timeUtility = new TimeUtility(this);

        if (Bukkit.getPluginManager().getPlugin("WorldEdit") == null) {
            this.worldEditPlugin = null;

            Bukkit.getLogger().severe("Failed to hook in with WorldEdit. Disabling plugin...");

            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            this.worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

            Bukkit.getLogger().info("Hooked in with WorldEdit.");
        }

        if (Bukkit.getPluginManager().getPlugin("Magic") == null) {
            this.worldEditPlugin = null;

            Bukkit.getLogger().severe("Failed to hook in with Magic. Disabling plugin...");

            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            this.magicPlugin = (MagicPlugin) Bukkit.getPluginManager().getPlugin("Magic");

            Bukkit.getLogger().info("Hooked in with Magic.");
        }

        if (Bukkit.getPluginManager().getPlugin("EffectLib") == null) {
            this.effectManager = null;

            Bukkit.getLogger().severe("Failed to hook in with EffectLib. Disabling plugin...");

            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            this.effectManager = new EffectManager(this);

            Bukkit.getLogger().info("Hooked in with EffectLib.");
        }

        staticPlugin = this;

        getConfig().options().copyDefaults(true);
        saveConfig();

        getCommand("maraudersmap").setExecutor(new MaraudersMapCommand(this));

        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MovementListener(this), this);
        Bukkit.getPluginManager().registerEvents(new SneakListener(this), this);
    }

    public void onDisable() {
        effectManager.dispose();

        HandlerList.unregisterAll(this);
    }
}
