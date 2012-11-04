package net.picklecraft;

import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
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
 * @author Pickle
 *
 * this is a useless thing I made cause I was bored lol.
 */
public class Pony {
	private final static ChatColor[] rainbow = {
		ChatColor.RED,
		ChatColor.GOLD,
		ChatColor.YELLOW,
		ChatColor.GREEN,
		ChatColor.BLUE,
		ChatColor.DARK_BLUE,
		ChatColor.DARK_PURPLE,
        };
	private static String[] ponies = {
			ChatColor.WHITE+"Rarity is best pony!",
			ChatColor.GOLD+"AppleJack is best pony!",
			ChatColor.YELLOW+"Fluttershy is best pony",
			ChatColor.YELLOW+"FlutterBitch is best bitch.",
			ChatColor.YELLOW+"FlutterHurricane!!!!!!!!1",
			ChatColor.DARK_PURPLE+"Twilight sparkle is best pony!",
			ChatColor.DARK_BLUE+"Luna is best pony!",
			ChatColor.LIGHT_PURPLE+"and then I said, OATMEAL?! ARE YOU CRAZY -Pinkie Pie",
			rainbowizeString("RainbowDash") +ChatColor.BLUE+" is best pony.",
			ChatColor.YELLOW+"DerpyHooves is derpy...",
			"Jarvis <3 ponies! jk ;D",
			rainbowizeString("SONIC RAINBOOM"),
			rainbowizeString("ATOMIC RAINBOOM"),
			rainbowizeString("RAINBOW FIRE TRAIL!"),
            rainbowizeString("MLP:FIM IS TEH BEST!"),
			rainbowizeString("Pony Swag")
			};
	private static Random r = new Random();

	public static void SayRandom(Player player) {
		player.sendMessage(ponies[r.nextInt(ponies.length)]);
	}
	public static void Say(Player player, String arg) {
		try {
			int pony = Integer.parseInt(arg)-1;
			if (pony >= 0 && pony <= ponies.length-1) {
				player.sendMessage(ponies[pony]);
			}
			else {
				player.sendMessage("/pony 1-"+ponies.length);
			}
		}
		catch(NumberFormatException e) {
			player.sendMessage("Thats not a number!");
		}
	}

	public static String rainbowizeString(String string) {
		StringBuilder sb = new StringBuilder();
		int rb = 0;
	    char[] chars = string.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if ((int)chars[i] != 32) {
				sb.append(rainbow[rb]);
			}
			sb.append(chars[i]);
			if (++rb >= rainbow.length) { rb = 0; }
		}
		return sb.toString();
	}

}
