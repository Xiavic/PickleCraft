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
	private int limit = -2; //-2 respresents we haven't updated yet.
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
    public boolean setWarp(String name, Location location) {
		int maxLimit = GetMaxLimit();
		if (warps.size() > maxLimit || maxLimit == -1) {
			//if limit is greater than limit, deny adding.
			//if limit is "-1" then no limit applied.
			if (getWarp(name) == null) {
				Warp warp = new Warp(name,location);
				warps.add(warp);
				return true;
			}
			module.getPlugin().getStringFromConfig("privatewarps.messages.errors.overlimit", name);
		}
        return false;
    }


	public int GetMaxLimit() {
		if (limit == -2) { // if limit is -2, then grab a new limit
			limit = -1;
			PermissionsResolverManager p = PermissionsResolverManager.getInstance();
			String[] groups = p.getGroups(this.name); // get all groups the play is in
			for (int g = 0; g < groups.length; g++) {
				HashMap<String,Integer> m = module.GetMaxLimitRanks(); // get the rank keys.
				for (String key : m.keySet()) {
					if (key.equalsIgnoreCase(groups[g])) {
						int l = m.get(key);
						//get the highest limit from all groups.
						if (l > limit) { //if key is the same as a group the player is in, then compare if it is higher.
							limit = l;
						}
					}
				}
			}
			//return -1 so all nonconfig ranks are unlimited.
		}
		return limit;
	}
}
