package net.picklecraft.Modules.Counter;

import java.util.ArrayList;
import java.util.List;

import net.picklecraft.PickleCraftPlugin;
import net.picklecraft.Modules.IModule;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
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
 *
 * @author Pickle
 */
public class CounterModule implements IModule, Runnable {

    private final PickleCraftPlugin plugin;

    private BlockListener blockListener;

    private List<CounterSign> signs;

    public CounterModule(PickleCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onDisable() {
        signs.clear();
        signs = null;
    }

    @Override
    public void onEnable() {
        blockListener = new BlockListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(blockListener, plugin);
        signs = new ArrayList<>();
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, this, 1000, 1000);
    }

    @Override
    public PickleCraftPlugin getPlugin() {
        return plugin;
    }

    @Override
    public String getName() {
        return "Counter";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    @Override
    public void sendCommandList(CommandSender sender) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (player != null) {
            if (PickleCraftPlugin.hasPerm(player, "IgnoreCraft.ignore")) {
                player.sendMessage(
                        plugin.getStringFromConfig("counter.messages.commandhelplist.header", "")
                );
                player.sendMessage(
                        plugin.getStringFromConfig("counter.messages.commandhelplist.msg",
                                plugin.getStringFromConfig("counter.counterline", ""))
                );
            }
        }
    }

    public boolean isCounterSign(Sign sign) {
        return sign.getLine(0).equalsIgnoreCase(plugin.getStringFromConfig("counter.counterline", ""));
    }

    public void Count(Sign sign, boolean count) {
        if (isCounterSign(sign)) {
            for (int i = 0; i < signs.size(); i++) {
                CounterSign cSign = signs.get(i);
                if (cSign.getSign() == sign) {
                    if (count) {
                        cSign.Count();
                    }
                }
            }
            CounterSign cSign = CounterSign.create(sign);
            if (count) {
                cSign.Count();
            }
            signs.add(cSign);
        }
    }

    @Override
    public void run() {
        //Update our sign cache.
        for (int i = 0; i < signs.size(); i++) {
            if (signs.get(i).isExpired()) {
                signs.remove(i);
            }
        }
    }

}
