package net.picklecraft.Modules.PrivateWarps;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.picklecraft.Modules.IModule;
import net.picklecraft.PickleCraftPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;
/**
 * Copyright (c) 2011-2012
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Pickle
 *
 */
public class PWModule implements IModule, Listener {
	public List<PWPlayer> players = new ArrayList<PWPlayer>();
	public HashMap<String,Integer> maxLimitRanks = new HashMap<String,Integer>();

	private PickleCraftPlugin plugin;
    private File pwFile;

	public PWModule(PickleCraftPlugin plugin) {
            this.plugin = plugin;
            pwFile = new File(plugin.getDataFolder() +"/warps.json");
            if (!pwFile.exists()) {
                try {
                    //pwFile.mkdirs();
                    pwFile.createNewFile();
                } catch (IOException ex) {
                    PickleCraftPlugin.log.log(Level.SEVERE, null, ex);
                }
            }
	}

	@Override
	public PickleCraftPlugin getPlugin() {
            return plugin;
	}
	@Override
	public String getName() {
            return "PrivateWarps";
	}
	@Override
	public void onDisable() {
            Save();
	}

	public HashMap<String,Integer> GetMaxLimitRanks() {
		return maxLimitRanks;
	}
	@Override
	public void onEnable() {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
            Load();
			/**
			 * Get rank "limit" values from config and parse them.
			 */
			ArrayList<String> ranks = (ArrayList<String>) plugin.getConfig().getStringList("privatewarps.ranks");
			for (int i = 0; i < ranks.size(); i++) {
				String[] tok = ranks.get(i).split(":");
				maxLimitRanks.put(tok[0], Integer.getInteger(tok[1]));
			}
	}

	@Override
	public void sendCommandList(CommandSender sender) {
            Player player = null;
            if (sender instanceof Player) {
                    player = (Player) sender;
            }
            if (player != null) {
                Command c = plugin.getCommand("pw");
                if (PickleCraftPlugin.hasPerm(player, c.getPermission())) {
                    player.sendMessage(
                        plugin.getStringFromConfig("privatewarps.messages.commandhelplist.header")
                        );
                    player.sendMessage(c.getUsage() +" "+ c.getDescription());
                }
            }
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
                String label, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		/*
		 * ignore command.
		 */
		if (command.getName().equalsIgnoreCase("pw")){
                    if (player != null) {
                            if (PickleCraftPlugin.hasPerm(player, command.getPermission())) {
                                if (args.length >= 1) {
                                    if (args.length >= 2) {
                                        if (args[0].equalsIgnoreCase("set")) {
                                            setWarp(player,args[1]);
                                        }
                                        else if (args[0].equalsIgnoreCase("remove")) {
                                            removeWarp(player,args[1]);
                                        }
                                    }
                                    else if (args[0].equalsIgnoreCase("list")) {
                                        listWarps(player);
                                    }
                                    else {
                                        teleportToWarp(player,args[0]);
                                    }
                                    return true;
                                }
                                return false;
                            }
                            else {
                                player.sendMessage(
                                    plugin.getStringFromConfig("common.messages.errors.donthaveperm")
                                    );
                                return true;
                            }
			}
			else {
                            sender.sendMessage("This is a player only command.");
                            return true;
			}
                }
		return false;
	}

        private PWPlayer getWarpPlayer(Player player) {
            for (PWPlayer p : players) {
                if (player.getName().equalsIgnoreCase(p.getName())) {
                    return p;
                }
            }
            PWPlayer p = new PWPlayer(this,player.getName());
            players.add(p);
            return p;
        }

