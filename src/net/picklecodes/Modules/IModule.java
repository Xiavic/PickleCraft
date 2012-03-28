package net.picklecodes.Modules;

import net.picklecodes.PickleCraftPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
/**
 * Copyright 2011-2012
 * 
 * @author Pickle
 *
 */
public interface IModule {
	public void onDisable();
	public void onEnable();
	public PickleCraftPlugin getPlugin();
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args);
	public void sendCommandList(CommandSender sender);
}
