package net.picklecraft.Modules.Timber;

import net.picklecraft.PickleCraftPlugin;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class TimberBlockListener implements Listener {
	private TimberModule module;
	public TimberBlockListener(TimberModule module) {
		this.module = module;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockBreakEvent event) {
		if (!event.isCancelled()) {
			if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
				if (event.getBlock().getType() != TimberModule.treeMaterials[0] &&
						event.getBlock().getType() != TimberModule.treeMaterials[1]) {
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
