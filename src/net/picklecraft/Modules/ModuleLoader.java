package net.picklecraft.Modules;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
public class ModuleLoader {
    private ModuleManager manager; 
    public ModuleLoader(ModuleManager manager) {
        this.manager = manager;
    }
    
    public ArrayList<IModule> getModules(String[] moduleList) {
        ArrayList<IModule> modules = new ArrayList<IModule>();
        
        return modules;
    }
    
    /** Modified version from
     * http://stackoverflow.com/questions/1456930/how-do-i-read-all-classes-from-a-java-package-in-the-classpath
     */
    public static Class<?>[] getClassFromJar(String module) throws IOException{
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        
        module = module.replace(".", "/");
        URL moduleURL = classLoader.getResource(module);

        if(moduleURL.getProtocol().equals("jar")){
            String jarFileName;
            JarFile jf ;
            Enumeration<JarEntry> jarEntries;
            String entryName;

            // build jar file name, then loop through zipped entries
            jarFileName = URLDecoder.decode(moduleURL.getFile(), "UTF-8");
            jarFileName = jarFileName.substring(5,jarFileName.indexOf('!'));
            System.out.println(">"+jarFileName);
            jf = new JarFile(jarFileName);
            jarEntries = jf.entries();
            while(jarEntries.hasMoreElements()){
                entryName = jarEntries.nextElement().getName();
                if(entryName.startsWith(module) && entryName.length()>module.length()+5){
                    entryName = entryName.substring(module.length(),entryName.lastIndexOf('.'));
                    try {
                        classes.add(classLoader.loadClass(entryName));
                    } catch (ClassNotFoundException ex) {
                        //Do nothing.
                    }
                }
            }
        }
        Class<?>[] temparray = new Class<?>[classes.size()];
        return classes.toArray(temparray);
    }
   
}

