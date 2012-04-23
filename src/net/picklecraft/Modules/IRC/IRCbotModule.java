package net.picklecraft.Modules.IRC;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.picklecraft.PickleCraftPlugin;
import net.picklecraft.Modules.IModule;

public class IRCbotModule implements IModule {
	private PickleCraftPlugin plugin;
	
	private IRCbotPlayerListener playerListener;
	
	private IRCbot bot;

	public IRCbotModule(PickleCraftPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onDisable() {
		bot.quitServer("Server disabled us!");
		bot = null;
	}

	@Override
	public void onEnable() {
		bot = new IRCbot(this);
		playerListener = new IRCbotPlayerListener(this);
		this.getPlugin().getServer().getPluginManager().registerEvents(playerListener,this.getPlugin());
	}

	@Override
	public PickleCraftPlugin getPlugin() {
		return plugin;
	}

	@Override
	public String getName() {
		return "IRCbot";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		return false;
	}

	@Override
	public void sendCommandList(CommandSender sender) {
		
	}
	
	public IRCbot getIRCbot() {
		return bot;
	}

}
