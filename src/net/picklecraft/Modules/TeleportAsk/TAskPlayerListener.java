package net.picklecraft.Modules.TeleportAsk;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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

public class TAskPlayerListener implements Listener {

    TeleportAskModule module;

    public TAskPlayerListener(TeleportAskModule module) {
        this.module = module;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        module.teleportPlayerList.add(new TeleportPlayer(module, event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerKick(PlayerKickEvent event) {
        TeleportPlayer p = module.getTeleportPlayer(event.getPlayer());
        if (p != null) {
            p.Deny();
            module.teleportPlayerList.remove(p);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent event) {
        TeleportPlayer p = module.getTeleportPlayer(event.getPlayer());
        if (p != null) {
            p.Deny();
            module.teleportPlayerList.remove(p);
        }
    }

}
