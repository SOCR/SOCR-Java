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
/*
 * Created on Dec 2, 2004
 */
package edu.ucla.stat.SOCR.core;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

/**
 * This class will be used for game and experiment, basically it has three parts.
 * the upmost part is a simulated toolbar, which is populated by addTool(). It also
 * can contain multiple toolbars, you can call addToolBar() to add more.
 * 
 * The middle part is for graph, you can add your component there by addGraph();
 * 
 * The lowest part is for TextArea, you can add your component there by addTable()
 * 
 * @author <A HREF="mailto:qma@loni.ucla.edu">Jeff Ma </A>
 */
public class MultiplePartsPanel extends SOCRValueSettable implements 
    MouseListener, MouseMotionListener {
    public static JApplet applet = null;
    protected JPanel mainPane = new JPanel(new BorderLayout());
    protected SOCRToolBar toolbar = new SOCRToolBar();
    protected SOCRToolBar toolbar2 = new SOCRToolBar();
    protected JPanel toolbars = new JPanel(new GridLayout(0,1));
    protected JPanel graphs = new JPanel(new GridLayout(1,0));
    protected JPanel tables = new JPanel(new GridLayout(1,0));    
    protected SOCRApplet.SOCRTextArea recordTable = new SOCRApplet.SOCRTextArea();
    protected String fName ="";
    
    /**
     * @return Returns the fName.
     */
    public String getName() {
        return fName;
    }
    /**
     * @param name The fName to set.
     */
    public void setName(String name) {
        fName = name;
    }
    //protected ValueSetter[] fValueSetters = new ValueSetter[0];
    
    public MultiplePartsPanel() {
    /*	JScrollPane js = new JScrollPane(toolbars);
    	js.setPreferredSize(new Dimension(300,100));
    	JSplitPane container = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                js, graphs );*/
    	
    	mainPane.add("North", toolbars); 
    	mainPane.add("Center", graphs);
    //	mainPane.add("North", container);
        
        addToolbar(toolbar);
        addToolbar(toolbar2);
        addTable(recordTable);
    }

    public JComponent getMainPanel() {
        return mainPane;
    }

    /**to  add the second tool bar*/
    public void addToolbar(Component c) {
        toolbars.add(c);
    }

    public void removeToolbar(){
    	toolbars.removeAll();
    }
    /**This method adds a new component usually a text areas to the bottom panel*/
    public void addTable(Component c) {
        tables.add(new JScrollPane(c));
    }

    /**This method adds a new component usually a graph to the center panel.*/
    public void addGraph(Component c) {
        graphs.add(c);
    }

    /**This method adds a new component (such as a button) to the main toolbar.*/
    public void addTool(Component c) {
        toolbar.add(c);
    }
    /**This method adds a new component (such as a button) to the second toolbar.*/    
    public void addTool2(Component c) {
        toolbar2.add(c);
    }
    
    /**
     * This method returns the record table
     * 
     * @uml.property name="recordTable"
     */
    public JTextArea getRecordTable() {
        return recordTable;
    }
    
    public void setApplet(JApplet a) {
        applet = (SOCRApplet) a;
	//  System.err.println("Set the MultiplePartsPanel.applet="+applet);
    }
    
    public static void play(String audiofile) {
        if (applet != null) applet.play(applet.getCodeBase(), audiofile);
    }
       
    public void mouseClicked(MouseEvent event){}
    public void mouseEntered(MouseEvent event){}
    public void mouseExited(MouseEvent event){}
    public void mousePressed(MouseEvent event){}
    public void mouseReleased(MouseEvent event){}
    public void mouseMoved(MouseEvent event){}
    public void mouseDragged(MouseEvent event){}
    
    public String format (double d) {
        return SOCRApplet.format(d);
    }     
    
    public static class SOCRToolBar extends JPanel {
        public SOCRToolBar() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
        }
    }
    public void update(Observable o, Object arg) {        
    }
    public JComponent getTextPanel() {
        return tables;
    }
}
