package net.picklecraft;

import com.sk89q.wepif.PermissionsResolverManager;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import net.picklecraft.Modules.IModule;
import net.picklecraft.Modules.ModuleManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Copyright (c) 2011-2014
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Pickle
 *
 */
public class PickleCraftPlugin extends JavaPlugin implements Listener {

    public static final Logger log = Bukkit.getLogger();
    public static ModuleManager moduleManager = null;
    public static final Pattern colorPattern = Pattern.compile("&");
    public static final Pattern colorStripPattern = Pattern.compile("&[0-9a-fk-rA-FK-R]");
    public static final Pattern colorSpecialStripPattern = Pattern.compile("&[klmno]");

    public static final Gson gson = new Gson();

    private static boolean worldedit;
    
	private int spamblock;
    
    private final Random random = new Random();
    /*
     * returns Player and a Boolean (that defines if multiple players was found).
     * first value will be Player
     * second will be Boolean
     *
     * Will return if An exact match was found,
     *  and the second value shall be false.
     */
    public static Object[] getPlayer(String name) {
        name = name.toLowerCase();
        Object[] playerAndbool = new Object[2];
        playerAndbool[0] = null;
        playerAndbool[1] = false;
        int playerMatch = 0;
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            String name2 = p.getName().toLowerCase();
            if (name2.contains(name)) {
                playerAndbool[0] = p;
                if (name2.equals(name)) {
                    playerAndbool[1] = false;
                    break;
                } else if (++playerMatch > 1) {
                    playerAndbool[1] = true;
                    break;
                }
            }
        }
        return playerAndbool;
    }

    public static String Colorize(String string) {
        return colorPattern.matcher(string).replaceAll("\u00A7");
    }
    /*
     * Strips annoying colors from players without perm.
     */

    public static String StripColor(String string, Player player) {
        if (PickleCraftPlugin.hasPerm(player, "PickleCraft.colors")) {
            if (PickleCraftPlugin.hasPerm(player, "PickleCraft.colors.special")) {
                /* Do nothing. */
                return string;
            }
            /*strip annoying colors like "&k" */
            return colorSpecialStripPattern.matcher(string).replaceAll("");
        }
        //strip all colors.
        return colorStripPattern.matcher(string).replaceAll("");
    }

    public String getStringFromConfig(String path) {
        return Colorize(getConfig().getString(path));
    }

    public String getStringFromConfig(String path, Object... args) {
        try {
            return Colorize(String.format(getConfig().getString(path), args));
        } catch (IllegalFormatException e) {
            log.warning("Check Config: " + path);
            return ChatColor.RED + "Check Config: " + path;
        }
    }

    public static boolean hasPerm(Player player, String perm) {
        if (worldedit) {
            return PermissionsResolverManager.getInstance().hasPermission(player, perm);
        }
        return player.hasPermission(perm);
    }

    public static boolean hasWorldEdit() {
        return worldedit;
    }

    public void reload() {
        reloadConfig();
        Bukkit.broadcastMessage(Colorize("&2Derp! reloading the plugin!"));
        //Bukkit.getPluginManager().disablePlugin(this);
        //Bukkit.getPluginManager().enablePlugin(this);
        moduleManager.reloadModules();
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        moduleManager.unloadModules();
        //saveConfig();
    }

    @Override
    public void onEnable() {
		spamblock = 0;
        if (moduleManager == null) {
            moduleManager = new ModuleManager(this);
        }
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldEdit")) {
            PermissionsResolverManager.initialize(this); //wepif
            PickleCraftPlugin.worldedit = true;
        }
        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();
        /* set up new modules */
        moduleManager.loadModules();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if(cmd.getName().equalsIgnoreCase("pray")){
			if(sender instanceof Player){
				sender.sendMessage(ChatColor.DARK_GREEN + "PickleLord" + ChatColor.GREEN + " has recieved your prayer.");
				if(spamblock <= 10){
				System.out.println("PickleLord, you have recieved a prayer.");
				spamblock = spamblock + 1;
				}
			}
			else{
				sender.sendMessage("Sending a prayer to The_Fireplace, for making this command.");
			}
		}
        if (command.getName().equalsIgnoreCase("picklecraft")) {
            if (args.length > 0) {
                if (player != null) {
                    if (hasPerm(player, "PickleCraft.reload")) {
                        if (args[0].equalsIgnoreCase("reload")) {
                            reload();
                        }
                    }
                }
            } else {
                PluginDescriptionFile pdfFile = this.getDescription();
                sender.sendMessage(ChatColor.DARK_GREEN + pdfFile.getName() + " Version " + pdfFile.getVersion());
                for (IModule module : moduleManager.modules) {
                    module.sendCommandList(sender);
                }
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("pony")) {
            if (player != null) {
                if (args.length >= 1) {
                    Pony.Say(player, args[0]);
                } else {
                    Pony.SayRandom(player);
                }
            } else {
                sender.sendMessage("This is a player only command.");
            }
            return true;
        } else {
            boolean success = false;
            for (IModule module : moduleManager.modules) {
                if (module.onCommand(sender, command, label, args) == true) {
                    success = true;
                }
            }
            return success;
        }
    }


    public void Damage(Player player, int amount) {
        ItemStack item = player.getItemInHand();
        short d = (short)(item.getDurability() + amount);
        
        //If item has unbreaking enchantment, then give a chance to not break!
        Map<Enchantment, Integer> enchantments = item.getEnchantments();
        if (enchantments.containsKey(Enchantment.DURABILITY)) {
            int level = enchantments.get(Enchantment.DURABILITY) + 1;
            float rand = random.nextFloat();
           
            float ratio = (1 / level)+0.1F; 
            //If the chance is greater than the chance ratio, then do not damage the item.
            if (rand > ratio) {
                d = item.getDurability();
            }
            
        }
        
        
        item.setDurability(d);
        if (d > player.getItemInHand().getType().getMaxDurability()) {
            item.setType(Material.AIR);
            player.setItemInHand(item);
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1, 1);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPluginDisabled(PluginDisableEvent event) {
        if ("WorldEdit".equals(event.getPlugin().getName())) {
            PickleCraftPlugin.worldedit = false;
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPluginEnabled(PluginEnableEvent event) {
        if ("WorldEdit".equals(event.getPlugin().getName())) {
            PickleCraftPlugin.worldedit = true;
            PermissionsResolverManager.initialize(this);
        }
    }
}
