package net.picklecraft.Modules.PrivateWarps;

import com.sk89q.wepif.PermissionsResolverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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

 * @author Pickle
 */
public class PWPlayer {
	private PWModule module;
    private String name;
    private List<Warp> warps = new ArrayList<Warp>();
	private int limit = -1; //-1 respresents unlimited.
    public PWPlayer(PWModule module, String name) {
		this.module = module;
        this.name = name;
    }

    public String getName() { return this.name; }

    public List<Warp> getWarps() { return warps; }

    public Warp getWarp(String name) {
        for (Warp warp : warps) {
            if (warp.getName().equalsIgnoreCase(name)) {
                return warp;
            }
        }
        return null;
    }
    public boolean removeWarp(String name) {
        for (int i = 0; i < warps.size(); i++) {
            if (warps.get(i).getName().equalsIgnoreCase(name)) {
                warps.remove(i);
                return true;
            }
        }
        return false;
    }
    public int setWarp(String name, Location location, boolean ignoreLimit) {
		//return 1 for success
		//0 for existing warp fail
		//-1 for limit reach
		int maxLimit = ignoreLimit ? -1 : GetMaxLimit(); //allows users to use warps already exist if config change
		//also is a loophole hack fix for getmaxlimit null when json is loaded.
		if (warps.size() < maxLimit || maxLimit == -1) {
			//if limit is greater than limit, deny adding.
			//if limit is "-1" then no limit applied.
			if (getWarp(name) == null) {
				Warp warp = new Warp(name,location);
				warps.add(warp);
				return 1;
			}
			else {
				return 0;
			}
		}
		else {
			return -1;
		}
    }


	public int GetMaxLimit() {
		limit = -1;
		PermissionsResolverManager p = PermissionsResolverManager.getInstance();
		String[] groups = p.getGroups(this.name); // get all groups the player is in
		HashMap<String,Integer> m = module.GetMaxLimitRanks(); // get the rank keys.
		for (int g = 0; g < groups.length; g++) {
			String lowerG = groups[g].toLowerCase(); // lowercase to match anything ;3
			if (m.containsKey(lowerG)) {
				Integer li = m.get(lowerG);
				if (li == -1) { //if we find group with -1, don't bother with the rest.
					limit = li;
					return limit;
				}
				//get the highest limit from all groups.
				if (li > limit) { //if key is the same as a group the player is in, then compare if it is higher.
					limit = li;
				}
			}
		}
		//return -1 so all nonconfig ranks are unlimited.
		return limit;
	}
}
