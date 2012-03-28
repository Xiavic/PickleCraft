package net.picklecodes.Modules.TeleportAsk;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TAskPlayerListener implements Listener {
		TeleportAskModule module;
		public TAskPlayerListener(TeleportAskModule module) {
			this.module = module;
		}

		@EventHandler(priority = EventPriority.LOW)
		public void onPlayerJoin (PlayerJoinEvent event) {
			module.teleportPlayerList.add(new TeleportPlayer(module,event.getPlayer()));
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
		public void onPlayerQuit (PlayerQuitEvent event) {
			TeleportPlayer p = module.getTeleportPlayer(event.getPlayer());
			if (p != null) {
				p.Deny();
				module.teleportPlayerList.remove(p);
			}
		}
		
		

}
