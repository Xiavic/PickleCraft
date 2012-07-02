package net.picklecraft.Modules.PrivateWarps;

import org.bukkit.Location;

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
public class Warp {
    private String name;
    private Location location;
    
    public Warp(String name, Location location) {
        this.name = name;
        this.location = location;
    }
    public String getName() { return this.name; }
    public Location getLocation() { return this.location; }
}
