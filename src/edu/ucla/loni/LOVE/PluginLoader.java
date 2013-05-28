package edu.ucla.loni.LOVE;

//import

import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.io.File;


//local import
//import edu.ucla.loni.LOVE.colormap.plugins.GrayColorMap;

/**
 * This class will look at .class files under plugins folder
 * and retrieve these classes for use as plugins.
 * <p/>
 * It provides centralized service for plugin loading. All the
 * plugin classes that are loaded before will be stored in cache
 * for later usages.
 * <p/>
 * The reason to extend <code>ClassLoader</code> is that we can use
 * those native, protected method of it. Those method can provide many
 * powerful functionality.
 * <p/>
 * BUG/TO DO: Still don't know how to deal with Jarred class file.
 * if classes are jarred, the program can no longer list
 * all the files in the directory thus can't load classes.
 */
public class PluginLoader extends ClassLoader
{
    /**
     * Cache to store all the plugins. Init size: 20.
     * Key is a string with path information like:
     * plugin.color.GrayColorMap
     */
    static Hashtable cache = new Hashtable(20);

    /**
     * Load the classes in a directory .plugins.*
     * Example of usage: loadDirectory("edu.ucla.loni.LOVE.plugin.colormap");
     *
     * @param packDir The directory to be loaded
     */
    public static Hashtable loadDirectory(String packDir)
    {
        //create the hashtable to be returned
        Hashtable classTable = new Hashtable(5);

        // step 1. Determine if this program is runing as
        //         jar file or not.
        URL url = PluginLoader.class.getResource("/edu/ucla/loni/LOVE/");
        try
        {
            URLConnection urlConnection = url.openConnection();
            if (urlConnection instanceof JarURLConnection)
            {
                //it is run from a jar file
                //get the jar file for this connection
                JarFile jarFile = ((JarURLConnection) urlConnection).getJarFile();
                Enumeration entries = jarFile.entries();

                //get the name of package
                String path = packDir.replace('.', '/');
                //iterate through all the entries
                while (entries.hasMoreElements())
                {
                    JarEntry entry = (JarEntry) entries.nextElement();
                    if (!entry.isDirectory())
                    {
                        //it is possible that this is a class file to be loaded
                        String entryName = entry.getName();

                        if (entryName.endsWith(".class"))
                        {
                            //test if this entry belong to the package
                            int index = entryName.lastIndexOf('/');
                            if (entryName.substring(0, index).equals(path))
                            {
                                //load the class corresponding to this entry
                                String className =
                                        entryName.substring(index + 1,
                                                entryName.length() - 6);
                                Class aClass = loadAClass(packDir + "." + className);
                                if (aClass != null)
                                {
                                    classTable.put(className, aClass);
                                }
                            }
                        }
                    }
                }
                return classTable;
            }//end of "if(urlConnection instanceof JarURLConnection)"
            else
            {
                //The program is loaded from uncompressed file system
                //1. find all the files in this directory
                //String path = Preference.getHomeDir();

                //path = path + File.separatorChar
                //        + packDir.replace('.', File.separatorChar);

                //File classDir = new File(path);

                url = PluginLoader.class.getResource("/" + packDir.replace('.', '/'));
                File classDir = new File(url.toURI());
                String[] fileList = classDir.list();

                //2. load all the files as class and store them into the hashtable
                //   name of the class is stored as key. Also this class will be cached.
                //
                //   since user has already decide which dir to load, we don't preserve the
                //   direcotry information in the key.
                if (fileList != null)
                {
                    //there is file in this directory

                    int i;

                    for (i = 0; i < fileList.length; i++)
                    {
                        if (fileList[i].endsWith(".class"))
                        {
                            String className = fileList[i].
                                    substring(0, fileList[i].indexOf("."));

                            Class aClass = loadAClass(packDir + "." + className);
                            if (aClass != null)
                            {
                                classTable.put(className, aClass);
                            }
                        }
                    }
                    return classTable;
                }
            }
        }
        catch (Exception e)
        {
            //any exception indicates that it is not loaded from Jar file
        }

        //no file in this directory, return <code>null</code>.
        return null;
    }

    /**
     * Load a single class as specified by the parameter.
     *
     * @param className Name of the class, including package name.
     */
    public static Class loadAClass(String className)
    {
        Class loadedClass = (Class) cache.get(className);
        if (loadedClass != null)
        {
            return loadedClass;
        }
        //not found in cache
        try
        {
            // the three argument Class.forName is used here.
            // See the reason at:
            loadedClass = Class.forName(className,
                    true,
                    ClassLoader.getSystemClassLoader());
            cache.put(className, loadedClass);

            return loadedClass;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //can't load the class successfully
        return null;
    }

    /**
     * Test program
     */
    public static void main(String args[]) throws Exception
    {
        Hashtable classTable = loadDirectory("edu.ucla.loni.LOVE");
        Enumeration e = classTable.keys();
        for (; e.hasMoreElements();)
        {
            System.out.println(e.nextElement());
        }
        //test of new class
        //Class grayColorMapClass = (Class)classTable.get("GrayColorMap");
        //GrayColorMap grayColorMap = (GrayColorMap)grayColorMapClass.newInstance();
        //System.out.println(grayColorMap);
        //System.out.println(grayColorMapClass.getClassLoader());

    }
}
