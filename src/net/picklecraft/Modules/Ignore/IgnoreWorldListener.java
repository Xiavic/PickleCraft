package net.picklecraft.Modules.Ignore;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

public class IgnoreWorldListener implements Listener  {
	private IgnoreModule module;
	public IgnoreWorldListener(IgnoreModule module) {
		this.module = module;
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onWorldSave(WorldSaveEvent event) {
		module.Save();
	}
}
