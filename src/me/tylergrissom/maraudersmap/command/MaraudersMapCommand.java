package me.tylergrissom.maraudersmap.command;

import com.sk89q.worldedit.bukkit.selections.Selection;
import me.tylergrissom.maraudersmap.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class MaraudersMapCommand extends CommandBase {

    private Main plugin;

    public MaraudersMapCommand(Main plugin) {
        this.plugin = plugin;
    }

    void execute(CommandSender sender, Command command, String[] args) {
        String[] help = {"§a§l*** §7/maraudersmap §a§l***", "§7/maraudersmap reload - reload configuration file", "§7/maraudersmap discover <player> <warpname> - force discover the warp", "§7/maraudersmap set <warpname> <attribute> [param] - set a warp's attributes", "§7/maraudersmap create <name> - create a new warp"};
        String[] attributes = {
                "item - hold an item in your hand",
                "warp-commands - specify either 'player' or 'console' followed by the command",
                "enabled - boolean",
                "discoverable - boolean",
                "warp-location - stand in the location you want",
                "discover-region - have a region select with //wand",
                "warp-message - string",
                "required-permission - string or none"
        };

        if (args.length == 0) {
            sender.sendMessage(help);
        } else {
            String sub = args[0];

            if (sub.equalsIgnoreCase("reload")) {
                if (sender.hasPermission("maraudersmap.maraudersmap.reload")) {
                    sender.sendMessage("§7Reloading configuration...");

                    plugin.reloadConfig();

                    sender.sendMessage("§7Successfully reloaded configuration.");
                } else {
                    sender.sendMessage("§4§lX §cYou don't have permission.");
                }
            } else if (sub.equalsIgnoreCase("discover")) {
                if (sender.hasPermission("maraudersmap.maraudersmap.discover")) {
                    if ((args.length == 1) || (args.length == 2)) {
                        sender.sendMessage(help);
                    } else {
                        Player target = Bukkit.getPlayer(args[1]);

                        if (target != null) {
                            String warpName = args[2];

                            if (plugin.getWarpUtility().warpExists(warpName)) {
                                if (!plugin.getWarpUtility().getWarps(target).contains(warpName)) {
                                    plugin.getWarpUtility().discoverWarp(target, warpName);

                                    sender.sendMessage("§7You have forced §e" + target.getName() + " §7to discover the warp §e" + warpName);
                                } else {
                                    sender.sendMessage("§4§lX §cThat player has already discovered that warp.");
                                }
                            } else {
                                sender.sendMessage("§4§lX §cThat warp doesn't exist.");
                            }
                        } else {
                            sender.sendMessage("§4§lX §cThat player isn't online.");
                        }
                    }
                } else {
                    sender.sendMessage("§4§lX §cYou don't have permission.");
                }
            } else if (sub.equalsIgnoreCase("set")) {
                if (sender.hasPermission("maraudersmap.maraudersmap.set")) {
                    if (args.length == 1 || args.length == 2) {
                        sender.sendMessage(attributes);
                    } else {
                        String warpName = args[1];

                        if (plugin.getConfig().getConfigurationSection("warps").getKeys(false).contains(warpName)) {
                            String attribute = args[2];

                            if (attribute.equalsIgnoreCase("item")) {
                                if (sender instanceof Player) {
                                    Player player = (Player) sender;
                                    ItemStack itemStack = player.getInventory().getItemInMainHand();

                                    plugin.getConfig().set("warps." + warpName + ".item.display-name", itemStack.getItemMeta().getDisplayName());
                                    plugin.getConfig().set("warps." + warpName + ".item.material", itemStack.getType().toString());
                                    plugin.getConfig().set("warps." + warpName + ".item.amount", itemStack.getAmount());
                                    plugin.getConfig().set("warps." + warpName + ".item.lore", itemStack.getItemMeta().getLore());

                                    player.sendMessage("§e" + warpName +"'s §7item has been set to the item in your hand.");
                                } else {
                                    sender.sendMessage("§4§lX §cYou have to be a player to perform this command.");
                                }
                            } else if (attribute.equalsIgnoreCase("warp-commands")) {

                            } else if (attribute.equalsIgnoreCase("enabled")) {
                                if (args.length == 3) {
                                    sender.sendMessage(attributes);
                                } else {
                                    boolean bool = Boolean.parseBoolean(args[3]);

                                    plugin.getConfig().set("warps." + warpName + ".enabled", bool);

                                    sender.sendMessage("§e" + warpName + " §7has been §e" + (bool ? "enabled" : "disabled"));
                                }
                            } else if (attribute.equalsIgnoreCase("discoverable")) {
                                if (args.length == 3) {
                                    sender.sendMessage(attributes);
                                } else {
                                    boolean bool = Boolean.parseBoolean(args[3]);

                                    plugin.getConfig().set("warps." + warpName + ".discoverable", bool);

                                    sender.sendMessage("§e" + warpName + "'s §7discoverability has been §e" + (bool ? "enabled" : "disabled"));
                                }
                            } else if (attribute.equalsIgnoreCase("warp-location")) {
                                if (sender instanceof Player) {
                                    Player player = (Player) sender;

                                    plugin.getLocationUtility().setConfigSectionFromLocation(plugin.getConfig().getConfigurationSection("warps." + warpName + ".warp-location"), player.getLocation());

                                    player.sendMessage("§e" + warpName + "'s §7warp location has been set to your current location.");
                                } else {
                                    sender.sendMessage("§4§lX §cYou have to be a player to perform this command.");
                                }
                            } else if (attribute.equalsIgnoreCase("discover-region")) {
                                if (sender instanceof Player) {
                                    Player player = (Player) sender;
                                    Selection selection = plugin.getWorldEditPlugin().getSelection(player);

                                    if (selection == null
                                            || selection.getMinimumPoint() == null
                                            || selection.getMaximumPoint() == null) {
                                        player.sendMessage("§4§lX §cPlease make a valid WorldEdit selection. Do §4§l//wand §cto receive the selection tool.");
                                    } else {
                                        if (selection.getMinimumPoint().getWorld().equals(selection.getMaximumPoint().getWorld())) {
                                            player.sendMessage("§7Setting all positions in configuration...");
                                            Location min = selection.getMinimumPoint();
                                            Location max = selection.getMaximumPoint();
                                            ConfigurationSection section = plugin.getConfig().getConfigurationSection("warps." + warpName + ".discover-region");

                                            {
                                                section.set("point-one.world", min.getWorld().getName());
                                                section.set("point-one.x", min.getX());
                                                section.set("point-one.y", min.getY());
                                                section.set("point-one.z", min.getZ());
                                            }

                                            {
                                                section.set("point-two.world", max.getWorld().getName());
                                                section.set("point-two.x", max.getX());
                                                section.set("point-two.y", max.getY());
                                                section.set("point-two.z", max.getZ());
                                            }

                                            player.sendMessage("§e" + warpName + "'s §7discovery region has been set.");
                                        } else {
                                            player.sendMessage("§4§lX §cYour WorldEdit selections must be in the same world.");
                                        }
                                    }
                                } else {
                                    sender.sendMessage("§4§lX §cYou have to be a player to perform this command.");
                                }
                            } else if (attribute.equalsIgnoreCase("warp-message")) {
                                if (args.length == 3) {
                                    sender.sendMessage(attributes);
                                } else {
                                    StringBuilder builder = new StringBuilder();

                                    for (int i = 3; i < args.length; i++) {
                                        builder.append(args[i]).append(" ");
                                    }

                                    String message = builder.toString().trim();

                                    plugin.getConfig().set("warps." + warpName + ".warp-message", message);

                                    sender.sendMessage("§e" + warpName + "'s §7warp message has been set to §e" + ChatColor.translateAlternateColorCodes('&', message));
                                }
                            } else if (attribute.equalsIgnoreCase("required-permission")) {
                                // /mm set <warpname> required-permission <permission>

                                if (args.length == 3) {
                                    plugin.getConfig().set("warps." + warpName + ".required-permission", "");

                                    sender.sendMessage("§e" + warpName + "'s §7required permission has been removed");
                                } else {
                                    String permission = args[3];

                                    plugin.getConfig().set("warps." + warpName + ".required-permission", permission);

                                    sender.sendMessage("§e" + warpName + "'s §7required permission has been set to §e" + permission);
                                }
                            } else {
                                sender.sendMessage(attributes);
                            }

                            plugin.saveConfig();
                        } else {
                            sender.sendMessage("§4§lX §cThat warp doesn't exist.");
                        }
                    }
                } else {
                    sender.sendMessage("§4§lX §cYou don't have permission.");
                }
            } else if (sub.equalsIgnoreCase("create")) {
                if (sender.hasPermission("maraudersmap.maraudersmap.create")) {
                    if (args.length == 1) {
                        sender.sendMessage(help);
                    } else {
                        String warpName = args[1];

                        if (plugin.getWarpUtility().warpExists(warpName)) {
                            sender.sendMessage("§4§lX §cThat warp already exists. Do §4§l/mm set §cto  set a warp's attributes.");
                        } else {
                            ConfigurationSection section = plugin.getConfig().createSection("warps." + warpName);

                            section.set("item.material", "STONE");
                            section.set("item.amount", 1);

                            section.set("warp-message", "");

                            section.set("warp-commands.player", Collections.singletonList(""));
                            section.set("warp-commands.command", Collections.singletonList(""));

                            section.set("enabled", true);
                            section.set("discoverable", true);

                            section.set("warp-location.world", "world");
                            section.set("warp-location.x", 0);
                            section.set("warp-location.y", 0);
                            section.set("warp-location.z", 0);

                            section.set("discover-region.point-one.world", "world");
                            section.set("discover-region.point-one.x", 0);
                            section.set("discover-region.point-one.y", 0);
                            section.set("discover-region.point-one.z", 0);

                            section.set("discover-region.point-two.world", "world");
                            section.set("discover-region.point-two.x", 0);
                            section.set("discover-region.point-two.y", 0);
                            section.set("discover-region.point-two.z", 0);

                            sender.sendMessage("§7Warp §e'" + warpName + "' §7has been created.");
                        }

                        plugin.saveConfig();
                    }
                } else {
                    sender.sendMessage("§4§lX §cYou don't have permission.");
                }
            } else {
                sender.sendMessage(help);
            }
        }
    }
}
