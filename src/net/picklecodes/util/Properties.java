package net.picklecodes.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;
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
 */
public class Properties {
	public Map<String, String> prop = new HashMap<String,String>();

	private File propFile;
	
	public Properties(JavaPlugin plugin, String file) {
		propFile = new File(plugin.getDataFolder(), file);
		if (!plugin.getDataFolder().exists()) { plugin.getDataFolder().mkdir(); }
		if (!propFile.exists()) { 
			try {
				propFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	public String getValue(String key) {
		if (prop.containsKey(key)) {
			return prop.get(key);
		}
		return null;
	}
	public void addValue(String key, String value) {
			prop.put(key, value);
	}
	
	
	public void remove(String key) {
			prop.remove(key);
	}
	
	/*
	 * Parses data from a file....
	 * 
	 */
	public void Parse() {
		try {
			 BufferedReader inputStream = new BufferedReader(new FileReader(propFile));
			 String line;
			while ((line = inputStream.readLine()) != null) {
				String[] Tokens = line.split("=");
				if (Tokens.length >= 2) {
					prop.put(Tokens[0], Tokens[1]);
				}
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void Save() {
		try {
			BufferedWriter writerStream = new BufferedWriter(new FileWriter(propFile));
			StringBuilder strBuilder = new StringBuilder();
			for (String key : prop.keySet())  {
				String value = prop.get(key).trim();
				if (!value.isEmpty()) {
					strBuilder.append(key.trim());
					strBuilder.append("=");
					strBuilder.append(value);
					strBuilder.append(System.lineSeparator());
				}
			}
			writerStream.write(strBuilder.toString());
			writerStream.flush();
		 } catch (FileNotFoundException e) {
				e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			net.picklecodes.PickleCraftPlugin.log.fine("IgnoreCraft Saved");
		}
	}
	
}
