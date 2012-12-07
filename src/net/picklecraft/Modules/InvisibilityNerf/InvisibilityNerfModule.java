package net.picklecraft.Modules.InvisibilityNerf;

import java.util.ArrayList;
import java.util.List;
import net.picklecraft.Modules.IModule;
import net.picklecraft.PickleCraftPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * Copyright (c) 2012
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
 * @author Pickle
 */
public class InvisibilityNerfModule implements IModule, Listener {
	private PickleCraftPlugin plugin;
	private int maxCoolDown = 20;
	private List<SpyPlayer> players = new ArrayList<SpyPlayer>();
	private PlayerListener playerListener;
	private SpyTimer spyTimer;

	public InvisibilityNerfModule(PickleCraftPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public PickleCraftPlugin getPlugin() {
		return plugin;
	}
	@Override
	public String getName() {
		return "InvisibilityNerf";
	}
	@Override
	public void onDisable() {

	}

	@Override
	public void onEnable() {
		playerListener = new PlayerListener(this);
		plugin.getServer().getPluginManager().registerEvents(playerListener, plugin);
		maxCoolDown = plugin.getConfig().getInt("invisiblenerf.maxcooldown");
		spyTimer = new SpyTimer(this);
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, spyTimer, spyTimer.getTick(), spyTimer.getTick());
	}

	@Override
	public void sendCommandList(CommandSender sender) {

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
        String label, String[] args) {

		return false;
	}

	public List<SpyPlayer> getPlayers() { return this.players; }
	public void removePlayer(Player player) {
		for (int i=0; i < players.size(); i++) {
			if (player == players.get(i).getPlayer()) {
				players.remove(i);
			}
		}
	}
	public SpyPlayer getPlayer(Player player) {
		for (int i=0; i < players.size(); i++) {
			SpyPlayer p = players.get(i);
			if (player == p.getPlayer()) {
				return p;
			}
		}
		return null;
	}
	public SpyPlayer addPlayer(Player player, int duration, int cooldown) {
		PotionEffect effect = new PotionEffect(PotionEffectType.INVISIBILITY,duration,20);
		//set a default effect, look into grabing existing effect :3
		SpyPlayer spy = new SpyPlayer(player,effect,cooldown);
		players.add(spy);
		return spy;
	}
	public SpyPlayer addPlayer(Player player) {
		//generic add player
		return addPlayer(player,400,maxCoolDown);
	}
}
