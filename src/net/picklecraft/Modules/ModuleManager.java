package net.picklecraft.Modules;

import java.util.ArrayList;
import java.util.List;
import net.picklecraft.PickleCraftPlugin;

/**
 * Copyright (c) 2011-2014
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Pickle
 *
 */
public class ModuleManager {

    public final List<IModule> modules = new ArrayList<>();

    private final PickleCraftPlugin plugin;

    public ModuleManager(PickleCraftPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadModules() {
        List<String> mods = this.plugin.getConfig().getStringList("modules");
        String[] ms = new String[mods.size()];
        List<IModule> imods = ModuleLoader.getModules(this.plugin, mods.toArray(ms));
        for (int i = 0; i < imods.size(); i++) {
            loadModule(imods.get(i));
        }
    }

    public void reloadModules() {
        for (IModule module : modules) {
            unloadModule(module);
        }
        modules.clear();
        loadModules();
    }

    public void loadModule(IModule module) {
        if (module != null) {
            module.onEnable();
            modules.add(module);
        }
    }

    public void unloadModule(IModule module) {
        if (module != null) {
            module.onDisable();
            modules.remove(module);
        }
    }

    public void unloadModules() {
        for (IModule module : modules) {
            module.onDisable();
        }
        modules.clear();
    }

    public void enableModule(IModule module) {
        module.onEnable();
    }

    public void enableModules() {
        for (IModule module : modules) {
            module.onEnable();
        }
    }

    public void disableModule(IModule module) {
        module.onDisable();
    }

    public void disableModules() {
        for (IModule module : modules) {
            module.onDisable();
        }
    }

    public IModule getModule(String name) {
        for (IModule module : modules) {
            if (module.getName().equals(name)) {
                return module;
            }
        }
        return null;
    }

}
