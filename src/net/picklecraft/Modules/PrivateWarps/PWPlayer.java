package net.picklecraft.Modules.PrivateWarps;

import java.util.ArrayList;
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
    private String name;
    private List<Warp> warps = new ArrayList<Warp>();
    
    public PWPlayer(String name) {
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
        if (getWarp(name) == null) {
            Warp warp = new Warp(name,location);
            warps.add(warp);
            return true;
        }
        return false;
    }
}
