package net.picklecraft.Modules.IRC;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class IRCbotPlayerListener implements Listener {
	private IRCbotModule module;
	public IRCbotPlayerListener(IRCbotModule module) {
		this.module = module;
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChat (PlayerChatEvent event) {
		module.getIRCbot().broadcast(event.getPlayer().getDisplayName() +": "+event.getMessage(),false);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin (PlayerJoinEvent event) {
		module.getIRCbot().broadcast(event.getPlayer().getDisplayName() +": "+event.getJoinMessage(),false);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit (PlayerQuitEvent event) {
		module.getIRCbot().broadcast(event.getPlayer().getDisplayName() +": "+event.getQuitMessage(),false);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerKicked (PlayerKickEvent event) {
		module.getIRCbot().broadcast(event.getPlayer().getDisplayName() +": was Kicked: "+event.getReason(),false);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerCommand (PlayerCommandPreprocessEvent event) {
		module.getIRCbot().broadcast(
				event.getPlayer().getDisplayName() +": tried command "+event.getMessage(),true);
	}
}
