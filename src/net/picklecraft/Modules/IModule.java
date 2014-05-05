package net.picklecraft.Modules;

import net.picklecraft.PickleCraftPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * Copyright (c) 2011-2014
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * @author Pickle
 */

public interface IModule {

    public void onDisable();

    public void onEnable();

    public PickleCraftPlugin getPlugin();

    public String getName();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args);

    public void sendCommandList(CommandSender sender);
}
