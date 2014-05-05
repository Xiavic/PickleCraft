package net.picklecraft.Modules.Counter;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

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

public class BlockListener implements Listener {

    CounterModule module;

    public BlockListener(CounterModule module) {
        this.module = module;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSignChange(SignChangeEvent event) {
        if (event.getBlock().getState() instanceof Sign) {
            Sign sign = (Sign) event.getBlock().getState();
            module.Count(sign, false);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onRedstonePower(BlockRedstoneEvent event) {
        if (event.getNewCurrent() > 0 && event.getOldCurrent() == 0) {
            if (event.getBlock().getState() instanceof Sign) {
                return;
            }
            World world = event.getBlock().getWorld();
            int X = event.getBlock().getX();
            int Z = event.getBlock().getZ();
            int Y = event.getBlock().getY();
            //lets look for a sign.
            Block b = null;
            for (int y = 0; y < 2; y++) {
                for (int i = 0; i < 4; i++) {
                    if (i == 0) {
                        b = world.getBlockAt(X - 1, Y + y, Z);
                    } else if (i == 1) {
                        b = world.getBlockAt(X + 1, Y + y, Z);
                    } else if (i == 2) {
                        b = world.getBlockAt(X, Y + y, Z - 1);
                    } else if (i == 3) {
                        b = world.getBlockAt(X, Y + y, Z + 1);
                    }
                    if (b != null) {
                        if (b.getState() instanceof Sign) {
                            Sign sign = (Sign) b.getState();
                            module.Count(sign, true);
                        }
                    }
                }
            }
        }
    }
}
