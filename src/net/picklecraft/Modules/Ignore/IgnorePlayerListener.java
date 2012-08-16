package net.picklecraft.Modules.Ignore;

import net.picklecraft.PickleCraftPlugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class IgnorePlayerListener implements Listener {
	IgnoreModule module;
	public IgnorePlayerListener(IgnoreModule module) {
		this.module = module;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat (AsyncPlayerChatEvent event) {
		Player[] players = event.getRecipients().toArray(new Player[0]);
		for (int i = 0; i < players.length; i++) {
			IgnorePlayer igPlayer = module.getIgnorePlayer(players[i]);
			if (igPlayer != null) {
				if (igPlayer.isIgnored(event.getPlayer()) || igPlayer.isAllIgnored()) {
					if (!PickleCraftPlugin.hasPerm(event.getPlayer(), "PickleCraft.ignore.cantbeignored")) {
						event.getRecipients().remove(players[i]);
					}
				}
			}
		}
	}
}
