package net.picklecraft.Modules.Ignore;

import net.picklecraft.PickleCraftPlugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class IgnorePlayerListener implements Listener {
	IgnoreModule module;
	public IgnorePlayerListener(IgnoreModule module) {
		this.module = module;
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChat (PlayerChatEvent event) {
		Player[] players = event.getRecipients().toArray(new Player[0]);
		for (int i = 0; i < players.length; i++) {
			IgnorePlayer igPlayer = module.playerIgnoreList.get(players[i]);
			if (igPlayer != null) {
				if (igPlayer.isIgnored(event.getPlayer()) || igPlayer.isAllIgnored()) {
					if (!PickleCraftPlugin.hasPerm(event.getPlayer(), "IgnoreCraft.ignore.cantbeignored")) {
						event.getRecipients().remove(players[i]);
					}
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin (PlayerJoinEvent event) {
		module.Load(event.getPlayer());
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerQuit (PlayerQuitEvent event) {
		module.Save(event.getPlayer());
		module.playerIgnoreList.remove(event.getPlayer());
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerKick (PlayerKickEvent event) {
		module.Save(event.getPlayer());
		module.playerIgnoreList.remove(event.getPlayer());
	}
	
}
