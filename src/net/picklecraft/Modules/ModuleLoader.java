package net.picklecraft.Modules;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.picklecraft.PickleCraftPlugin;

/**
 *
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
 */
public class ModuleLoader {

    public static List<IModule> getModules(PickleCraftPlugin plugin, String[] moduleList) {
        List<IModule> modules = new ArrayList<>();
        for (int s = 0; s < moduleList.length; s++) {
            try {
                Class<?> c = Class.forName(moduleList[s]);
                if (c == null) {
                    continue;
                }
                if (IModule.class.isAssignableFrom(c)) {
                    Constructor<?> con = c.getConstructor(PickleCraftPlugin.class);
                    modules.add((IModule) con.newInstance(plugin));
                    //PickleCraftPlugin.log.log(Level.INFO, "Loaded Module {0}", moduleList[s]);
                }
            } catch (InvocationTargetException ex) {
                Logger.getLogger(ModuleLoader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                PickleCraftPlugin.log.log(Level.SEVERE, "Failed to To instantiate module {0}", moduleList[s]);
            } catch (ClassNotFoundException ex) {
                PickleCraftPlugin.log.log(Level.SEVERE, "Can not find module {0}", moduleList[s]);
            } catch (Exception ex) {
                PickleCraftPlugin.log.log(Level.SEVERE, "Something bad happened while loading {0} Error: {1}", new Object[]{moduleList[s], ex.getMessage()});
            }
        }
        return modules;
    }

}
