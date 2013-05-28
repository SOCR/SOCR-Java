/****************************************************
Statistics Online Computational Resource (SOCR)
http://www.StatisticsResource.org
 
All SOCR programs, materials, tools and resources are developed by and freely disseminated to the entire community.
Users may revise, extend, redistribute, modify under the terms of the Lesser GNU General Public License
as published by the Open Source Initiative http://opensource.org/licenses/. All efforts should be made to develop and distribute
factually correct, useful, portable and extensible resource all available in all digital formats for free over the Internet.
 
SOCR resources are distributed in the hope that they will be useful, but without
any warranty; without any explicit, implicit or implied warranty for merchantability or
fitness for a particular purpose. See the GNU Lesser General Public License for
more details see http://opensource.org/licenses/lgpl-license.php.
 
http://www.SOCR.ucla.edu
http://wiki.stat.ucla.edu/socr
 It s Online, Therefore, It Exists! 
****************************************************/
package edu.ucla.stat.SOCR.util;

/*
 * SimpleClassLoader.java - a bare bones class loader.
 *
 * Copyright (c) 1996 Chuck McManis, All Rights Reserved.
 *
 * Permission to use, copy, modify, and distribute this software
 * and its documentation for NON-COMMERCIAL purposes and without
 * fee is hereby granted provided that this copyright notice
 * appears in all copies.
 *
 * CHUCK MCMANIS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
 * SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. CHUCK MCMANIS
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

 // Modified by dushyanth Krishnamurthy 

import java.util.Hashtable;
//import util.ClassFile;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

//import edu.ucla.stat.SOCR.modeler.*;
//import plugins.Modeler.Modeler;
import java.io.*;
import java.util.zip.*;
import java.net.*;
import java.util.*;
//import sun.security.
//import sun.plugin.security.*;
//import sun.jdbc.odbc
//import sun.applet.AppletClassLoader;
//import sun.applet.A
public class SimpleClassLoader extends ClassLoader {
    private Hashtable classes = new Hashtable();
    private URL filePath;
    private URL classFile;
    private ClassLoader Loader;
    
    
    public SimpleClassLoader(URL path, ClassLoader loader) {
    Loader = loader;
    filePath = path;
    //Loader.
    //System.out.println("\n simple class loader:  "+ this.getClass().toString());
    //System.out.println("\n SUPER simple class loader:  "+ super.getClass().toString());
    //System.out.println("\n System simple class loader:  "+ super.getSystemClassLoader().getClass().toString());
    
    }

    /**
     * This sample function for reading class implementations reads
     * them from the local file system
     */
    private byte getClassImplFromPluginFolder(String className)[] {
    	System.out.println("        >>>>>> Fetching the implementation of "+className);
    	byte result[];
    	
       
        try {
        	classFile = new URL(filePath.toString()+className+".class");
        	//System.out.println("Byte Data path" + classFile.toString());
        	InputStream ips = classFile.openStream();
        	//FileInputStream fi = new FileInputStream(filePath+"/"+className+".class");
    	    result = new byte[ips.available()];
    	    //fi.read(result);
    	    ips.read(result);
    	    return result;
            
    	} catch (Exception e) {

    	    e.printStackTrace();
            /*
    	     * If we caught an exception, either the class wasnt found or it
    	     * was unreadable by our process.
    	     */
    	    return null;
    	}
        
       
    }

    /**
     * This is a simple version for external clients since they
     * will always want the class resolved before it is returned
     * to them.
     */
    public Class loadClass(String className) throws ClassNotFoundException {
        //filePath = path;
        //System.out.println("step -1");
        return (loadClass(className, true));
    }

    /**
     * This is the required version of loadClass which is called
     * both from loadClass above and from the internal function
     * FindClassFromClass.
     */
    public synchronized Class loadClass(String className, boolean resolveIt)
    	throws ClassNotFoundException{
        Class result;
        byte  classData[];
        //filePath = path;
        //System.out.println("        >>>>>> Load class : "+className);
        //System.out.println("step 0");
        /* Check our local cache of classes */
        result = (Class)classes.get(className);
        if (result != null) {
            System.out.println("        >>>>>> returning cached result.");
            return result;
        }
        //System.out.println("step one");
        /* Check with the primordial class loader */
        try {
            //result =  this.Loader.findSystemClass(className);
            
            //result = Loader.getClass().
            //result = super. loadClass(className);
            result = super.findSystemClass(className);
            // super.fin
           // System.out.println(
            
          //  System.out.println("        >>>>>> returning system class (in CLASSPATH).");
            return result;
        } catch (ClassNotFoundException e) {
            //super.loadClass(className);
            //System.out.println("        >>>>>> Not a system class.");
            
        }
        
         try {
            //result =  this.Loader.findSystemClass(className);
            
            //result = Loader.getClass().
            //result = super. loadClass(className);
           // System.out.println("this is the class name trying to load by Loader  " + className);  
            result = Loader.loadClass(className);
           // result = Loader.l .findLoadedClass(className);
            // super.fin
           // 
            
            //System.out.println("        >>>>>> returning system class by super.");
            return result;
        } catch (Exception e) {
            //super.loadClass(className);
           // System.out.println("        >>>>>> Not a super class.");
            
        }
        
       // System.out.println("step two");
        /* Try to load it from our repository */
        classData = getClassImplFromPluginFolder(className);
        if (classData == null) {
            
         //   System.out.println("part III");
            throw new ClassNotFoundException();
            
        }
       // System.out.println("step three");
        try{
        /* Define it (parse the class file) */
            if(classData == null)
            System.out.println("step three and half");
                //super.loadClass(className);
            else
            result =  defineClass(className,classData, 0, classData.length);
        if (result == null) {
            //result = super.defineClass(name
            throw new ClassFormatError();
        }
        }catch(Exception e) {
        e.printStackTrace();
        return null;
        }
        
       // System.out.println("step four");
        if (resolveIt) {
            resolveClass(result);
        }

        classes.put(className, result);
       // System.out.println("        >>>>>> Returning newly loaded class.");
        return result;
    }
    
      byte[] loadFromJar(String jar, String name) {
       // System.out.println("trying to load from JAR");
        
        BufferedInputStream bis = null;
        try {
            ZipFile jarFile = new ZipFile(jar);
         //   System.out.println("Jar file size"+jarFile.size());
            //jarFile.size()
            Enumeration entries = jarFile.entries();
            
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
           //     System.out.println(entry.getName() +"  "+name + "end of line\n");
                if (entry.getName().equals(name)) {
                    bis = new BufferedInputStream(jarFile.getInputStream(entry));
                    int size = (int)entry.getSize();
                    byte[] data = new byte[size];
                    int b=0, eofFlag=0;
                    while ((size - b) > 0) {
                        eofFlag = bis.read(data, b, size - b);
                        if (eofFlag==-1) break;
                        b += eofFlag;
                    }
             //      System.out.println(data.length);
                    return data;
                }
            }
        }
        catch (Exception e) {e.printStackTrace();}
        finally {
            
            try {if (bis!=null) bis.close();}
            catch (IOException e) {}
        }
        return null;
    }

    
}



