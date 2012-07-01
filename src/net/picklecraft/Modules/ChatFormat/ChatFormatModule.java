package net.picklecraft.Modules.ChatFormat;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.picklecraft.Modules.Counter.CounterSign;
import net.picklecraft.Modules.IModule;
import net.picklecraft.PickleCraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 *
 * Copyright (c) 2011-2012
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 *
 * @author Pickle
 */

/*
 * A simple replacement for mchat 
 * (as it's becoming annoying, adding random non useful functions) for my use.
 */
public class ChatFormatModule implements IModule, Listener {
    private Pattern pattern = Pattern.compile("%");
	private PickleCraftPlugin plugin;
	private PermissionManager permEx;
	
	private List<CounterSign> signs;
	
	public ChatFormatModule(PickleCraftPlugin plugin) {
            this.plugin = plugin;
            if(Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx")){
                permEx = PermissionsEx.getPermissionManager();
                plugin.getServer().getPluginManager().registerEvents(this, plugin);
            }
            else {
                PickleCraftPlugin.moduleManager.unloadModule(this);
                PickleCraftPlugin.log.info("Could not find pex!");
            }
	}

	@Override
	public void onDisable() {

	}
	
	@Override
	public void onEnable() {
            
	}
	
	@Override
	public PickleCraftPlugin getPlugin() {
            return plugin;
	}
	
	@Override
	public String getName() {
            return "ChatFormat";
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            return false;
	}
	
	@Override
	public void sendCommandList(CommandSender sender) {
            
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onChat(PlayerChatEvent event) {
            StringBuilder sb = new StringBuilder();
            PermissionUser user = permEx.getUser(event.getPlayer());
            sb.append(user.getPrefix());
            sb.append(event.getPlayer().getDisplayName());
            sb.append(user.getSuffix());
            /* exscape "%" in the message */
            Matcher m = pattern.matcher(event.getMessage());
            StringBuffer sb2 = new StringBuffer();
            boolean result = m.find();
            while(result) {
                m.appendReplacement(sb2, "%%");
                result = m.find();
            }
            m.appendTail(sb2);
            
            sb.append(sb2.toString());
            event.setFormat(PickleCraftPlugin.Colorize(sb.toString()));
        }
}
