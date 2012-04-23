package net.picklecraft.Modules.IRC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.picklecraft.PickleCraftPlugin;

import org.bukkit.ChatColor;
import org.jibble.pircbot.Colors;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

public class IRCbot extends PircBot {
	private IRCbotModule module;
	
	private String Nick = "PickleCraft";
	
	private List<String> channels = new ArrayList<String>();
	private String secureChannel;
	
	public IRCbot(IRCbotModule module) {
		
		this.module = module;
		String ss = module.getPlugin().getConfig().getString("ircbot.nickname");
		if (ss != null) { Nick = ss; }
		this.setName(Nick);
		String[] s = module.getPlugin().getConfig().getString("ircbot.server").split(":");
		String server = s[0];
		int port = 6667;
		if (s.length > 1) {
			try {
				port = Integer.parseInt(s[1]);
			}
			catch(NumberFormatException e) {
				PickleCraftPlugin.log.warning("Port is invaild, trying default.");
			}
		}
		
		try {
			this.connect(server,port);
			List<?> c = module.getPlugin().getConfig().getList("ircbot.relay_channels");
			for (int i=0; i < c.size(); i++) {
				String chan = String.valueOf(c.get(i));
				channels.add(chan);
				this.joinChannel(chan);
			}
			secureChannel = module.getPlugin().getConfig().getString("ircbot.sevure_channel");
			this.joinChannel(secureChannel);
		} catch (NickAlreadyInUseException e) {
			PickleCraftPlugin.log.warning("Nick is in use!!!");
		} catch (IOException e) {
			PickleCraftPlugin.log.warning("Failed to connect to irc.");
		} catch (IrcException e) {
			PickleCraftPlugin.log.warning("Some error with irc?");
			e.printStackTrace();
		}
	}
	
	public String getSecureChannel() { 
		return secureChannel;
	}
	
	public void broadcast(String message,boolean secure) {
		if (!secure) {
			for (int i=0; i < channels.size(); i++) {
				this.sendMessage(channels.get(i), message);
			}
		}
		this.sendMessage(secureChannel, message);
	}	
	
	@Override
	public void onMessage(String channel, String sender,
            String login, String hostname, String message) {
		module.getPlugin().getServer().broadcastMessage(
				PickleCraftPlugin.Colorize("[IRC]<"+sender+"> "+message)
				);
				
	}

}
