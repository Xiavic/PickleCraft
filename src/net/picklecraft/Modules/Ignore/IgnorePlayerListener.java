package net.picklecraft.Modules.Ignore;

import net.picklecraft.PickleCraftPlugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

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
public class IgnorePlayerListener implements Listener {

    IgnoreModule module;

    public IgnorePlayerListener(IgnoreModule module) {
        this.module = module;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!PickleCraftPlugin.hasPerm(event.getPlayer(), "PickleCraft.ignore.cantbeignored")) {
            Player[] players = event.getRecipients().toArray(new Player[0]);
            for (Player player : players) {
                IgnorePlayer igPlayer = module.getIgnorePlayer(player);
                if (igPlayer != null) {
                    if (igPlayer.isIgnored(event.getPlayer().getUniqueId()) || igPlayer.isAllIgnored()) {
                        event.getRecipients().remove(player);
                    }
                }
            }
        }
    }
    
}