        private void setWarp(Player player, String name) {
            if (name != null) {
                PWPlayer p = getWarpPlayer(player);
                if (p.setWarp(name, player.getLocation())) {
                    player.sendMessage(
                        plugin.getStringFromConfig("privatewarps.messages.info.warpset")
                        );
                }
                else {
                    player.sendMessage(
                        plugin.getStringFromConfig("privatewarps.messages.errors.warpexist",name)
                        );
                }
            }
        }
        private void removeWarp(Player player, String name) {
            if (name != null) {
                PWPlayer p = getWarpPlayer(player);
                if (p.removeWarp(name)) {
                    player.sendMessage(
                        plugin.getStringFromConfig("privatewarps.messages.info.warpremove")
                        );
                }
                else {
                    player.sendMessage(
                        plugin.getStringFromConfig("privatewarps.messages.errors.warpnoexist",name)
                        );
                }
            }
        }
        private void teleportToWarp(Player player, String name) {
            if (name != null) {
                PWPlayer p = getWarpPlayer(player);
                Warp warp = p.getWarp(name);
                if (warp != null) {
                    player.teleport(warp.getLocation());
                }
                else {
                    player.sendMessage(
                        plugin.getStringFromConfig("privatewarps.messages.errors.warpnoexist",name)
                        );
                }
            }
        }
        private void listWarps(Player player) {
            PWPlayer p = getWarpPlayer(player);
            ArrayList<Warp> warps = (ArrayList<Warp>) p.getWarps();
            if (warps.size() <= 0) {
                player.sendMessage(
                    plugin.getStringFromConfig("privatewarps.messages.errors.nowarps")
                    );
            }
            else {
                StringBuilder s = new StringBuilder();
                s.append("&2Warps: ");
                for (int i = 0; i < warps.size(); i++) {
                    s.append("&e");
                    s.append(warps.get(i).getName());
                    s.append(",");
                    if (i % 8 == 0 && i != 0 ) {
                        player.sendMessage(PickleCraftPlugin.Colorize(s.toString()));
                        s = new StringBuilder();
                    }
                }
                String d = s.toString();
                if (!d.isEmpty()) {
                    //incase not enough indexs to fire the sendmessage in le loop :c
                    player.sendMessage(PickleCraftPlugin.Colorize(d));
                }
            }
        }

	public void Save() {
            try (FileWriter fw = new FileWriter(pwFile); JsonWriter writer = new JsonWriter(fw)) {
                writer.setIndent(" ");
                writer.beginArray();
                for (PWPlayer player : players) {
                    if (player.getWarps().isEmpty()) {
                        continue;
                    }
                    writer.beginObject();
                    writer.name("player").value(player.getName());
                    if (player.getWarps().size() > 0) {
                         writer.name("warps");
                         writer.beginArray();
                         for (Warp w : player.getWarps()) {
                             writer.beginObject();
                             writer.name("name").value(w.getName());
                             writer.name("location");
                             writer.beginObject();
                             writer.name("x").value((int)w.getLocation().getX());
                             writer.name("y").value((int)w.getLocation().getY());
                             writer.name("z").value((int)w.getLocation().getZ());
                             writer.name("world").value(w.getLocation().getWorld().getName());
                             writer.endObject();
                             writer.endObject();
                         }
                         writer.endArray();
                    }
                    else {
                        writer.name("warps").nullValue();
                    }
                    writer.endObject();
                }
                writer.endArray();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
	}

	public void Load() {
            try {
                PWPlayer player = null;
                FileReader fr = new FileReader(pwFile);
                JsonReader reader = new JsonReader(fr);
                reader.beginArray();
                while(reader.hasNext()) {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        String name = reader.nextName();
                        if (name.equalsIgnoreCase("player")) {
                            player = new PWPlayer(this,reader.nextString());
                        }
                        else if (name.equalsIgnoreCase("warps")) {
                            reader.beginArray();
                            while (reader.hasNext()) {
                                String warpname = "";
                                Location loc = new Location(null,0,0,0);
                                reader.beginObject();
                                while (reader.hasNext()) {
                                    String n = reader.nextName();
                                    if (n.equalsIgnoreCase("name")) {
                                        warpname = reader.nextString();
                                    }
                                    else if (n.equalsIgnoreCase("location")) {
                                        reader.beginObject();
                                        while (reader.hasNext()) {
                                            String a = reader.nextName();
                                            if (a.equalsIgnoreCase("x")) {
                                            loc.setX(reader.nextInt());
                                            }
                                            else if (a.equalsIgnoreCase("y")) {
                                            loc.setY(reader.nextInt());
                                            }
                                            else if (a.equalsIgnoreCase("z")) {
                                            loc.setZ(reader.nextInt());
                                            }
                                            else if (a.equalsIgnoreCase("world")) {
                                                World w = plugin.getServer().getWorld(reader.nextString());
                                                loc.setWorld(w);
                                            }
                                        }
                                        reader.endObject();
                                    }
                                }
                                reader.endObject();
                                if (!warpname.isEmpty()) {
                                    player.setWarp(warpname, loc);
                                }
                            }

                            reader.endArray();
                        }
                    }
                   reader.endObject();

                   players.add(player);
               }
               reader.endArray();
               reader.close();
               fr.close();
            } catch (EOFException e) {
                //Ignore.
            } catch (IOException e) {
                e.printStackTrace();
            }
	}


        @EventHandler(priority = EventPriority.LOW)
	public void onWorldSave(WorldSaveEvent event) {
            //save json when the world does.
            Save();
	}

}
