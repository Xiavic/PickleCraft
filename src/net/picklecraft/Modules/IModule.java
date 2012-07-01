package net.picklecraft.Modules;

import net.picklecraft.PickleCraftPlugin;
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
	public String getName();
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args);
	public void sendCommandList(CommandSender sender);
}