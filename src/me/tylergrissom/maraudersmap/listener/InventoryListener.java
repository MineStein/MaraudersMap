package me.tylergrissom.maraudersmap.listener;

import com.elmakers.mine.bukkit.slikey.effectlib.effect.WarpEffect;
import com.elmakers.mine.bukkit.slikey.effectlib.util.ParticleEffect;
import me.tylergrissom.maraudersmap.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InventoryListener implements Listener {

    private Main plugin;

    public InventoryListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();
        String displayName;

        if (event.getInventory().getName().equals("Marauder's Map")) {
            event.setCancelled(true);

            if ((itemStack != null) && (itemStack.getItemMeta().getDisplayName() != null)) {
                displayName = itemStack.getItemMeta().getDisplayName();

                for (String str : plugin.getConfig().getConfigurationSection("warps").getKeys(false)) {
                    ConfigurationSection section = plugin.getConfig().getConfigurationSection("warps." + str);

                    if (section.getString("item.display-name") != null && displayName != null && ChatColor.translateAlternateColorCodes('&', section.getString("item.display-name")).equals(displayName)) {
                        Location location = plugin.getLocationUtility().getLocationFromConfigSection(section.getConfigurationSection("warp-location"));

                        if (location == null) {
                            player.sendMessage("§4§X §cThis location is invalid in the config.yml.");

                            return;
                        } else {
                            String requiredPermission = section.getString("required-permission");

                            if (section.getString("required-permission") == null || requiredPermission.equals("") || player.hasPermission(requiredPermission)) {
                                if (!plugin.getWarpUtility().getWarpQueue().containsKey(player.getUniqueId().toString())) {
                                    player.closeInventory();
                                    player.sendMessage("§7Warping in §e5 seconds§7...");

                                    if (section.getBoolean("cancellable", true)) player.sendMessage("§7Press §e§l§n§oshift§r §7cancel.");

                                    WarpEffect warpEffect = new WarpEffect(plugin.getEffectManager());

                                    warpEffect.duration = 2000;
                                    warpEffect.period = 3;
                                    warpEffect.particle = ParticleEffect.FIREWORKS_SPARK;
                                    warpEffect.particles = 20;
                                    warpEffect.iterations = 200;
                                    warpEffect.radius = 1;
                                    warpEffect.rings = 100;

                                    warpEffect.setEntity(player);

                                    warpEffect.start();

                                    plugin.getWarpUtility().getWarpQueue().put(player.getUniqueId().toString(), str);

                                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 20, false, false));
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, -20, false, false));

                                    Bukkit.getScheduler().runTaskLater(plugin, new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            if (plugin.getWarpUtility().getWarpQueue().containsKey(player.getUniqueId().toString())) {
                                                player.sendMessage("§7Fast travelling to §e" + ChatColor.translateAlternateColorCodes('&', section.getString("item.display-name")));

                                                player.teleport(location);

                                                plugin.getWarpUtility().getWarpQueue().remove(player.getUniqueId().toString());

                                                String warpMessage = section.getString("warp-message");
                                                List<String> serverCommands = section.getStringList("warp-commands.console");
                                                List<String> playerCommands = section.getStringList("warp-commands.player");

                                                if (serverCommands != null && serverCommands.size() > 0) {
                                                    List<String> newServerCommands = new ArrayList<>();

                                                    for (int i = 0; i < serverCommands.size(); i++) {
                                                        if (!serverCommands.get(i).equals("")) newServerCommands.add(serverCommands.get(i).replaceAll("%player%", player.getName()));
                                                    }

                                                    for (int i = 0; i < newServerCommands.size(); i++) {
                                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), newServerCommands.get(i));
                                                    }
                                                }

                                                if (playerCommands != null && playerCommands.size() > 0) {
                                                    for (int i = 0; i < playerCommands.size(); i++) {
                                                        player.performCommand(playerCommands.get(i));
                                                    }
                                                }

                                                if (warpMessage != null && !Objects.equals(warpMessage, "")) {
                                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', warpMessage));
                                                }
                                            }
                                        }
                                    }, 100);
                                } else {
                                    player.sendMessage("§4§lX §cYou're already warping somewhere. Please wait...");
                                }
                            } else {
                                player.closeInventory();
                                player.sendMessage("§4§lX §cYou don't have permission.");
                            }
                        }
                    }
                }
            }
        }
    }
}
