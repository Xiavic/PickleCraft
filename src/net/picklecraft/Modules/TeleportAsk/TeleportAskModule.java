package net.picklecraft.Modules.TeleportAsk;

import java.util.ArrayList;
import java.util.List;
import net.picklecraft.Modules.IModule;
import net.picklecraft.PickleCraftPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
public class TeleportAskModule implements IModule {

    public List<TeleportPlayer> teleportPlayerList = new ArrayList<>();

    private final PickleCraftPlugin plugin;

    private TAskPlayerListener taPlayerListener;

    public TeleportAskModule(PickleCraftPlugin plugin) {
        this.plugin = plugin;
    }

    public TeleportPlayer getTeleportPlayer(Player player) {
        if (player != null) {
            for (TeleportPlayer p : teleportPlayerList) {
                if (p.getPlayer() == player) {
                    return p;
                }
            }
            TeleportPlayer p = new TeleportPlayer(this, player);
            teleportPlayerList.add(p);
            return p;
        }
        return null;
    }

    @Override
    public PickleCraftPlugin getPlugin() {
        return plugin;
    }

    @Override
    public String getName() {
        return "TeleportAgree";
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        taPlayerListener = new TAskPlayerListener(this);
        plugin.getServer().getPluginManager().registerEvents(taPlayerListener, plugin);
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            teleportPlayerList.add(new TeleportPlayer(this, player));
        }
        /* run update on teleport players..
         * 20L = 1 second worth of ticks.
         */
        plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (TeleportPlayer player : teleportPlayerList) {
                    player.Update();
                }
            }
        }, 0L, 20L);
    }

    @Override
    public void sendCommandList(CommandSender sender) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (player != null) {
            Command c = plugin.getCommand("tpa");
            if (PickleCraftPlugin.hasPerm(player, c.getPermission())) {
                player.sendMessage(
                        plugin.getStringFromConfig("teleportask.messages.commandhelplist.header")
                );
                player.sendMessage(c.getUsage() + " " + c.getDescription());
            }
            c = plugin.getCommand("tpahere");
            if (PickleCraftPlugin.hasPerm(player, c.getPermission())) {
                player.sendMessage(c.getUsage() + " " + c.getDescription());
            }
            c = plugin.getCommand("tpac");
            if (PickleCraftPlugin.hasPerm(player, c.getPermission())) {
                player.sendMessage(c.getUsage() + " " + c.getDescription());
            }
        }
    }

    /*
    * Note: This needs to be broken up and reworked.
    */
    @Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        /**
         * send a teleport request
         */
        if (command.getName().equalsIgnoreCase("tpa")) {
            if (player != null) {
                if (PickleCraftPlugin.hasPerm(player, command.getPermission())) {
                    /**
                     * ability for playerA to send a dual request to playerB and
                     * playerC playerC to playerB
                     */
                    if (args.length >= 2) {
                        if (PickleCraftPlugin.hasPerm(player, "PickleCraft.teleport.dualplayers")) {
                            Object[] player_B_Andbool = PickleCraftPlugin.getPlayer(args[1]);
                            Object[] player_C_Andbool = PickleCraftPlugin.getPlayer(args[0]);
                            Player pB = (Player) player_B_Andbool[0]; //playerB
                            Player pC = (Player) player_C_Andbool[0]; //playerC
                            if (pB != null) {
                                if (pC != null) {
                                    if ((Boolean) player_C_Andbool[1] == false) {
                                        if ((Boolean) player_B_Andbool[1] == false) {
                                            if (pB != pC) {
                                                Command c = plugin.getCommand("tpaccept");
                                                if (PickleCraftPlugin.hasPerm(pB, c.getPermission())) {
                                                    if (PickleCraftPlugin.hasPerm(pC, c.getPermission())) {
                                                        getTeleportPlayer(player).DualRequest(
                                                                getTeleportPlayer(pB),
                                                                getTeleportPlayer(pC)
                                                        );
                                                    } else {
                                                        player.sendMessage(
                                                                plugin.getStringFromConfig("teleportask.messages.errors.cannotaccept", pC.getName())
                                                        );
                                                    }
                                                } else {
                                                    player.sendMessage(
                                                            plugin.getStringFromConfig("teleportask.messages.errors.cannotaccept", pB.getName())
                                                    );
                                                }
                                            } else {
                                                player.sendMessage(
                                                        plugin.getStringFromConfig("common.messages.errors.sameplayer", args[0], args[1])
                                                );
                                            }
                                        } else {
                                            player.sendMessage(
                                                    plugin.getStringFromConfig("common.messages.errors.tomanyplayers", args[0])
                                            );
                                        }
                                    } else {
                                        player.sendMessage(
                                                plugin.getStringFromConfig("common.messages.errors.tomanyplayers", args[1])
                                        );
                                    }
                                } else {
                                    player.sendMessage(
                                            plugin.getStringFromConfig("common.messages.errors.playerdontexist", args[1])
                                    );
                                }
                            } else {
                                player.sendMessage(
                                        plugin.getStringFromConfig("common.messages.errors.playerdontexist", args[0])
                                );
                            }
                        } else {
                            player.sendMessage(
                                    plugin.getStringFromConfig("common.messages.errors.donthaveperm")
                            );
                        }
                        return true;
                    } /**
                     * Standard teleport.
                     */
                    else if (args.length >= 1) {
                        Object[] playerAndbool = PickleCraftPlugin.getPlayer(args[0]);
                        Player p = (Player) playerAndbool[0];
                        if (p != null) {
                            if ((Boolean) playerAndbool[1] == false) {
                                Command c = plugin.getCommand("tpaccept");
                                if (PickleCraftPlugin.hasPerm(p, c.getPermission())) {
                                    getTeleportPlayer(p).Request(getTeleportPlayer(player), false);
                                } else {
                                    player.sendMessage(
                                            plugin.getStringFromConfig("teleportask.messages.errors.cannotaccept", p.getName())
                                    );
                                }

                            } else {
                                player.sendMessage(
                                        plugin.getStringFromConfig("common.messages.errors.tomanyplayers", args[0])
                                );
                            }
                        } else {
                            player.sendMessage(
                                    plugin.getStringFromConfig("common.messages.errors.playerdontexist", args[0])
                            );
                        }
                        return true;
                    }
                } else {
                    player.sendMessage(
                            plugin.getStringFromConfig("common.messages.errors.donthaveperm")
                    );
                    return true;
                }
            } else {
                sender.sendMessage("This is a player only command.");
                return true;
            }
        } /**
         * teleport to coordinates
         */
        else if (command.getName().equalsIgnoreCase("tpac")) {
            if (player != null) {
                if (PickleCraftPlugin.hasPerm(player, command.getPermission())) {
                    if (args.length >= 1) {
                        String[] coord = args[0].split(",");
                        if (coord.length >= 2) {
                            try {
                                int x = Integer.parseInt(coord[0]);
                                int y = 0;
                                int z;
                                if (coord.length >= 3) {
                                    y = Integer.parseInt(coord[1]);
                                    z = Integer.parseInt(coord[2]);
                                } else {
                                    z = Integer.parseInt(coord[1]);
                                }
                                if (!getTeleportPlayer(player).TeleportToCoord(x, y, z)) {
                                    player.sendMessage(
                                            plugin.getStringFromConfig("teleportask.messages.errors.failteleport")
                                    );
                                }
                            } catch (NumberFormatException e) {
                                player.sendMessage(
                                        plugin.getStringFromConfig("common.messages.errors.parsefail", "coordinates")
                                );
                            }
                            return true;
                        }
                    }
                } else {
                    player.sendMessage(
                            plugin.getStringFromConfig("common.messages.errors.donthaveperm")
                    );
                    return true;
                }
            } else {
                sender.sendMessage("This is a player only command.");
                return true;
            }
        } /**
         * similar to TPA but teleports playerB to playerA
         */
        else if (command.getName().equalsIgnoreCase("tpahere")) {
            if (player != null) {
                if (PickleCraftPlugin.hasPerm(player, command.getPermission())) {
                    if (args.length >= 1) {
                        Object[] playerAndbool = PickleCraftPlugin.getPlayer(args[0]);
                        Player p = (Player) playerAndbool[0];
                        if (p != null) {
                            if ((Boolean) playerAndbool[1] == false) {
                                Command c = plugin.getCommand("tpaccept");
                                if (PickleCraftPlugin.hasPerm(p, c.getPermission())) {
                                    getTeleportPlayer(p).Request(getTeleportPlayer(player), true);
                                } else {
                                    player.sendMessage(
                                            plugin.getStringFromConfig("teleportask.messages.errors.cannotaccept", p.getName())
                                    );
                                }
                            } else {
                                player.sendMessage(
                                        plugin.getStringFromConfig("common.messages.errors.tomanyplayers", args[0])
                                );
                            }
                        } else {
                            player.sendMessage(
                                    plugin.getStringFromConfig("common.messages.errors.playerdontexist", args[0])
                            );
                        }
                        return true;
                    }
                } else {
                    player.sendMessage(
                            plugin.getStringFromConfig("common.messages.errors.donthaveperm")
                    );
                    return true;
                }
            } else {
                sender.sendMessage("This is a player only command.");
                return true;
            }
        } /**
         * accept request?
         */
        else if (command.getName().equalsIgnoreCase("tpaccept")) {
            if (player != null) {
                if (PickleCraftPlugin.hasPerm(player, command.getPermission())) {
                    getTeleportPlayer(player).Accept();
                } else {
                    player.sendMessage(
                            plugin.getStringFromConfig("common.messages.errors.donthaveperm")
                    );
                    return true;
                }
            } else {
                sender.sendMessage("This is a player only command.");
            }
            return true;
        } /**
         * Nope! chuck testa!
         */
        else if (command.getName().equalsIgnoreCase("tpdeny")) {
            if (player != null) {
                getTeleportPlayer(player).Deny();
            } else {
                sender.sendMessage("This is a player only command.");
            }
            return true;
        } /**
         * return to last location before teleport.
         */
        else if (command.getName().equalsIgnoreCase("tpareturn")) { //return to last location pior to teleport
            if (player != null) {
                if (PickleCraftPlugin.hasPerm(player, command.getPermission())) {
                    getTeleportPlayer(player).ReturnToLastLocation();
                } else {
                    player.sendMessage(
                            plugin.getStringFromConfig("common.messages.errors.donthaveperm")
                    );
                    return true;
                }
            } else {
                sender.sendMessage("This is a player only command.");
            }
            return true;
        }
        return false;
    }

}
