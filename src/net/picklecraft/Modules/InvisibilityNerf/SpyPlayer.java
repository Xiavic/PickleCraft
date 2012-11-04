package net.picklecraft.Modules.InvisibilityNerf;

import java.util.Collection;
import org.bukkit.entity.Player;
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
public class SpyPlayer {
	private Player player;
	private int maxCoolDown = 400;
	private int coolDown = 400;
	private int duration;
	private PotionEffect effect;
	private boolean invisible;

	public SpyPlayer(Player player, PotionEffect effect, int maxCoolDown) {
		this.player = player;
		this.maxCoolDown = maxCoolDown;
		this.effect = effect;
		this.duration = effect.getDuration();
	}

	public Player getPlayer() { return this.player; }
	public int getDuration() { return this.duration; }
	public void setDuration(int duration) { this.duration = duration; }

	public int getCoolDown() { return this.coolDown; }
	public void setCoolDown(int coolDown) {
		this.coolDown = coolDown;
		if (coolDown <= 0) { setInvisible(false); }
		if (coolDown > maxCoolDown) { this.coolDown = maxCoolDown; }
	}
	public boolean getInvisible() { return this.invisible; }
	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
		if (this.invisible) {
			player.addPotionEffect(effect);
		}
		else {
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
		}
	}

	public static boolean hasInvisEffect(Player player) {
		//Check if we still have effect from potion
		return player.hasPotionEffect(PotionEffectType.INVISIBILITY);
	}

}
