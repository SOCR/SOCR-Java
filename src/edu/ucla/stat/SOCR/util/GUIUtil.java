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

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUIUtil {
    /**
     * Adds a component into a container component of <code> GridBagLayout.
     *
     * @param container a <code>Container </code> where a component will be added
     * @param c a <code> Component </code> to be added into a container
     * @param x an interger specifying the x coordiante of the added component
     * @param y an integer specifying the y coordinate of the added component
     * @param w an integer specifying the width of the added component
     * @param h an integer specifying the height of the added component
     * @param anchor an interger specifying the anchor point of the added component
     * @param wx an double specifying the extra horizontal space distribution
     * @param wy an double specifying the extra vertical space distribution
     * @throws AWTException Abstract Window Toolkit exception
     */
    public static void addComponent(Container container, Component c,int x,int y,
        int w,int h,int fill,int anchor,int wx,int wy)  throws AWTException {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5,5,2,2);
            gbc.gridx = x;
            gbc.gridy =y;
            gbc.gridwidth=w;
            gbc.gridheight=h;
            gbc.fill=fill;
            gbc.anchor=anchor;
            gbc.weightx=wx;
            gbc.weighty=wy;
            addComponent(container, c, gbc);
    }

    public static void addComponent(Container container, Component c,
        int x, int y, int w, int h)  throws AWTException {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,2,2);
        gbc.gridx = x;
        gbc.gridy =y;
        gbc.gridwidth=w;
        gbc.gridheight=h;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        addComponent(container, c, gbc);
    }

    /** add the component with the label at left to the JPanel at row */
    public static void addInput2Pane(String label, Component c, JPanel p, int row)
            throws AWTException {
        JLabel jlabel = new JLabel(label);
        int a = GridBagConstraints.EAST, f = GridBagConstraints.HORIZONTAL;
        addComponent(p,jlabel,0,row,1,1,f,a,0,0);
        addComponent(p,     c,1,row,1,1,f,a,1,1);
    }

    public static void addComponent(Container container, Component c,
            GridBagConstraints gbc)  throws AWTException {
        LayoutManager lm = container.getLayout();
        if (!(lm instanceof GridBagLayout)) {
            throw new AWTException("Invalid layout" +lm);
        } else {
            container.add(c,gbc);
        }
    }
    
   /**
     * Gets the toppest parent of a component.
     *
     * @param a <code> Component </code>
     * @returns a <code> Frame </code> specifying the toppest parent of a component
     */
    public static Frame getTopestParent(Component component) {
        Component c = component;
        while(c.getParent() != null ) c = c.getParent();
        if (c instanceof Frame) return (Frame)c;
        else return null;
    }

}

