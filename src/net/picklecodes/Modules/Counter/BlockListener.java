package net.picklecodes.Modules.Counter;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

public class BlockListener implements Listener {

	CounterModule module;
	
	public BlockListener(CounterModule module) {
		this.module = module;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onSignChange(SignChangeEvent event) {
		Sign sign = (Sign)event.getBlock().getState();
		module.Count(sign, false);
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onRedstonePower(BlockRedstoneEvent event) {
		if (event.getNewCurrent() > 0 && event.getOldCurrent() == 0) {
			if (event.getBlock().getState() instanceof Sign) { return; }
			World world = event.getBlock().getWorld();
			int X = event.getBlock().getX();
			int Z = event.getBlock().getZ();
			int Y = event.getBlock().getY();
			//lets look for a sign.
			Block b = null;
			for (int i = 0; i < 4; i++) {
				if (i == 0) { b = world.getBlockAt(X-1,Y,Z); }
				else if (i == 1) { b = world.getBlockAt(X+1,Y,Z); }
				else if (i == 2) { b = world.getBlockAt(X,Y,Z-1); }
				else if (i == 3) { b = world.getBlockAt(X,Y,Z+1); }
				if (b != null) {
					if (b.getState() instanceof Sign) {	
						Sign sign = (Sign)b.getState();
						module.Count(sign, true);
					}
				}
			}
		}
	}
}
