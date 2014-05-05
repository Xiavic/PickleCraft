package net.picklecraft.Modules.Timber;

import net.picklecraft.PickleCraftPlugin;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

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

public class TimberBlockListener implements Listener {

    private final TimberModule module;

    public TimberBlockListener(TimberModule module) {
        this.module = module;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isCancelled()) {
            if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                if (event.getBlock().getType() != TimberModule.treeMaterials[0]
                        && event.getBlock().getType() != TimberModule.treeMaterials[1]) {
                    return;
                }
                if (PickleCraftPlugin.hasPerm(event.getPlayer(), "PickleCraft.timber.cut")) {
                    if (event.getPlayer().getItemInHand().getType() == Material.IRON_AXE
                            || event.getPlayer().getItemInHand().getType() == Material.GOLD_AXE
                            || event.getPlayer().getItemInHand().getType() == Material.DIAMOND_AXE) {
                        module.CutTree(event.getPlayer(), event.getBlock());
                    }
                }
            }
        }
    }
}
