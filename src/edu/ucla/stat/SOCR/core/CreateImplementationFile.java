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
package edu.ucla.stat.SOCR.core;

import java.applet.*;
import java.io.*;
import java.lang.reflect.*;

import javax.swing.*;

/**
 * this class used to create Implementation files used to initiate the comboBox,
 * its goal is to replace the ClassLoader.
 * 
 * @author <A HREF="mailto:qma@loni.ucla.edu">Jeff Ma </A>
 */
public class CreateImplementationFile {
    
    public static void usage() {
        System.out.println("CreateImplementation class need 3 arguments\n\n"
                + "package name (e.g. edu.ucla.stat.SOCR.distributions\n\n" 
                + "class_dir (e.g. C:/eclipse/workspace/PIPELINESOCR/classes\n\n" 
                + "output filename (e.g. implementedDistributions.txt\n\n\n" );
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            usage();
            return;
        }
        String package_name = args[0];
        String class_dir = args[1];
        String outputfilename =  args[2];
        
        String dirname = class_dir + '/' + package_name.replace('.', '/');
        File dir = new File(dirname);
        if (!dir.exists()) {
            System.out.println(dirname + " not exists");
            return;
        }

        StringBuffer header = new StringBuffer(
                "# This file created by edu.ucla.stat.SOCR.core.CreateImplementationFile,\n" + 
                "# search from " + dir.getAbsolutePath() + "\n" +
                "# current format is \"name, classname\"\n");
        
        StringBuffer content = new StringBuffer();

        String[] fs = dir.list();
        header.append("# there are " + fs.length + " files in the package " 
                + package_name+ "\n");
        for (int i = 0; i < fs.length; i++) {
            try {
                if (fs[i].indexOf("$") != -1) {
                    header.append("# " + fs[i] + " is an Inner class\n");
                } else if (!fs[i].endsWith(".class")) {
                    header.append("# " + fs[i] + "is not a .class file\n");
                } else {                    
                    int index = fs[i].lastIndexOf(".class");
                    String name = fs[i].substring(0, index);
                    Class c = Class.forName(package_name + "." +name);
                    if ((c.getModifiers() & Modifier.ABSTRACT) != 0) {
                        header.append("# " + fs[i] + " is an abstract class\n");
                        continue; //abstract
                    }
                    //System.out.println (Modifier.toString(c.getModifiers()));
                    String label = null;
                    Object o = c.newInstance();
                    if (o instanceof JApplet) {
                        ((JApplet) o).init();
                        label = ((JApplet) o).getName();
                    } else if (o instanceof Pluginable) {
                        label = ((Pluginable)o).getName();
                    }  else if (o instanceof JComponent) {
                        label = ((JComponent) o).getName();
                    }
                    if (label == null || label.equals("")) label = name;

                    content.append("\n" + label + ", " + c.getName());
                }
            } catch (Throwable e1) {
                header.append("# " + fs[i] + " seemed not a pluginable or " +
                        "don't have a non-parameter contructor\n");
                e1.printStackTrace();
            }
        }
        
        header.append(content);
        System.out.println(header);
        try {
            FileWriter fw = new FileWriter(outputfilename);
            fw.write(header.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

